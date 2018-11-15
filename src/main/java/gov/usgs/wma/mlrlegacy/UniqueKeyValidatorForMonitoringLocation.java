package gov.usgs.wma.mlrlegacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueKeyValidatorForMonitoringLocation implements ConstraintValidator<UniqueKey, MonitoringLocation> {
	private static transient final Logger LOG = LoggerFactory.getLogger(UniqueKeyValidatorForMonitoringLocation.class);
	
	private final MonitoringLocationDao dao;
	private final ObjectMapper objectMapper;
	
	@Autowired
	public UniqueKeyValidatorForMonitoringLocation(MonitoringLocationDao dao, ObjectMapper objectMapper) {
		this.dao = dao;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public void initialize(UniqueKey constraintAnnotation) {
		// Nothing for us to do here at this time.
	}

	@Override
	public boolean isValid(MonitoringLocation newOrUpdatedMonitoringLocation, ConstraintValidatorContext context) {
		boolean valid = true;

		if (null != newOrUpdatedMonitoringLocation && null != context) {
			valid = isValidSiteNumberAndAgencyCode(newOrUpdatedMonitoringLocation, context);
			valid &= isValidNormalizedStationName(newOrUpdatedMonitoringLocation, context);
		}

		return valid;
	}
	
	/**
	 * 
	 * No two monitoring locations should share the same Agency Code and Site Number.
	 * 
	 * Side effects: adds constraint violations to `context`
	 * @param newOrUpdatedMonitoringLocation
	 * @param context
	 * @return true if valid, false if invalid
	 */
	protected boolean isValidSiteNumberAndAgencyCode(MonitoringLocation newOrUpdatedMonitoringLocation, ConstraintValidatorContext context) {
		boolean valid = true;
		if (null != newOrUpdatedMonitoringLocation.getAgencyCode() && null != newOrUpdatedMonitoringLocation.getSiteNumber()) {
			Map<String, Object> filters = new HashMap<>();
			filters.put(Controller.AGENCY_CODE, newOrUpdatedMonitoringLocation.getAgencyCode());
			filters.put(Controller.SITE_NUMBER, newOrUpdatedMonitoringLocation.getSiteNumber());
			MonitoringLocation existingMonitoringLocation = dao.getByAK(filters);
			if (existingMonitoringLocation != null) {
				if (isCreate(newOrUpdatedMonitoringLocation) || sameId(existingMonitoringLocation, newOrUpdatedMonitoringLocation)) {
					valid = false;
					String msg = "Monitoring Locations with Duplicate Agency Code and Site Number were found:\n";
					msg += serializeMls(Arrays.asList(existingMonitoringLocation));
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate(msg)
						.addPropertyNode(Controller.SITE_NUMBER)
						.addPropertyNode(Controller.AGENCY_CODE)
						.addConstraintViolation();
				}
			}
		}
		return valid;
	}
	
	/**
	 * 
	 * No two monitoring locations should share the same normalized station 
	 * name. (stationIx)
	 * 
	 * Side effects: adds constraint violations to `context`
	 * @param newOrUpdatedMonitoringLocation
	 * @param context
	 * @return true if valid, false if invalid
	 */
	protected boolean isValidNormalizedStationName(MonitoringLocation newOrUpdatedMonitoringLocation, ConstraintValidatorContext context) {
		boolean valid;
		if (isCreate(newOrUpdatedMonitoringLocation)) {
			valid = isValidNormalizedStationNameCreate(newOrUpdatedMonitoringLocation, context);
		} else {
			valid = isValidNormalizedStationNameUpdate(newOrUpdatedMonitoringLocation, context);
		}
	
		return valid;
	}
	
	/**
	 * 
	 * Side effects: adds constraint violations to `context`
	 * @param monitoringLocationUpdate
	 * @param context
	 * @return true if valid, false if invalid
	 */
	protected boolean isValidNormalizedStationNameUpdate(MonitoringLocation monitoringLocationUpdate, ConstraintValidatorContext context) {
		boolean valid = true;
		//it's OK if the stationIx isn't populated because it could be provided in the database
		if (null != monitoringLocationUpdate.getStationIx()) {
			Map<String, Object> filters = new HashMap<>();
			filters.put(Controller.NORMALIZED_STATION_NAME, monitoringLocationUpdate.getStationIx());
			List<MonitoringLocation> existingMonitoringLocations = dao.getByNormalizedName(filters);
			if (1 == existingMonitoringLocations.size()) {
				//it's OK if there's one existing record as long as it has the same siteNumber and site
				MonitoringLocation otherMl = existingMonitoringLocations.get(0);
				valid = sameId(monitoringLocationUpdate, otherMl);
			} else if (1 < existingMonitoringLocations.size()) {
				valid = false;
			}
			if (!valid) {
				String msg = buildDuplicateStationIxErrorMessage(monitoringLocationUpdate, existingMonitoringLocations);
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(msg).addPropertyNode(Controller.NORMALIZED_STATION_NAME).addConstraintViolation();
			}
		}
		return valid;
	}
	
	/**
	 * Side effects: adds constraint violations to `context`
	 * @param monitoringLocationCreation
	 * @param context
	 * @return true if valid, false if invalid
	 */
	protected boolean isValidNormalizedStationNameCreate(MonitoringLocation monitoringLocationCreation, ConstraintValidatorContext context) {
		boolean valid;
		String msg = "";
		if(null == monitoringLocationCreation.getStationIx()) {
			//creations must define a stationIx
			valid = false;
			msg = "The Monitoring Location did not define a required field: the normalized station name (stationIx)";
		} else {
			Map<String, Object> filters = new HashMap<>();
			filters.put(Controller.NORMALIZED_STATION_NAME, monitoringLocationCreation.getStationIx());
			List<MonitoringLocation> existingMonitoringLocations = dao.getByNormalizedName(filters);
			valid = existingMonitoringLocations.isEmpty();
			if (!valid) {
				msg = buildDuplicateStationIxErrorMessage(monitoringLocationCreation, existingMonitoringLocations);
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(msg).addPropertyNode(Controller.NORMALIZED_STATION_NAME).addConstraintViolation();
			}
		}
		if (!valid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(msg).addPropertyNode(Controller.NORMALIZED_STATION_NAME).addConstraintViolation();
		}
		
		return valid;
	}
	
	/**
	 * 
	 * @param ml
	 * @return true if it's a create (as opposed to an update)
	 */
	protected boolean isCreate(MonitoringLocation ml) {
		boolean isCreate = null == ml.getId();
		return isCreate;
	}
	/**
	 * 
	 * @param ml
	 * @return true if it's an update (as opposed to a create)
	 */	
	protected boolean isUpdate(MonitoringLocation ml) {
		boolean isUpdate = !isCreate(ml);
		return isUpdate;
	}
	
	/**
	 * 
	 * @param ml1
	 * @param ml2
	 * @return true if they have the same Id, false otherwise
	 */
	protected boolean sameId(MonitoringLocation ml1, MonitoringLocation ml2) {
		//It's better to use .compareTo() than .equals() for BigDecimals
		boolean same = 0 == ml1.getId().compareTo(ml2.getId());
		return same;
	}
	
	/**
	 * 
	 * @param mls
	 * @return 
	 */
	protected String serializeMls(Collection<MonitoringLocation> mls) {
		List<String> mlMsgs = new ArrayList<>();
		for (MonitoringLocation ml : mls) {
			String existingMlMsg;
			try {
				existingMlMsg = objectMapper.writeValueAsString(ml);
			} catch (JsonProcessingException ex) {
				existingMlMsg = "{\"msg\":\"error serializing monitoring location\"}";
				LOG.warn("Problem serializing monitoring location", ex);
			}
			mlMsgs.add(existingMlMsg);
		}
		String msg = "[" + String.join(",", mlMsgs) + "]";
		return msg;
	}
	
	/**
	 * 
	 * @param ml
	 * @param existingMls
	 * @return human-facing error message
	 */
	
	protected String buildDuplicateStationIxErrorMessage(MonitoringLocation ml, Collection<MonitoringLocation> existingMls) {
		String msg = "The supplied monitoring location had a duplicate normalized station name (stationIx): '" +
			ml.getStationIx() + "'.\n"; 
		msg += "The following " + existingMls.size() + " monitoring location(s) had the same normalized station name: ";
		msg += serializeMls(existingMls);
		return msg;
	}
}