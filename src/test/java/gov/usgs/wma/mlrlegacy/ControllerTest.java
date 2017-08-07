package gov.usgs.wma.mlrlegacy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(Controller.class)
@AutoConfigureMockMvc(secure=false)
public class ControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MonitoringLocationDao dao;

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
}
