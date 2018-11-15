package gov.usgs.wma.mlrlegacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueKeyValidatorForMonitoringLocation implements ConstraintValidator<UniqueKey, MonitoringLocation> {
	private static transient final Logger LOG = LoggerFactory.getLogger(UniqueKeyValidatorForMonitoringLocation.class);
	
	private final UniqueSiteIdAndAgencyCodeValidator uniqueSiteIdAndAgencyCodeValidator;
	
	private final UniqueNormalizedStationNameValidator uniqueNormalizedStationNameValidator;
	
	@Autowired
	public UniqueKeyValidatorForMonitoringLocation(
		UniqueSiteIdAndAgencyCodeValidator uniqueSiteIdAndAgencyCodeValidator,
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