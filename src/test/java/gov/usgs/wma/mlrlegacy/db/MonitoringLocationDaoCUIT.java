package gov.usgs.wma.mlrlegacy.db;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import gov.usgs.wma.mlrlegacy.MonitoringLocation;
import gov.usgs.wma.mlrlegacy.MonitoringLocationDao;

public class MonitoringLocationDaoCUIT extends BaseDaoIT {

	public static final String QUERY_ALL_TO_MINUTE = "select legacy_location_id,agency_cd, site_no, station_nm, station_ix,"
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
		+ "to_char(site_cr,'YYYY-MM-DD HH24:MI') site_crm,to_char(site_md,'YYYY-MM-DD HH24:MI') site_mdm from legacy_location";

	@Autowired
	private MonitoringLocationDao dao;

	@Test
	@DatabaseSetup("classpath:/testData/emptyDatabase/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_MINUTE,
			value="classpath:/testResult/oneSparseResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={IdModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void createSparce() {
		id = String.valueOf(dao.create(buildASparseMonitoringLocation()));
		String nowMinutes = LocalDateTime.now(ZoneId.of("UTC")).toString().replace("T", " ").substring(0, 16);
		createdDate = nowMinutes;
		createdBy = "site_cnx";
		updatedDate = nowMinutes;
		updatedBy = "site_mnx";
	}

	@Test
	@DatabaseSetup("classpath:/testData/emptyDatabase/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_MINUTE,
			value="classpath:/testResult/oneResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={IdModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void createFull() {
		id = String.valueOf(dao.create(buildAMonitoringLocation()));
		String nowMinutes = LocalDateTime.now(ZoneId.of("UTC")).toString().replace("T", " ").substring(0, 16);
		createdDate = nowMinutes;
		createdBy = "site_cnx";
		updatedDate = nowMinutes;
		updatedBy = "site_mnx";
	}

	@Test
	@DatabaseSetup("classpath:/testData/setupOne/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_MINUTE,
			value="classpath:/testResult/oneSparseResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={IdModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void updateSparse() {
		MonitoringLocation ml = buildASparseMonitoringLocation();
		ml.setId(ONE_MILLION);
		dao.update(ml);
		id = String.valueOf(ONE_MILLION);
		String nowMinutes = LocalDateTime.now(ZoneId.of("UTC")).toString().replace("T", " ").substring(0, 16);
		createdDate = "2017-08-24 09:15";
		createdBy = "site_cn ";
		updatedDate = nowMinutes;
		updatedBy = "site_mnx";
	}

	@Test
	@DatabaseSetup("classpath:/testData/setupOne/")
	@ExpectedDatabase(
			table="legacy_location",
			query=QUERY_ALL_TO_MINUTE,
			value="classpath:/testResult/oneResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers={IdModifier.class,CreatedModifier.class,UpdatedModifier.class}
			)
	public void updateFull() {
		MonitoringLocation ml = buildAMonitoringLocation();
		ml.setId(ONE_MILLION);
		dao.update(ml);
		id = String.valueOf(ONE_MILLION);
		String nowMinutes = LocalDateTime.now(ZoneId.of("UTC")).toString().replace("T", " ").substring(0, 16);
		createdDate = "2017-08-24 09:15";
		createdBy = "site_cn ";
		updatedDate = nowMinutes;
		updatedBy = "site_mnx";
	}

	@Test
	@DatabaseSetup("classpath:/testData/setupOne/")
//	@ExpectedDatabase(
//			table="legacy_location",
//			query=QUERY_ALL_TO_MINUTE,
//			value="classpath:/testResult/oneResultDb/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
//			modifiers={IdModifier.class,CreatedModifier.class,UpdatedModifier.class}
//			)
	public void patchFull() {
		Map<String, Object> ml = new HashMap<>();
		ml.put("id", ONE_MILLION);
		ml.put("updatedBy", "site_mnx");
//		ml.put("agencyCode", "USGSX");
		dao.patch(ml);
		id = String.valueOf(ONE_MILLION);
		String nowMinutes = LocalDateTime.now(ZoneId.of("UTC")).toString().replace("T", " ").substring(0, 16);
		createdDate = "2017-08-24 09:15";
		createdBy = "site_cn ";
		updatedDate = nowMinutes;
		updatedBy = "site_mnx";
	}

	public static MonitoringLocation buildASparseMonitoringLocation() {
		MonitoringLocation ml = new MonitoringLocation();
		ml.setAgencyCode("USGSX");
		ml.setSiteNumber("123456789012346");
		ml.setCreatedBy("site_cnx");
		ml.setCreated("2017-08-26 07:33:43");
		ml.setUpdatedBy("site_mnx");
		ml.setUpdated("2017-08-27 23:59:59");
		return ml;
	}

	public static MonitoringLocation buildAMonitoringLocation() {
		MonitoringLocation ml = buildASparseMonitoringLocation();
		ml.setStationName("station_nmx");
		ml.setStationIx("STATIONIXX");
		ml.setLatitude("lat_va    x");
		ml.setLongitude("long_va    x");
		ml.setDecimalLatitude("43.38360141");
		ml.setDecimalLongitude("-88.97733141");
		ml.setCoordinateMethodCode("z");
		ml.setCoordinateAccuracyCode("y");
		ml.setCoordinateDatumCode("coord_datx");
		ml.setDistrictCode("dix");
		ml.setLandNet("land_net_dx");
		ml.setMapName("map_nx");
		ml.setCountryCode("UX");
		ml.setStateFipsCode("56");
		ml.setCountyCode("016");
		ml.setMapScale("map_scx");
		ml.setAltitude("alt_va x");
		ml.setAltitudeMethodCode("x");
		ml.setAltitudeAccuracyValue("aax");
		ml.setAltitudeDatumCode("alt_datumx");
		ml.setHydrologicUnitCode("07010101010101");
		ml.setAgencyUseCode("w");
		ml.setBasinCode("ex");
		ml.setSiteTypeCode("site_tx");
		ml.setTopographicCode("v");
		ml.setDataTypesCode("data_types_cd                x");
		ml.setInstrumentsCode("instruments_cd               x");
		ml.setRemarks("site_rmks_tv");
		ml.setSiteEstablishmentDate("inventox");
		ml.setDrainageArea("drain_ax");
		ml.setContributingDrainageArea("contribx");
		ml.setTimeZoneCode("tz_cdx");
		ml.setDaylightSavingsTimeFlag("u");
		ml.setGwFileCode("gw_file_cd                   x");
		ml.setFirstConstructionDate("construx");
		ml.setDataReliabilityCode("t");
		ml.setAquiferCode("aqfr_cdx");
		ml.setNationalAquiferCode("nat_aqfr_x");
		ml.setPrimaryUseOfSite("s");
		ml.setSecondaryUseOfSite("r");
		ml.setTertiaryUseOfSiteCode("q");
		ml.setPrimaryUseOfWaterCode("p");
		ml.setSecondaryUseOfWaterCode("o");
		ml.setTertiaryUseOfWaterCode("m");
		ml.setNationalWaterUseCode("nx");
		ml.setAquiferTypeCode("l");
		ml.setWellDepth("well_dex");
		ml.setHoleDepth("hole_dex");
		ml.setSourceOfDepthCode("k");
		ml.setProjectNumber("project_no x");
		ml.setSiteWebReadyCode("j");
		ml.setMinorCivilDivisionCode("mcd_x");
		return ml;
	}

}
