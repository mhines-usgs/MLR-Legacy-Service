package gov.usgs.wma.mlrlegacy;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 *
 * 
 */
@Component
public class UniqueNormalizedStationNameValidator implements Validator<MonitoringLocation> {
	
	private static final transient Logger LOG = LoggerFactory.getLogger(UniqueNormalizedStationNameValidator.class);
	private final MonitoringLocationDao mlDao;
	
	@Autowired
	public UniqueNormalizedStationNameValidator(MonitoringLocationDao mlDao) {
		this.mlDao = mlDao;
	}
	
	/**
	 * No two monitoring locations should share the same normalized station 
	 * name. (stationIx)
	 * @param newOrUpdatedMonitoringLocation
	 * @return 
	 */
	@Override
	public ValidationResult validate(MonitoringLocation newOrUpdatedMonitoringLocation) {
		ValidationResult result;
		if (isCreate(newOrUpdatedMonitoringLocation)) {
			result = validateNormalizedStationNameCreate(newOrUpdatedMonitoringLocation);
		} else {
			result = validateNormalizedStationNameUpdate(newOrUpdatedMonitoringLocation);
		}
	
		return result;
		
	}
	
	/**
	 * 
	 * @param monitoringLocationUpdate
	 * @return
	 */
	protected ValidationResult validateNormalizedStationNameUpdate(MonitoringLocation monitoringLocationUpdate) {
		boolean valid = true;
		List<String> msgs = new ArrayList<>();
		//it's OK if the stationIx isn't populated because it could be provided in the database
		if (null != monitoringLocationUpdate.getStationIx()) {
			Map<String, Object> filters = new HashMap<>();
			filters.put(Controller.NORMALIZED_STATION_NAME, monitoringLocationUpdate.getStationIx());
			List<MonitoringLocation> existingMonitoringLocations = mlDao.getByNormalizedName(filters);
			if (1 == existingMonitoringLocations.size()) {
				//it's OK if there's one existing record as long as it has the same siteNumber and site
				MonitoringLocation otherMl = existingMonitoringLocations.get(0);
				valid = sameId(monitoringLocationUpdate, otherMl);
			} else if (1 < existingMonitoringLocations.size()) {
				valid = false;
			}
			if (!valid) {
				String msg = buildDuplicateStationIxErrorMessage(monitoringLocationUpdate, existingMonitoringLocations);
				msgs.add(msg);
			}
		}
		ValidationResult result = new ValidationResultImpl(valid, msgs);
		return result;
	}
	
	/**
	 * @param monitoringLocationCreation
	 * @return
	 */
	protected ValidationResult validateNormalizedStationNameCreate(MonitoringLocation monitoringLocationCreation) {
		boolean valid;
		List<String> msgs = new ArrayList<>();

		if(null == monitoringLocationCreation.getStationIx()) {
			//creations must define a stationIx
			valid = false;
		} else {
			Map<String, Object> filters = new HashMap<>();
			filters.put(Controller.NORMALIZED_STATION_NAME, monitoringLocationCreation.getStationIx());
			List<MonitoringLocation> existingMonitoringLocations = mlDao.getByNormalizedName(filters);
			valid = existingMonitoringLocations.isEmpty();
			if (!valid) {
				String msg = buildDuplicateStationIxErrorMessage(monitoringLocationCreation, existingMonitoringLocations);
				msgs.add(msg);
			}
		}
		ValidationResult result = new ValidationResultImpl(valid, msgs);
		return result;
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
	 * @param ml
	 * @param existingMls
	 * @return human-facing error message
	 */
	
	protected String buildDuplicateStationIxErrorMessage(MonitoringLocation ml, Collection<MonitoringLocation> existingMls) {
		String msg = "The supplied monitoring location had a duplicate normalized station name (stationIx): '" +
			ml.getStationIx() + "'.\n"; 
		msg += "The following " + existingMls.size() + " monitoring location(s) had the same normalized station name: ";
		ObjectMapper objectMapper = new ObjectMapper();
		
		List<String> existingMlMsgs = new ArrayList<>();
		for (MonitoringLocation existingMl : existingMls) {
			String existingMlMsg;
			try{
				existingMlMsg = objectMapper.writeValueAsString(existingMl);
			} catch (JsonProcessingException ex) {
				existingMlMsg = "{\"msg\":\"error serializing monitoring location\"}";
				LOG.warn("Problem serializing monitoring location", ex);
			}
			existingMlMsgs.add(existingMlMsg);
		}
		msg += String.join(",", existingMlMsgs);
		return msg;
	}

}
