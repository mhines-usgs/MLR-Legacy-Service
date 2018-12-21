package gov.usgs.wma.mlrlegacy.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import java.io.IOException;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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
		
		String msgs = responseBody;
		assertTrue(msgs.equals("{}"));
	}

}
