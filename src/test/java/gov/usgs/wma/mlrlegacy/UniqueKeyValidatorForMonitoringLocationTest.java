package gov.usgs.wma.mlrlegacy;

import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.spi.time.TimeProvider;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class UniqueKeyValidatorForMonitoringLocationTest {
	private UniqueSiteIdAndAgencyCodeValidator uniqueSiteIdAndAgencyCodeValidator;
	private UniqueNormalizedStationNameValidator uniqueNormalizedStationNameValidator;
	private UniqueKeyValidatorForMonitoringLocation instance;

	@Before
	public void setUp() {
		uniqueSiteIdAndAgencyCodeValidator = mock(UniqueSiteIdAndAgencyCodeValidator.class);
		uniqueNormalizedStationNameValidator = mock(UniqueNormalizedStationNameValidator.class);
		instance = new UniqueKeyValidatorForMonitoringLocation(uniqueSiteIdAndAgencyCodeValidator, uniqueNormalizedStationNameValidator);
	}

	private ConstraintValidatorContextImpl createEmptyConstraintValidatorContextImpl() {
		PathImpl path = PathImpl.createRootPath();
		path.addBeanNode();
		TimeProvider timeProvider = mock(TimeProvider.class);
		ConstraintValidatorContextImpl context = new ConstraintValidatorContextImpl(
			null, timeProvider, path, null
		);
		context.disableDefaultConstraintViolation();
		return context;
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
		when(uniqueSiteIdAndAgencyCodeValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(true);
		boolean result = instance.isValid(newOrUpdatedMonitoringLocation, context);
		assertFalse(result);
	}

	@Test
	public void testDuplicateNormalizedName() {
		MonitoringLocation newOrUpdatedMonitoringLocation = mock(MonitoringLocation.class);
		ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
		when(uniqueSiteIdAndAgencyCodeValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(true);
		when(uniqueSiteIdAndAgencyCodeValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(false);
		boolean result = instance.isValid(newOrUpdatedMonitoringLocation, context);
		assertFalse(result);
	}

	@Test
	public void testDuplicateNormalizedNameAndStationIdOrAgencyCode() {
		MonitoringLocation newOrUpdatedMonitoringLocation = mock(MonitoringLocation.class);
		ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
		when(uniqueSiteIdAndAgencyCodeValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(false);
		when(uniqueSiteIdAndAgencyCodeValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(false);
		boolean result = instance.isValid(newOrUpdatedMonitoringLocation, context);
		assertFalse(result);
	}

	@Test
	public void testHappyPath() {
		MonitoringLocation newOrUpdatedMonitoringLocation = mock(MonitoringLocation.class);
		ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
		when(uniqueSiteIdAndAgencyCodeValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(true);
		when(uniqueSiteIdAndAgencyCodeValidator.isValid(newOrUpdatedMonitoringLocation, context)).thenReturn(true);
		boolean result = instance.isValid(newOrUpdatedMonitoringLocation, context);
		assertFalse(result);
	}
}
