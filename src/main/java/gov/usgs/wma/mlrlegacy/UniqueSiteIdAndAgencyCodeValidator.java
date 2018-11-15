package gov.usgs.wma.mlrlegacy;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * No two monitoring locations should share the same Agency Code and Site Number.
 * 
 */
@Component
public class UniqueSiteIdAndAgencyCodeValidator extends BaseUniqueMonitoringLocationValidator{

	
	public UniqueSiteIdAndAgencyCodeValidator(@Autowired MonitoringLocationDao dao) {
		super(dao);
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
	@Override
	public boolean isValid(MonitoringLocation newOrUpdatedMonitoringLocation, ConstraintValidatorContext context) {
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

}
