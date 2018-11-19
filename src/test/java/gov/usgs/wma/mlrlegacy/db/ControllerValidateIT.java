package gov.usgs.wma.mlrlegacy.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.jayway.jsonpath.JsonPath;
import static gov.usgs.wma.mlrlegacy.db.BaseControllerIT.getAuthorizedHeaders;
import static gov.usgs.wma.mlrlegacy.db.BaseIT.KNOWN_USER;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;

/**
 * Controller integration tests for Validation operations
 */
public class ControllerValidateIT extends BaseControllerIT {
	private static final String URL = "/monitoringLocations/validate";
	
	@DatabaseSetup("classpath:/testData/emptyDatabase/")
	@Test
	public void testGoodCreation() throws Exception {
		String json = getInputJson("createFullMonitoringLocation.json");
		HttpEntity<String> entity = new HttpEntity<>(json, getUnauthorizedHeaders());

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, entity, String.class);
		
		String responseBody = responseEntity.getBody();
		assertEquals(responseBody, 200, responseEntity.getStatusCodeValue());

		String[] msgs = (new ObjectMapper()).readValue(responseBody, String[].class);
		assertEquals(0, msgs.length);
	}
	
	@DatabaseSetup("classpath:/testData/setupOne/")
	@Test
	public void testCreateExisting() throws IOException {
		HttpEntity<String> entity = new HttpEntity<>(getInputJson("createFullMonitoringLocation.json"), getUnauthorizedHeaders());

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, entity, String.class);

		String responseBody = responseEntity.getBody();
		assertEquals(responseBody, 406, responseEntity.getStatusCodeValue());
		
		String[] msgs = (new ObjectMapper()).readValue(responseBody, String[].class);
		assertNotEquals(0, msgs.length);
	}
	
	@DatabaseSetup("classpath:/testData/setupOne/")
	@Test
	public void testPatchExistingLocation() throws IOException {
		id = "1000000";
		HttpEntity<String> entity = new HttpEntity<>(getInputJson("patchFullBlankMonitoringLocation.json"), getUnauthorizedHeaders());

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, entity, String.class);

		String responseBody = responseEntity.getBody();
		assertEquals(responseBody, 200, responseEntity.getStatusCodeValue());
		
		String[] msgs = (new ObjectMapper()).readValue(responseBody, String[].class);
		assertEquals(0, msgs.length);
	}

}
