package gov.usgs.wma.mlrlegacy;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import javax.validation.ConstraintValidatorContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import static org.mockito.Mockito.mock;

/**
 *
 */
public class UniqueNormalizedStationNameValidatorTest {
	private MonitoringLocationDao dao;
	private UniqueNormalizedStationNameValidator instance;
	
	@Before
	public void setUp() {
		//most tests want a real object mapper
		//...and a mocked DAO
		dao = mock(MonitoringLocationDao.class);
		instance = new UniqueNormalizedStationNameValidator(dao);
	}
	
	/**
	 * Test of isValid method, of class UniqueNormalizedStationNameValidator.
	 */
	@Ignore
	@Test
	public void testIsValid() {
//		MonitoringLocation ml = new MonitoringLocation();
//		ConstraintValidatorContext context = null;
//		UniqueNormalizedStationNameValidator instance = null;
//		boolean expResult = false;
////		boolean result = instance.isValid(newOrUpdatedMonitoringLocation, context);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}
}
