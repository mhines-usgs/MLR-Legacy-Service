package gov.usgs.wma.mlrlegacy.db;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.mlrlegacy.MonitoringLocation;
import gov.usgs.wma.mlrlegacy.MonitoringLocationDao;

@DatabaseSetup("classpath:/testData/setupOne/")
public class MonitoringLocationDaoRIT extends BaseDBTest {

	@Autowired
	private MonitoringLocationDao dao;

	@Test
	public void getAll() {
		List<MonitoringLocation> locations = dao.getByMap(null);
		assertEquals(1, locations.size());
		assertOneMillion(locations.get(0));
	}

	@Test
	public void getById() {
		MonitoringLocation location = dao.getById(ONE_MILLION);
		assertOneMillion(location);
	}

	public static void assertOneMillion(MonitoringLocation location) {
		assertEquals(ONE_MILLION, location.getId());
		assertEquals("USGS ", location.getAgencyCode());
		assertEquals("123456789012345", location.getSiteNumber());
		assertEquals("station_nm", location.getStationName());
		assertEquals("STATIONIX", location.getStationIx());
		assertEquals("lat_va     ", location.getLatitude());
		assertEquals("long_va     ", location.getLongitude());
		//TODO Better data type?
		assertEquals("43.3836014", location.getDecimalLatitude());
		assertEquals("-88.9773314", location.getDecimalLongitude());
		assertEquals("a", location.getCoordinateMethodCode());
		assertEquals("b", location.getCoordinateAccuracyCode());
		assertEquals("coord_datu", location.getCoordinateDatumCode());
		assertEquals("dis", location.getDistrictCode());
		assertEquals("land_net_ds", location.getLandNet());
		assertEquals("map_nm", location.getMapName());
		assertEquals("US", location.getCountryCode());
		assertEquals("55", location.getStateFipsCode());
		assertEquals("013", location.getCountyCode());
		assertEquals("map_sca", location.getMapScale());
		assertEquals("alt_va  ", location.getAltitude());
		assertEquals("c", location.getAltitudeMethodCode());
		assertEquals("aav", location.getAltitudeAccuracyValue());
		assertEquals("alt_datum_", location.getAltitudeDatumCode());
		assertEquals("070101010101", location.getHydrologicUnitCode());
		assertEquals("d", location.getAgencyUseCode());
		assertEquals("ee", location.getBasinCode());
		assertEquals("site_tp", location.getSiteTypeCode());
		assertEquals("f", location.getTopographicCode());
		assertEquals("data_types_cd                 ", location.getDataTypesCode());
		assertEquals("instruments_cd                ", location.getInstrumentsCode());
		assertEquals("site_rmks_tx", location.getRemarks());
		assertEquals("inventor", location.getSiteEstablishmentDate());
		assertEquals("drain_ar", location.getDrainageArea());
		assertEquals("contrib_", location.getContributingDrainageArea());
		assertEquals("tz_cd ", location.getTimeZoneCode());
		assertEquals("g", location.getDaylightSavingsTimeFlag());
		assertEquals("gw_file_cd                    ", location.getGwFileCode());
		assertEquals("construc", location.getFirstConstructionDate());
		assertEquals("h", location.getDataReliabilityCode());
		assertEquals("aqfr_cd ", location.getAquiferCode());
		assertEquals("nat_aqfr_c", location.getNationalAquiferCode());
		assertEquals("i", location.getPrimaryUseOfSite());
		assertEquals("j", location.getSecondaryUseOfSite());
		assertEquals("k", location.getTertiaryUseOfSiteCode());
		assertEquals("l", location.getPrimaryUseOfWaterCode());
		assertEquals("m", location.getSecondaryUseOfWaterCode());
		assertEquals("n", location.getTertiaryUseOfWaterCode());
		assertEquals("na", location.getNationalWaterUseCode());
		assertEquals("o", location.getAquiferTypeCode());
		assertEquals("well_dep", location.getWellDepth());
		assertEquals("hole_dep", location.getHoleDepth());
		assertEquals("p", location.getSourceOfDepthCode());
		assertEquals("project_no  ", location.getProjectNumber());
		assertEquals("q", location.getSiteWebReadyCode());
		assertEquals("site_cn ", location.getCreatedBy());
		assertEquals("2017-08-24", location.getCreated());
		assertEquals("site_mn ", location.getUpdatedBy());
		assertEquals("2017-08-25", location.getUpdated());
		assertEquals("mcd_c", location.getMinorCivilDivisionCode());
	}

}
