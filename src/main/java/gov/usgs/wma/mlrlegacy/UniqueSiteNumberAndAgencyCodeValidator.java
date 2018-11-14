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
 * No two monitoring locations should share the same site id and agency code
 */
@Component
public class UniqueSiteNumberAndAgencyCodeValidator extends BaseMonitoringLocationValidator {
	
	private static final transient Logger LOG = LoggerFactory.getLogger(UniqueSiteNumberAndAgencyCodeValidator.class);
	
	@Autowired
	public UniqueSiteNumberAndAgencyCodeValidator(MonitoringLocationDao mlDao, ObjectMapper objectMapper) {
		super(mlDao, objectMapper);
	}
	
	/**
	 * 
	 * @param monitoringLocationUpdate
	 * @return
	 */
	@Override
	protected ValidationResult validateUpdate(MonitoringLocation monitoringLocationUpdate) {
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
	@Override
	protected ValidationResult validateCreate(MonitoringLocation monitoringLocationCreation) {
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
	 * @param existingMls
	 * @return human-facing error message
	 */
	protected String buildDuplicateStationIxErrorMessage(MonitoringLocation ml, Collection<MonitoringLocation> existingMls) {
		String msg = "The supplied monitoring location had a duplicate normalized station name (stationIx): '" +
			ml.getStationIx() + "'.\n"; 
		msg += "The following " + existingMls.size() + " monitoring location(s) had the same normalized station name:\n";
		
		msg += serializeMls(existingMls);
		return msg;
	}
}
