package gov.usgs.wma.mlrlegacy.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

	public static final String QUERY_ALL_TO_SECOND = "select legacy_location_id,agency_cd, site_no, station_nm, station_ix,"
		+ "lat_va, long_va, dec_lat_va, dec_long_va, coord_meth_cd, coord_acy_cd,"
		+ "coord_datum_cd, district_cd, land_net_ds, map_nm, country_cd,"
		+ "state_cd, county_cd, map_scale_fc, alt_va, alt_meth_cd, alt_acy_va,"
		+ "alt_datum_cd, huc_cd, agency_use_cd, basin_cd, site_tp_cd, topo_cd,"
		+ "data_types_cd, instruments_cd, site_rmks_tx, inventory_dt, drain_area_va,"
		+ "contrib_drain_area_va, tz_cd, local_time_fg, gw_file_cd, construction_dt,"
		+ "reliability_cd, aqfr_cd, nat_aqfr_cd, site_use_1_cd, site_use_2_cd,"
		+ "site_use_3_cd, water_use_1_cd, water_use_2_cd, water_use_3_cd,"
		+ "nat_water_use_cd, aqfr_type_cd, well_depth_va, hole_depth_va,"
		+ "depth_src_cd, project_no, site_web_cd, site_cn, site_mn, mcd_cd,"
		+ "to_char(site_cr,'YYYY-MM-DD HH24:MI:SS') site_crm, to_char(site_md,'YYYY-MM-DD HH24:MI:SS')  site_mdm from legacy_location";

	@Test
	@DatabaseSetup("classpath:/testData/emptyDatabase/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_SECOND,
			value="classpath:/testResult/oneResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={IdModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void createMonitoringLocation() throws Exception {
		HttpEntity<String> entity = new HttpEntity<String>(getCompareFile("testData/", "monitoringLocation.json").replace("\"[id]\"", "1"), getHeaders());

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/monitoringLocations", entity, String.class);

		String responseBody = responseEntity.getBody();
		id = JsonPath.read(responseBody, "$.id").toString();
		createdDate = JsonPath.read(responseBody, "$.created").toString();
		createdBy = "unknown ";
		updatedDate = JsonPath.read(responseBody, "$.updated").toString();
		updatedBy = "unknown ";

		String expectedBody = getCompareFile("testResult/", "monitoringLocation.json").replace("\"[id]\"", id)
			.replace("[createdDate]", createdDate).replace("[createdBy]", createdBy)
			.replace("[updatedDate]", updatedDate).replace("[updatedBy]", updatedBy);

		assertEquals(201, responseEntity.getStatusCodeValue());
		assertFalse(createdDate.contentEquals("2017-08-26 07:33:43"));
		assertFalse(updatedDate.contentEquals("2017-08-27 23:59:59"));
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
