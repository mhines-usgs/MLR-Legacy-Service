package gov.usgs.wma.mlrlegacy;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(Controller.class)
@AutoConfigureMockMvc(secure=false)
public class ControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MonitoringLocationDao dao;
	
	@Test
	public void givenReturnData_whenGetByMap_thenReturnList() throws Exception {
		List<MonitoringLocation> mlList = new ArrayList<>();
		MonitoringLocation mlOne = new MonitoringLocation();
		MonitoringLocation mlTwo = new MonitoringLocation();
		
		mlOne.setId(BigInteger.ONE);
		mlOne.setAgencyCode("USGS");
		mlOne.setSiteNumber("987654321");
		mlTwo.setId(BigInteger.TEN);
		mlTwo.setAgencyCode("USGS");
		mlTwo.setSiteNumber("11112222");
		
		mlList.add(mlOne);
		mlList.add(mlTwo);
		Map<String, String> params = new HashMap<>();
		
		given(dao.getByMap(params)).willReturn(mlList);
		
		mvc.perform(get("/monitoringLocations"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].id", is(equalTo(1))))
				.andExpect(jsonPath("$[1].id", is(equalTo(10))));
	}
	
	@Test
	public void givenReturnData_whenGetByAgencyCode_theReturnList() throws Exception {
		List<MonitoringLocation> mlList = new ArrayList<>();
		MonitoringLocation mlOne = new MonitoringLocation();
		
		mlOne.setId(BigInteger.ONE);
		mlOne.setAgencyCode("USGS");
		mlOne.setSiteNumber("987654321");
		
		mlList.add(mlOne);
		
		Map<String, String> params = new HashMap<>();
		params.put("agencyCode", "USGS");
		
		given(dao.getByMap(params)).willReturn(mlList);
		
		mvc.perform(get("/monitoringLocations").param("agencyCode", "USGS"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(equalTo(1))))
				.andExpect(jsonPath("$[0].id", is(equalTo(1))));
	}
	
	@Test
	public void givenReturnData_whenGetBySiteNumber_theReturnList() throws Exception {
		List<MonitoringLocation> mlList = new ArrayList<>();
		MonitoringLocation mlOne = new MonitoringLocation();
		
		mlOne.setId(BigInteger.ONE);
		mlOne.setAgencyCode("USGS");
		mlOne.setSiteNumber("987654321");
		
		mlList.add(mlOne);
		
		Map<String, String> params = new HashMap<>();
		params.put("siteNumber", "987654321");
		
		given(dao.getByMap(params)).willReturn(mlList);
		
		mvc.perform(get("/monitoringLocations").param("siteNumber", "987654321"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(equalTo(1))))
				.andExpect(jsonPath("$[0].id", is(equalTo(1))));
	}
	
	@Test
	public void givenML_whenGetById_thenReturnML() throws Exception {
		MonitoringLocation ml = new MonitoringLocation();
		ml.setId(BigInteger.ONE);
		ml.setAgencyCode("USGS");
		ml.setSiteNumber("987654321");

		given(dao.getById(BigInteger.ONE)).willReturn(ml);

		mvc.perform(get("/monitoringLocations/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("id", is(equalTo(1))))
			.andExpect(jsonPath("agencyCode", is(equalTo(ml.getAgencyCode()))))
			.andExpect(jsonPath("siteNumber", is(equalTo(ml.getSiteNumber()))))
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
		newMl.setAgencyCode("USGS");
		newMl.setSiteNumber("12345678");
		newMl.setId(BigInteger.ONE);
		
		String requestBody = "{\"agencyCode\": \"USGS\", \"siteNumber\": \"12345678\"}";
		
		given(dao.create(any(MonitoringLocation.class))).willReturn(BigInteger.ONE);
		given(dao.getById(BigInteger.ONE)).willReturn(newMl);
		
		mvc.perform(post("/monitoringLocations").content(requestBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id", is(equalTo(1))))
				.andExpect(jsonPath("agencyCode", is(equalTo("USGS"))))
				.andExpect(jsonPath("siteNumber", is(equalTo("12345678"))));
	}

	@Test
	public void givenML_whenUpdate_thenReturnUpdatedML() throws Exception {
		String requestBody = "{\"agencyCode\": \"USGS\", \"siteNumber\": \"12345678\"}";
		MonitoringLocation ml = new MonitoringLocation();
		
		ml.setId(BigInteger.ONE);
		ml.setAgencyCode("USGS");
		ml.setSiteNumber("12345678");
		
		Mockito.doNothing().when(dao).update(any(MonitoringLocation.class));
		given(dao.getById(BigInteger.ONE)).willReturn(ml);
		
		mvc.perform(put("/monitoringLocations/1").content(requestBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", is(equalTo(1))))
				.andExpect(jsonPath("agencyCode", is(equalTo("USGS"))))
				.andExpect(jsonPath("siteNumber", is(equalTo("12345678"))));
	}
	
	@Test
	public void givenNewML_whenUpdate_thenStatusNotFound() throws Exception {
		String requestBody = "{\"agencyCode\": \"USGS\", \"siteNumber\": \"12345678\"}";
		
		given(dao.getById(BigInteger.ONE)).willReturn(null);
		mvc.perform(put("/monitoringLocations/1").content(requestBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}
