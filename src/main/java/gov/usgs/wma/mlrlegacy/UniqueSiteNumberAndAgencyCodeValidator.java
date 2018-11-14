package gov.usgs.wma.mlrlegacy;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
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
	protected ValidationResult validateUpdate(MonitoringLocation mlUpdate) {
		boolean valid = true;
		List<String> msgs = new ArrayList<>();
		if (null != mlUpdate.getAgencyCode() && null != mlUpdate.getSiteNumber()) {
			Map<String, Object> filters = new HashMap<>();
			filters.put(Controller.AGENCY_CODE, mlUpdate.getAgencyCode());
			filters.put(Controller.SITE_NUMBER, mlUpdate.getSiteNumber());
			MonitoringLocation existingMonitoringLocation = mlDao.getByAK(filters);
			if (existingMonitoringLocation != null) {
				if (isCreate(mlUpdate) || sameId(existingMonitoringLocation, mlUpdate)) {
					valid = false;
					msgs.add(buildDuplicateSiteIdAndAgencyCodeMessage(mlUpdate, existingMonitoringLocation));
				}
			}
		}
		ValidationResultImpl result = new ValidationResultImpl(valid, msgs);
		return result;
	}
	
	/**
	 * @param monitoringLocationCreation
	 * @return
	 */
	@Override
	protected ValidationResult validateCreate(MonitoringLocation mlCreation) {
		boolean valid = true;
		List<String> msgs = new ArrayList<>();
		if (null != mlCreation.getAgencyCode() && null != mlCreation.getSiteNumber()) {
			Map<String, Object> filters = new HashMap<>();
			filters.put(Controller.AGENCY_CODE, mlCreation.getAgencyCode());
			filters.put(Controller.SITE_NUMBER, mlCreation.getSiteNumber());
			MonitoringLocation existingMonitoringLocation = mlDao.getByAK(filters);
			if (existingMonitoringLocation != null) {
				if (isCreate(mlCreation) || sameId(existingMonitoringLocation, mlCreation)) {
					valid = false;
					msgs.add(buildDuplicateSiteIdAndAgencyCodeMessage(mlCreation, existingMonitoringLocation));
				}
			}
		}
		ValidationResultImpl result = new ValidationResultImpl(valid, msgs);
		return result;
	}
	
	/**
	 * 
	 * @param ml
	 * @param existingMls
	 * @return human-facing error message
	 */
	protected String buildDuplicateSiteIdAndAgencyCodeMessage(MonitoringLocation ml, MonitoringLocation existingMl) {
		String msg = "The supplied monitoring location had a duplicate normalized station name (stationIx): '" +
			ml.getStationIx() + "'.\n"; 
		msg += "The following monitoring location had the same normalized station name:\n";
		
		msg += serializeMls(Arrays.asList(existingMl));
		return msg;
	}
}
