package gov.usgs.wma.mlrlegacy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueKeyValidatorForMonitoringLocation implements ConstraintValidator<UniqueKey, MonitoringLocation> {
	
	@Autowired
	private MonitoringLocationDao dao;

	@Override
	public void initialize(UniqueKey constraintAnnotation) {
		// Nothing for us to do here at this time.
	}

	@Override
	public boolean isValid(MonitoringLocation value, ConstraintValidatorContext context) {
		boolean valid = true;

		if (null != value && null != context) {
			if (null != value.getAgencyCode() && null != value.getSiteNumber()) {
				Map<String, Object> filters = new HashMap<String,Object>();
				filters.put(Controller.AGENCY_CODE, value.getAgencyCode());
				filters.put(Controller.SITE_NUMBER, value.getSiteNumber());
				List<MonitoringLocation> monitoringLocations = dao.getByMap(filters);
				for (MonitoringLocation monitoringLocation : monitoringLocations) {
					if (null == value.getId() || 0 != monitoringLocation.getId().compareTo(value.getId())) {
						valid = false;
						context.disableDefaultConstraintViolation();
						context.buildConstraintViolationWithTemplate("Duplicate Agency Code and Site Number found in MLR.").addPropertyNode(Controller.SITE_NUMBER).addConstraintViolation();
					}
				}
			}
		}

		return valid;
	}
}