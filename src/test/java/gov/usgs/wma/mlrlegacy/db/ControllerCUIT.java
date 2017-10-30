package gov.usgs.wma.mlrlegacy.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.jayway.jsonpath.JsonPath;

public class ControllerCUIT extends BaseControllerIT {

	@Test
	@DatabaseSetup("classpath:/testData/emptyDatabase/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_SECOND,
			value="classpath:/testResult/oneSparseResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={KeyModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void createSparseMonitoringLocation() throws Exception {
		agency = UPDATED_AGENCY_CODE;
		siteNbr = UPDATED_SITE_NUMBER;
		HttpEntity<String> entity = new HttpEntity<String>(getInputJson("sparseMonitoringLocation.json"), getHeaders());

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/monitoringLocations", entity, String.class);

		String responseBody = responseEntity.getBody();
		id = JsonPath.read(responseBody, "$.id").toString();

		String expectedBody = getExpectedCreateJson(responseBody, "sparseMonitoringLocation.json");

		assertEquals(201, responseEntity.getStatusCodeValue());
		JSONAssert.assertEquals(expectedBody, responseBody, JSONCompareMode.STRICT);
	}

	@Test
	@DatabaseSetup("classpath:/testData/emptyDatabase/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_SECOND,
			value="classpath:/testResult/oneResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={KeyModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void createFullMonitoringLocation() throws Exception {
		agency = UPDATED_AGENCY_CODE;
		siteNbr = UPDATED_SITE_NUMBER;
		HttpEntity<String> entity = new HttpEntity<String>(getInputJson("fullMonitoringLocation.json"), getHeaders());

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/monitoringLocations", entity, String.class);

		String responseBody = responseEntity.getBody();
		id = JsonPath.read(responseBody, "$.id").toString();

		String expectedBody = getExpectedCreateJson(responseBody, "fullMonitoringLocation.json");

		assertEquals(201, responseEntity.getStatusCodeValue());
		JSONAssert.assertEquals(expectedBody, responseBody, JSONCompareMode.STRICT);
	}

	@Test
	@DatabaseSetup("classpath:/testData/setupOne/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_SECOND,
			value="classpath:/testResult/oneSparseResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={KeyModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void updateSparseMonitoringLocation() throws Exception {
		id = String.valueOf(ONE_MILLION);
		agency = UPDATED_AGENCY_CODE;
		siteNbr = UPDATED_SITE_NUMBER;
		HttpEntity<String> entity = new HttpEntity<String>(getInputJson("sparseMonitoringLocation.json"), getHeaders());

		ResponseEntity<String> responseEntity = restTemplate.exchange("/monitoringLocations/" + id, HttpMethod.PUT, entity, String.class);

		String responseBody = responseEntity.getBody();
		String expectedBody = getExpectedUpdateJson(responseBody, "sparseMonitoringLocation.json");

		assertEquals(200, responseEntity.getStatusCodeValue());
		JSONAssert.assertEquals(expectedBody, responseBody, JSONCompareMode.STRICT);
	}

	@Test
	@DatabaseSetup("classpath:/testData/setupOne/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_SECOND,
			value="classpath:/testResult/oneResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={KeyModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void updateFullMonitoringLocation() throws Exception {
		id = String.valueOf(ONE_MILLION);
		agency = UPDATED_AGENCY_CODE;
		siteNbr = UPDATED_SITE_NUMBER;
		HttpEntity<String> entity = new HttpEntity<String>(getInputJson("fullMonitoringLocation.json"), getHeaders());

		ResponseEntity<String> responseEntity = restTemplate.exchange("/monitoringLocations/" + id, HttpMethod.PUT, entity, String.class);

		String responseBody = responseEntity.getBody();
		String expectedBody = getExpectedUpdateJson(responseBody, "fullMonitoringLocation.json");

		assertEquals(200, responseEntity.getStatusCodeValue());
		JSONAssert.assertEquals(expectedBody, responseBody, JSONCompareMode.STRICT);
	}

	@Test
	@DatabaseSetup("classpath:/testData/setupOne/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_SECOND,
			value="classpath:/testResult/oneSparsePatchDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={KeyModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void updateMonitoringLocation_notFound() throws Exception {
		id = String.valueOf(ONE_MILLION);
		HttpEntity<String> entity = new HttpEntity<String>(getInputJson("fullMonitoringLocation.json"), getHeaders());

		ResponseEntity<String> responseEntity = restTemplate.exchange("/monitoringLocations/1", HttpMethod.PUT, entity, String.class);

		assertNull(responseEntity.getBody());
		assertEquals(404, responseEntity.getStatusCodeValue());
	}

	@Test
	@DatabaseSetup("classpath:/testData/setupOne/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_SECOND,
			value="classpath:/testResult/oneSparsePatchDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={KeyModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void patchSparseMonitoringLocation() throws Exception {
		id = String.valueOf(ONE_MILLION);
		HttpEntity<String> entity = new HttpEntity<String>(getInputJson("sparseMonitoringLocation.json"), getHeaders());

		ResponseEntity<String> responseEntity = restTemplate.exchange("/monitoringLocations?_method=patch", HttpMethod.POST, entity, String.class);

		String responseBody = responseEntity.getBody();
		String expectedBody = getExpectedUpdateJson(responseBody, "oneMillion.json");

		assertEquals(200, responseEntity.getStatusCodeValue());
		JSONAssert.assertEquals(expectedBody, responseBody, JSONCompareMode.STRICT);
	}

	@Test
	@DatabaseSetup("classpath:/testData/setupOne/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_SECOND,
			value="classpath:/testResult/oneFullPatchDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={KeyModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	@WithMockUser(username="Known", roles="WIWSC_DBA")
	public void patchFullMonitoringLocation() throws Exception {
		id = String.valueOf(ONE_MILLION);
		HttpEntity<String> entity = new HttpEntity<String>(getInputJson("fullMonitoringLocation.json"), getHeaders());

		ResponseEntity<String> responseEntity = restTemplate.exchange("/monitoringLocations?_method=patch", HttpMethod.POST, entity, String.class);

		String responseBody = responseEntity.getBody();
		String expectedBody = getExpectedUpdateJson(responseBody, "fullMonitoringLocation.json");

		assertEquals(200, responseEntity.getStatusCodeValue());
		JSONAssert.assertEquals(expectedBody, responseBody, JSONCompareMode.STRICT);
	}

	@Test
	@DatabaseSetup("classpath:/testData/setupOne/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_SECOND,
			value="classpath:/testResult/onePatchBlanksResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={KeyModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void patchFullBlanksMonitoringLocation() throws Exception {
		id = String.valueOf(ONE_MILLION);
		HttpEntity<String> entity = new HttpEntity<String>(getInputJson("patchFullBlankMonitoringLocation.json"), getHeaders());

		ResponseEntity<String> responseEntity = restTemplate.exchange("/monitoringLocations?_method=patch", HttpMethod.POST, entity, String.class);

		String responseBody = responseEntity.getBody();
		String expectedBody = getExpectedUpdateJson(responseBody, "patchBlanksMonitoringLocation.json");

		assertEquals(200, responseEntity.getStatusCodeValue());
		JSONAssert.assertEquals(expectedBody, responseBody, JSONCompareMode.STRICT);
	}

	@Test
	@DatabaseSetup("classpath:/testData/setupOne/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_SECOND,
			value="classpath:/testResult/oneSparseResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={KeyModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void patchFullNullMonitoringLocation() throws Exception {
		id = String.valueOf(ONE_MILLION);
		HttpEntity<String> entity = new HttpEntity<String>(getInputJson("patchFullNullMonitoringLocation.json"), getHeaders());

		ResponseEntity<String> responseEntity = restTemplate.exchange("/monitoringLocations?_method=patch", HttpMethod.POST, entity, String.class);

		String responseBody = responseEntity.getBody();
		String expectedBody = getExpectedUpdateJson(responseBody, "sparseMonitoringLocation.json");

		assertEquals(200, responseEntity.getStatusCodeValue());
		JSONAssert.assertEquals(expectedBody, responseBody, JSONCompareMode.STRICT);
	}

	@Test
	@DatabaseSetup("classpath:/testData/setupOne/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_SECOND,
			value="classpath:/testResult/oneSparsePatchDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={KeyModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void patchMonitoringLocation_notFound() throws Exception {
		id = String.valueOf(ONE_MILLION);
		HttpEntity<String> entity = new HttpEntity<String>("{\"agencyCode\": \"abc\", \"siteNumber\": \"123\"}", getHeaders());

		ResponseEntity<String> responseEntity = restTemplate.exchange("/monitoringLocations?_method=patch", HttpMethod.POST, entity, String.class);
		assertNull(responseEntity.getBody());
		assertEquals(404, responseEntity.getStatusCodeValue());
	}

}
