package gov.usgs.wma.mlrlegacy.db;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import gov.usgs.wma.mlrlegacy.MonitoringLocation;
import gov.usgs.wma.mlrlegacy.MonitoringLocationDao;

public class MonitoringLocationDaoCUIT extends BaseDBTest {

	@Autowired
	private MonitoringLocationDao dao;

	@Test
	@DatabaseSetup("classpath:/testData/emptyDatabase/")
	@ExpectedDatabase(
			table="legacy_location",
//			query="select * from ,
			value="classpath:/testResult/legacy_location.csv",assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED,
			modifiers=IdModifier.class
			)
	public void create() {
		id = String.valueOf(dao.create(buildAMonitoringLocation()));
		//TODO build a complete MonitoringLocation and assert that it matches the database
	}

	@Test
	@DatabaseSetup("classpath:/testData/setupOne/")
	public void update() {
		dao.update(new MonitoringLocation());
		//TODO modify all fields (except id) and assert that it matches the database
	}

	public static MonitoringLocation buildAMonitoringLocation() {
		MonitoringLocation ml = new MonitoringLocation();
		ml.setAgencyCode("USGSX");
		ml.setSiteNumber("123456789012346");
		ml.setStationName("station_nmx");
		ml.setStationIx("STATIONIXX");
		ml.setLatitude("lat_va    x");
		ml.setLongitude("long_va    x");
		//TODO Better data type?
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
		ml.setCreatedBy("site_cnx");
		ml.setCreated("2017-08-26");
		ml.setUpdatedBy("site_mnx");
		ml.setUpdated("2017-08-27");
		ml.setMinorCivilDivisionCode("mcd_x");
		return ml;
	}

}
