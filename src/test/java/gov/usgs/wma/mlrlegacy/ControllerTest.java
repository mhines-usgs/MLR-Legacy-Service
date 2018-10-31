package gov.usgs.wma.mlrlegacy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import gov.usgs.wma.mlrlegacy.db.BaseIT;
import java.util.Arrays;
import springfox.documentation.spi.service.contexts.SecurityContext;

@RunWith(SpringRunner.class)
@WebMvcTest(Controller.class)
@AutoConfigureMockMvc(secure=false)
public class ControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MonitoringLocationDao dao;

	@MockBean
	private Authentication authentication;

	@MockBean
	private SecurityContext securityContext;

	@Test
	public void givenReturnData_whenGetByAgencyCodeAndSiteNumber_thenReturnMonitoringLocation() throws Exception {
		MonitoringLocation mlOne = new MonitoringLocation();

		mlOne.setId(BigInteger.ONE);
		mlOne.setAgencyCode(BaseIT.DEFAULT_AGENCY_CODE);
		mlOne.setSiteNumber("987654321");

		Map<String, Object> params = new HashMap<>();
		params.put(Controller.AGENCY_CODE, BaseIT.DEFAULT_AGENCY_CODE);
		params.put(Controller.SITE_NUMBER, "987654321");

		MultiValueMap<String, String> cruParams = new LinkedMultiValueMap<>();
		cruParams.set(Controller.AGENCY_CODE, BaseIT.DEFAULT_AGENCY_CODE);
		cruParams.set(Controller.SITE_NUMBER, "987654321");
		
		given(dao.getByAK(params)).willReturn(mlOne);

		mvc.perform(get("/monitoringLocations").params(cruParams))
			.andExpect(status().isOk())
			.andExpect(jsonPath("id", is(equalTo(1))));
	}

	@Test
	public void givenReturnNoData_whenGetByAgencyCodeAndSiteNumber_thenReturn404() throws Exception {
		MultiValueMap<String, String> cruParams = new LinkedMultiValueMap<>();
		cruParams.set(Controller.AGENCY_CODE, BaseIT.DEFAULT_AGENCY_CODE);
		cruParams.set(Controller.SITE_NUMBER, "987654321");
		
		given(dao.getByNormalizedName(any())).willReturn(null);

		mvc.perform(get("/monitoringLocations").params(cruParams))
			.andExpect(status().isNotFound());
	}

	@Test
	public void givenReturnData_whenGetByStationName_thenReturnMonitoringLocation() throws Exception {
		final String MY_NORMALIZED_STATION_NAME = "THELOCALWATERINGHOLE";
		MonitoringLocation mlOne = new MonitoringLocation();

		mlOne.setId(BigInteger.ZERO);
		mlOne.setAgencyCode(BaseIT.DEFAULT_AGENCY_CODE);
		mlOne.setSiteNumber("987654321");
		mlOne.setStationName(MY_NORMALIZED_STATION_NAME);

		MonitoringLocation mlTwo = new MonitoringLocation();
		mlTwo.setId(BigInteger.ONE);
		mlTwo.setAgencyCode("Some other agency");
		mlTwo.setSiteNumber("123456");
		mlTwo.setStationName(MY_NORMALIZED_STATION_NAME);

		List<MonitoringLocation> monitoringLocations = Arrays.asList(mlOne, mlTwo);

		Map<String, Object> params = new HashMap<>();
		params.put(Controller.NORMALIZED_STATION_NAME, MY_NORMALIZED_STATION_NAME);

		MultiValueMap<String, String> cruParams = new LinkedMultiValueMap<>();
		cruParams.set(Controller.NORMALIZED_STATION_NAME, MY_NORMALIZED_STATION_NAME);

		given(dao.getByNormalizedName(params)).willReturn(monitoringLocations);

		List<Integer> expectedIds = Arrays.asList(0, 1);
		mvc.perform(get("/monitoringLocations").params(cruParams))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$..['id']", is(equalTo(expectedIds))));
	}

	@Test
	public void givenReturnNull_whenGetByStationName_thenReturn404() throws Exception {
		MultiValueMap<String, String> cruParams = new LinkedMultiValueMap<>();
		cruParams.set(Controller.NORMALIZED_STATION_NAME, "STATIONY");

		given(dao.getByNormalizedName(any())).willReturn(null);

		mvc.perform(get("/monitoringLocations").params(cruParams))
			.andExpect(status().isNotFound());
	}

	@Test
	public void givenReturnEmptyList_whenGetByStationName_thenReturn404() throws Exception {
		MultiValueMap<String, String> cruParams = new LinkedMultiValueMap<>();
		cruParams.set(Controller.NORMALIZED_STATION_NAME, "STATIONY");
		List<MonitoringLocation> emptyList = new ArrayList<>();
		given(dao.getByNormalizedName(any())).willReturn(emptyList);

		mvc.perform(get("/monitoringLocations").params(cruParams))
			.andExpect(status().isNotFound());
	}

	@Test
	public void givenML_whenGetById_thenReturnML() throws Exception {
		MonitoringLocation ml = new MonitoringLocation();
		ml.setId(BigInteger.ONE);
		ml.setAgencyCode(BaseIT.DEFAULT_AGENCY_CODE);
		ml.setSiteNumber("987654321");

		given(dao.getById(BigInteger.ONE)).willReturn(ml);

		mvc.perform(get("/monitoringLocations/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("id", is(equalTo(1))))
			.andExpect(jsonPath(Controller.AGENCY_CODE, is(equalTo(ml.getAgencyCode()))))
			.andExpect(jsonPath(Controller.SITE_NUMBER, is(equalTo(ml.getSiteNumber()))))
		;
	}

	@Test
	public void givenNoML_whenGetById_thenReturnNotFound() throws Exception {
		given(dao.getById(BigInteger.ONE)).willReturn(null);

		mvc.perform(get("/monitoringLocations/1"))
				.andExpect(status().isNotFound())
				.andExpect(content().string(""));
	}

	@Test
	public void givenML_whenCreate_thenReturnMLWithId() throws Exception {
		MonitoringLocation newMl = new MonitoringLocation();
		newMl.setAgencyCode(BaseIT.DEFAULT_AGENCY_CODE);
		newMl.setSiteNumber("12345678");
		newMl.setId(BigInteger.ONE);

		String requestBody = "{\"agencyCode\": \"USGS\", \"siteNumber\": \"12345678\"}";

		given(dao.create(any(MonitoringLocation.class))).willReturn(BigInteger.ONE);
		given(dao.getById(BigInteger.ONE)).willReturn(newMl);

		mvc.perform(post("/monitoringLocations").content(requestBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id", is(equalTo(1))))
				.andExpect(jsonPath(Controller.AGENCY_CODE, is(equalTo(BaseIT.DEFAULT_AGENCY_CODE))))
				.andExpect(jsonPath(Controller.SITE_NUMBER, is(equalTo("12345678"))));
	}

	@Test
	public void givenML_whenUpdate_thenReturnUpdatedML() throws Exception {
		String requestBody = "{\"agencyCode\": \"USGS\", \"siteNumber\": \"12345678\"}";
		MonitoringLocation ml = new MonitoringLocation();

		ml.setId(BigInteger.ONE);
		ml.setAgencyCode(BaseIT.DEFAULT_AGENCY_CODE);
		ml.setSiteNumber("12345678");

		Mockito.doNothing().when(dao).update(any(MonitoringLocation.class));
		given(dao.getById(BigInteger.ONE)).willReturn(ml);

		mvc.perform(put("/monitoringLocations/1").content(requestBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", is(equalTo(1))))
				.andExpect(jsonPath(Controller.AGENCY_CODE, is(equalTo(BaseIT.DEFAULT_AGENCY_CODE))))
				.andExpect(jsonPath(Controller.SITE_NUMBER, is(equalTo("12345678"))));
	}

	@Test
	public void givenNewML_whenUpdate_thenStatusNotFound() throws Exception {
		String requestBody = "{\"agencyCode\": \"USGS\", \"siteNumber\": \"12345678\"}";
		
		given(dao.getById(BigInteger.ONE)).willReturn(null);
		mvc.perform(put("/monitoringLocations/1").content(requestBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void givenML_whenPatch_thenReturnUpdatedML() throws Exception {
		String requestBody = "{\"agencyCode\": \"USGS\", \"siteNumber\": \"12345678\"}";
		MonitoringLocation ml = new MonitoringLocation();
		ml.setId(BigInteger.ONE);
		ml.setAgencyCode(BaseIT.DEFAULT_AGENCY_CODE);
		ml.setSiteNumber("12345678");

		Mockito.doNothing().when(dao).patch(anyMap());
		given(dao.getByAK(anyMap())).willReturn(ml);

		mvc.perform(patch("/monitoringLocations").content(requestBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", is(equalTo(1))))
				.andExpect(jsonPath(Controller.AGENCY_CODE, is(equalTo(BaseIT.DEFAULT_AGENCY_CODE))))
				.andExpect(jsonPath(Controller.SITE_NUMBER, is(equalTo("12345678"))));
	}

	@Test
	public void givenNewML_whenPatch_thenStatusNotFound() throws Exception {
		String requestBody = "{\"agencyCode\": \"USGS\", \"siteNumber\": \"12345678\"}";

		Mockito.doNothing().when(dao).patch(anyMap());
		given(dao.getByAK(anyMap())).willReturn(null);
		mvc.perform(patch("/monitoringLocations").content(requestBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void givenNoSecurityContext_thenUsernameUnknown() {
		Controller controller = new Controller();
		assertEquals(Controller.UNKNOWN_USERNAME, controller.getUsername());
	}

	@Test
	@WithAnonymousUser
	public void givenAnonymousUser_thenUsernameUnkown() {
		Controller controller = new Controller();
		assertEquals(Controller.UNKNOWN_USERNAME, controller.getUsername());
	}

	@Test
	@WithMockUser(username="Known")
	public void givenRealUser_thenUsernameKown() {
		Controller controller = new Controller();
		assertEquals("Known", controller.getUsername());
	}

}
