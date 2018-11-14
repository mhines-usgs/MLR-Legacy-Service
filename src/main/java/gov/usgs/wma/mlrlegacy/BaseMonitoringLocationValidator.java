package gov.usgs.wma.mlrlegacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 
 */
public abstract class BaseMonitoringLocationValidator implements Validator<MonitoringLocation>{

	protected final MonitoringLocationDao mlDao;
	protected final ObjectMapper objectMapper;
	private static final transient Logger LOG = LoggerFactory.getLogger(BaseMonitoringLocationValidator.class);

	public BaseMonitoringLocationValidator(MonitoringLocationDao mlDao, ObjectMapper objectMapper) {
		this.mlDao = mlDao;
		this.objectMapper = objectMapper;
	}
	/**
	 * Monitoring Locations should be unique according to a criteria implemented
	 * by subclasses
	 * @param newOrUpdatedMonitoringLocation
	 * @return 
	 */
	@Override
	public ValidationResult validate(MonitoringLocation newOrUpdatedMonitoringLocation) {
		ValidationResult result;
		if (isCreate(newOrUpdatedMonitoringLocation)) {
			result = validateCreate(newOrUpdatedMonitoringLocation);
		} else {
			result = validateUpdate(newOrUpdatedMonitoringLocation);
		}
	
		return result;
		
	}
	
	protected abstract ValidationResult validateCreate(MonitoringLocation monitoringLocationCreation);
	protected abstract ValidationResult validateUpdate(MonitoringLocation monitoringLocationCreation);
	
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
	 * Serialize Monitoring Locations as JSON array
	 * @param mls
	 * @return String containing JSON array of monitoring locations
	 */
	protected String serializeMls(Collection<MonitoringLocation> mls) {
		List<String> mlMsgs = new ArrayList<>();
		for (MonitoringLocation ml : mls) {
			String existingMlMsg;
			try{
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
}
