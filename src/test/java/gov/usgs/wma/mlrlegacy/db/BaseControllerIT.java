package gov.usgs.wma.mlrlegacy.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import gov.usgs.wma.mlrlegacy.Application;

@SpringBootTest(classes={DBTestConfig.class, Application.class}, webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseControllerIT extends BaseIT {

	@Autowired
	protected TestRestTemplate restTemplate;

	public static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

}
