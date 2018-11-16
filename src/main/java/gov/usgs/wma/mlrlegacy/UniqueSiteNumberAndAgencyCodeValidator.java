package gov.usgs.wma.mlrlegacy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * No two monitoring locations should share the same Agency Code and Site Number.
 * 
 */
@Component
public class UniqueSiteNumberAndAgencyCodeValidator extends BaseUniqueMonitoringLocationValidator{

	
	public UniqueSiteNumberAndAgencyCodeValidator(@Autowired MonitoringLocationDao dao) {
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
		String msg = "";
		if (null != newOrUpdatedMonitoringLocation.getAgencyCode() && null != newOrUpdatedMonitoringLocation.getSiteNumber()) {
			Map<String, Object> filters = new HashMap<>();
			filters.put(Controller.AGENCY_CODE, newOrUpdatedMonitoringLocation.getAgencyCode());
			filters.put(Controller.SITE_NUMBER, newOrUpdatedMonitoringLocation.getSiteNumber());
			MonitoringLocation existingMonitoringLocation = dao.getByAK(filters);
			if (existingMonitoringLocation != null) {
				if(isPatch(newOrUpdatedMonitoringLocation)) {
					valid = true;
				} else if (isCreate(newOrUpdatedMonitoringLocation)){
					valid = false;
					msg = "An existing Monitoring Location with the same Agency Code and Site Number was found:\n";
					msg += serializeMls(Arrays.asList(existingMonitoringLocation));
				} else if (isUpdate(newOrUpdatedMonitoringLocation)){
					if(isValidUpdate(newOrUpdatedMonitoringLocation, existingMonitoringLocation)){
						valid = true;
					} else {
						valid = false;
						msg = String.format(
							"The proposed Monitoring Location Id (%s) was different from the Id of an existing Monitoring Location (%s)"
							+ " even though both have the same Agency Code (%s) and Site Number(%s)."
							+ "The Ids should match if the Agency Code and Site Number are the same.",
							newOrUpdatedMonitoringLocation.getId().toString(),
							existingMonitoringLocation.getId().toString(),
							existingMonitoringLocation.getAgencyCode(),
							existingMonitoringLocation.getSiteNumber()
						);
						msg += serializeMls(Arrays.asList(existingMonitoringLocation));
					}
				} else {
					valid = false;
					msg = "Unable to classify parameterized Monitoring Location as a Create, Update, or Patch.";
				}
			}
		}
		if(!valid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(msg)
				.addPropertyNode(Controller.SITE_NUMBER)
				.addPropertyNode(Controller.AGENCY_CODE)
				.addConstraintViolation();
		}
		return valid;
	}
	
	protected boolean isValidUpdate(MonitoringLocation newOrUpdatedMonitoringLocation, MonitoringLocation existingMl) {
		boolean validUpdate = isUpdate(newOrUpdatedMonitoringLocation) 
			&&
			Objects.equals(newOrUpdatedMonitoringLocation.getId(), existingMl.getId());
		return validUpdate;
	}

}
