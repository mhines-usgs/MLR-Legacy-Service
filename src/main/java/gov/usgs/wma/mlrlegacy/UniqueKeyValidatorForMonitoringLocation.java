package gov.usgs.wma.mlrlegacy;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueKeyValidatorForMonitoringLocation implements ConstraintValidator<UniqueKey, MonitoringLocation> {
	private static transient final Logger LOG = LoggerFactory.getLogger(UniqueKeyValidatorForMonitoringLocation.class);
	
	private final UniqueSiteNumberAndAgencyCodeValidator uniqueSiteIdAndAgencyCodeValidator;
	
	private final UniqueNormalizedStationNameValidator uniqueNormalizedStationNameValidator;
	
	@Autowired
	public UniqueKeyValidatorForMonitoringLocation(
		UniqueSiteNumberAndAgencyCodeValidator uniqueSiteIdAndAgencyCodeValidator,
		UniqueNormalizedStationNameValidator uniqueNormalizedStationNameValidator
	) {
		this.uniqueSiteIdAndAgencyCodeValidator = uniqueSiteIdAndAgencyCodeValidator;
		this.uniqueNormalizedStationNameValidator = uniqueNormalizedStationNameValidator;
	}
	
	@Override
	public void initialize(UniqueKey constraintAnnotation) {
		// Nothing for us to do here at this time.
	}

	@Override
	public boolean isValid(MonitoringLocation newOrUpdatedMonitoringLocation, ConstraintValidatorContext context) {
		boolean valid = true;
		if (null != newOrUpdatedMonitoringLocation && null != context) {
			valid = this.uniqueSiteIdAndAgencyCodeValidator.isValid(newOrUpdatedMonitoringLocation, context);
			valid &= this.uniqueNormalizedStationNameValidator.isValid(newOrUpdatedMonitoringLocation, context);
		}
		return valid;
	}

}