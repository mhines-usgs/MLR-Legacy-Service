package gov.usgs.wma.mlrlegacy.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import gov.usgs.wma.mlrlegacy.Application;
import gov.usgs.wma.mlrlegacy.MethodSecurityConfig;
import gov.usgs.wma.mlrlegacy.OAuth2ResourceServerConfig;

@SpringBootTest(
		classes={DBTestConfig.class, Application.class,
				OAuth2ResourceServerConfig.class,
				MethodSecurityConfig.class},
		webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties={"maintenanceRoles=ROLE_DBA_55",
				"security.oauth2.resource.jwt.keyValue=secret",
				"oauthResourceTokenKeyUri=",
				"security.require-ssl=false",
				"server.ssl.enabled=false"}
	)
public abstract class BaseControllerIT extends BaseIT {

	@Autowired
	protected TestRestTemplate restTemplate;

	@Value("${security.oauth2.resource.jwt.keyValue}")
	private String secret;

	public static String createToken(String username, String email, String ... roles) throws Exception {
		String jwt = JWT.create()
				.withClaim("user_name", username)
				.withArrayClaim("authorities", roles)
				.withClaim("email", email)
				.sign(Algorithm.HMAC256("secret"))
				;
		return jwt;
	}

	public static HttpHeaders getAuthorizedHeaders() {
		return getHeaders(KNOWN_USER, "known@usgs.gov", "ROLE_DBA_55");
	}

	public static HttpHeaders getUnauthorizedHeaders() {
		return getHeaders(KNOWN_USER, "known@usgs.gov", "ROLE_UNKNOWN");
	}

	public static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	public static HttpHeaders getHeaders(String username, String email, String ... roles) {
		HttpHeaders headers = getHeaders();
		try {
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + createToken(username, email, roles));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return headers;
	}

}
