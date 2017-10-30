package gov.usgs.wma.mlrlegacy.db;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;

import gov.usgs.wma.mlrlegacy.Application;
import gov.usgs.wma.mlrlegacy.MethodSecurityConfig;
import gov.usgs.wma.mlrlegacy.OAuth2ResourceServerConfig;

@SpringBootTest(
		classes={DBTestConfig.class, Application.class, OAuth2ResourceServerConfig.class, MethodSecurityConfig.class},
		webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT
	)
public abstract class BaseControllerIT extends BaseIT {

	@Autowired
	protected TestRestTemplate restTemplate;

	@Autowired
	protected TokenStore tokenStore;

	@Before
	public void setUp() {
		final OAuth2AccessToken token = new DefaultOAuth2AccessToken("FOO");
		final ClientDetails client = new BaseClientDetails("client", null, "read", "client_credentials", "ROLE_CLIENT");
		final OAuth2Authentication authentication = new OAuth2Authentication(
				new TokenRequest(null, "client", null, "client_credentials").createOAuth2Request(client), null);

		tokenStore.storeAccessToken(token, authentication);
	}

	public static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer FOO");
		return headers;
	}

}
