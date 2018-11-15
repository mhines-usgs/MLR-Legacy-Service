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
import org.mockito.Mock;

/**
 *
 */
public class UniqueKeyValidatorForMonitoringLocationTest {

	private UniqueKeyValidatorForMonitoringLocation instance;
	
	@Mock
	private MonitoringLocationDao mockDao;
	
	@Mock
	private ObjectMapper mockObjectMapper;
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
		instance = new UniqueKeyValidatorForMonitoringLocation(mockDao, mockObjectMapper);
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of isValid method, of class UniqueKeyValidatorForMonitoringLocation.
	 */
	@Test
	public void testIsValid() {
		System.out.println("isValid");
		MonitoringLocation newOrUpdatedMonitoringLocation = null;
		ConstraintValidatorContext context = null;
		boolean expResult = false;
		boolean result = instance.isValid(newOrUpdatedMonitoringLocation, context);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isValidSiteNumberAndAgencyCode method, of class UniqueKeyValidatorForMonitoringLocation.
	 */
	@Test
	public void testIsValidSiteNumberAndAgencyCode() {
		System.out.println("isValidSiteNumberAndAgencyCode");
		MonitoringLocation newOrUpdatedMonitoringLocation = null;
		ConstraintValidatorContext context = null;
		boolean expResult = false;
		boolean result = instance.isValidSiteNumberAndAgencyCode(newOrUpdatedMonitoringLocation, context);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isValidNormalizedStationName method, of class UniqueKeyValidatorForMonitoringLocation.
	 */
	@Test
	public void testIsValidNormalizedStationName() {
		System.out.println("isValidNormalizedStationName");
		MonitoringLocation newOrUpdatedMonitoringLocation = null;
		ConstraintValidatorContext context = null;
		boolean expResult = false;
		boolean result = instance.isValidNormalizedStationName(newOrUpdatedMonitoringLocation, context);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isValidNormalizedStationNameUpdate method, of class UniqueKeyValidatorForMonitoringLocation.
	 */
	@Test
	public void testIsValidNormalizedStationNameUpdate() {
		System.out.println("isValidNormalizedStationNameUpdate");
		MonitoringLocation monitoringLocationUpdate = null;
		ConstraintValidatorContext context = null;
		boolean expResult = false;
		boolean result = instance.isValidNormalizedStationNameUpdate(monitoringLocationUpdate, context);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isValidNormalizedStationNameCreate method, of class UniqueKeyValidatorForMonitoringLocation.
	 */
	@Test
	public void testIsValidNormalizedStationNameCreate() {
		System.out.println("isValidNormalizedStationNameCreate");
		MonitoringLocation monitoringLocationCreation = null;
		ConstraintValidatorContext context = null;
		boolean expResult = false;
		boolean result = instance.isValidNormalizedStationNameCreate(monitoringLocationCreation, context);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isCreate method, of class UniqueKeyValidatorForMonitoringLocation.
	 */
	@Test
	public void testIsCreate() {
		System.out.println("isCreate");
		MonitoringLocation ml = null;
		boolean expResult = false;
		boolean result = instance.isCreate(ml);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isUpdate method, of class UniqueKeyValidatorForMonitoringLocation.
	 */
	@Test
	public void testIsUpdate() {
		System.out.println("isUpdate");
		MonitoringLocation ml = null;
		boolean expResult = false;
		boolean result = instance.isUpdate(ml);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of sameId method, of class UniqueKeyValidatorForMonitoringLocation.
	 */
	@Test
	public void testSameId() {
		System.out.println("sameId");
		MonitoringLocation ml1 = null;
		MonitoringLocation ml2 = null;
		boolean expResult = false;
		boolean result = instance.sameId(ml1, ml2);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of serializeMls method, of class UniqueKeyValidatorForMonitoringLocation.
	 */
	@Test
	public void testSerializeMls() {
		System.out.println("serializeMls");
		Collection<MonitoringLocation> mls = null;
		String expResult = "";
		String result = instance.serializeMls(mls);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of buildDuplicateStationIxErrorMessage method, of class UniqueKeyValidatorForMonitoringLocation.
	 */
	@Test
	public void testBuildDuplicateStationIxErrorMessage() {
		System.out.println("buildDuplicateStationIxErrorMessage");
		MonitoringLocation ml = null;
		Collection<MonitoringLocation> existingMls = null;
		String expResult = "";
		String result = instance.buildDuplicateStationIxErrorMessage(ml, existingMls);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
