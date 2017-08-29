package gov.usgs.wma.mlrlegacy.db;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.jayway.jsonpath.JsonPath;

public class ControllerCUIT extends BaseControllerIT {

	@Test
	@DatabaseSetup("classpath:/testData/emptyDatabase/")
	@ExpectedDatabase(
			table="legacy_location",
			value="classpath:/testResult/oneResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers=IdModifier.class
			)
	public void createMonitoringLocation() throws Exception {
		HttpEntity<String> entity = new HttpEntity<String>(getCompareFile("testData/", "monitoringLocation.json").replace("\"[id]\"", "1"), getHeaders());

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/monitoringLocations", entity, String.class);

		String responseBody = responseEntity.getBody();
		id = JsonPath.read(responseBody, "$.id").toString();
		String expectedBody = getCompareFile("testData/", "monitoringLocation.json").replace("\"[id]\"", id);

		assertEquals(201, responseEntity.getStatusCodeValue());
		JSONAssert.assertEquals(expectedBody, responseBody, JSONCompareMode.STRICT);
	}

	@Test
	@DatabaseSetup("classpath:/testData/setupOne/")
	@ExpectedDatabase(
			table="legacy_location",
			value="classpath:/testResult/oneResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers=IdModifier.class
			)
	public void updateMonitoringLocation() throws Exception {
		id = String.valueOf(ONE_MILLION);
		HttpEntity<String> entity = new HttpEntity<String>(getCompareFile("testData/", "monitoringLocation.json").replace("\"[id]\"", "1"), getHeaders());

		ResponseEntity<String> responseEntity = restTemplate.exchange("/monitoringLocations/" + id, HttpMethod.PUT, entity, String.class);

		String responseBody = responseEntity.getBody();
		String expectedBody = getCompareFile("testData/", "monitoringLocation.json").replace("\"[id]\"", id);

		assertEquals(200, responseEntity.getStatusCodeValue());
		JSONAssert.assertEquals(expectedBody, responseBody, JSONCompareMode.STRICT);
	}

}
