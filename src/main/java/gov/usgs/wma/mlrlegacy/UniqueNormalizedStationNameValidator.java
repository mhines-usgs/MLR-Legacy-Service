package gov.usgs.wma.mlrlegacy;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * No two monitoring locations should share the same normalized station 
 * name. (stationIx)
 * 
 */
@Component
public class UniqueNormalizedStationNameValidator extends BaseUniqueMonitoringLocationValidator {

	
	public UniqueNormalizedStationNameValidator(@Autowired MonitoringLocationDao dao) {
		super(dao);
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
	@Override
	public boolean isValid(MonitoringLocation newOrUpdatedMonitoringLocation, ConstraintValidatorContext context) {
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
