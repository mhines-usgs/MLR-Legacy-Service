package gov.usgs.wma.mlrlegacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 
 */
public abstract class BaseUniqueMonitoringLocationValidator {

	private static transient final Logger LOG = LoggerFactory.getLogger(BaseUniqueMonitoringLocationValidator.class);
	protected static final String TRANSACTION_TYPE = "transactionType";

	protected final MonitoringLocationDao dao;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public BaseUniqueMonitoringLocationValidator(MonitoringLocationDao dao) {
		this.dao = dao;
	}
	
	public abstract boolean isValid(MonitoringLocation newOrUpdatedMonitoringLocation, ConstraintValidatorContext context);
		
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
				existingMlMsg = getObjectMapper().writeValueAsString(ml);
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
	 * @return the objectMapper
	 */
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	/**
	 * @param objectMapper the objectMapper to set
	 */
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
}
