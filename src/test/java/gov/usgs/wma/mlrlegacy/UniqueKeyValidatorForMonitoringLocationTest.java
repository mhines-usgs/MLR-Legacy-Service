package gov.usgs.wma.mlrlegacy;

import javax.validation.ConstraintValidatorContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class UniqueKeyValidatorForMonitoringLocationTest {
	private UniqueSiteNumberAndAgencyCodeValidator uniqueSiteIdAndAgencyCodeValidator;
	private UniqueNormalizedStationNameValidator uniqueNormalizedStationNameValidator;
	private UniqueKeyValidatorForMonitoringLocation instance;

	@Before
	public void setUp() {
		uniqueSiteIdAndAgencyCodeValidator = mock(UniqueSiteNumberAndAgencyCodeValidator.class);
		uniqueNormalizedStationNameValidator = mock(UniqueNormalizedStationNameValidator.class);
		instance = new UniqueKeyValidatorForMonitoringLocation(uniqueSiteIdAndAgencyCodeValidator, uniqueNormalizedStationNameValidator);
	}

	@Test
	public void testNullMonitoringLocation() {
		MonitoringLocation newOrUpdatedMonitoringLocation = null;
		ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
		boolean result = instance.isValid(newOrUpdatedMonitoringLocation, context);
		assertTrue(result);
	}

	@Test
	public void testNullContext() {
		MonitoringLocation newOrUpdatedMonitoringLocation = mock(MonitoringLocation.class);
		ConstraintValidatorContext context = null;
		boolean result = instance.isValid(newOrUpdatedMonitoringLocation, context);
		assertTrue(result);
	}

	@Test
	public void testNullContextAndMonitoringLocation() {
		MonitoringLocation newOrUpdatedMonitoringLocation = null;
		ConstraintValidatorContext context = null;
		boolean result = instance.isValid(newOrUpdatedMonitoringLocation, context);
		assertTrue(result);
	}

	@Test
	public void testDuplicateStationIdOrAgencyCode() {
		MonitoringLocation newOrUpdatedMonitoringLocation = mock(MonitoringLocation.class);
		ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
		when(uniqueSiteIdAndAgencyCodeValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(false);
		when(uniqueNormalizedStationNameValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(true);
		boolean result = instance.isValid(newOrUpdatedMonitoringLocation, context);
		assertFalse(result);
	}

	@Test
	public void testDuplicateNormalizedName() {
		MonitoringLocation newOrUpdatedMonitoringLocation = mock(MonitoringLocation.class);
		ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
		when(uniqueSiteIdAndAgencyCodeValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(true);
		when(uniqueNormalizedStationNameValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(false);
		boolean result = instance.isValid(newOrUpdatedMonitoringLocation, context);
		assertFalse(result);
	}

	@Test
	public void testDuplicateNormalizedNameAndStationIdOrAgencyCode() {
		MonitoringLocation newOrUpdatedMonitoringLocation = mock(MonitoringLocation.class);
		ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
		when(uniqueSiteIdAndAgencyCodeValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(false);
		when(uniqueNormalizedStationNameValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(false);
		boolean result = instance.isValid(newOrUpdatedMonitoringLocation, context);
		assertFalse(result);
	}

	@Test
	public void testHappyPath() {
		MonitoringLocation newOrUpdatedMonitoringLocation = mock(MonitoringLocation.class);
		ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
		when(uniqueSiteIdAndAgencyCodeValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(true);
		when(uniqueNormalizedStationNameValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(true);
		boolean result = instance.isValid(newOrUpdatedMonitoringLocation, context);
		assertTrue(result);
	}
}
