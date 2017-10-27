package gov.usgs.wma.mlrlegacy;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitoringLocations")
public class Controller {

	@Autowired
	private MonitoringLocationDao mLDao;

	public static final String UNKNOWN_USERNAME = "unknown ";
	public static final String AGENCY_CODE = "agencyCode";
	public static final String SITE_NUMBER = "siteNumber";
	public static final String UPDATED_BY = "updatedBy";

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping()
	public List<MonitoringLocation> getMonitoringLocations(
		@RequestParam(name = AGENCY_CODE, required = false) String agencyCode,
		@RequestParam(name = SITE_NUMBER, required = false) String siteNumber) {
		Map<String, Object> params = new HashMap<>();
		if (null != agencyCode) {
			params.put(AGENCY_CODE, agencyCode);
		}
		if (null != siteNumber) {
			params.put(SITE_NUMBER, siteNumber);
		}
		return mLDao.getByMap(params);
	}

	@GetMapping("/{id}")
	public MonitoringLocation getMonitoringLocation(@PathVariable("id") String id, HttpServletResponse response) {
		MonitoringLocation ml = mLDao.getById(NumberUtils.parseNumber(id, BigInteger.class));
		if (null == ml) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}
		return ml;
	}

	@PostMapping()
	public MonitoringLocation createMonitoringLocation(@RequestBody MonitoringLocation ml, HttpServletResponse response) {
		ml.setCreatedBy(getUsername());
		ml.setUpdatedBy(getUsername());
		BigInteger newId = mLDao.create(ml);

		response.setStatus(HttpStatus.CREATED.value());
		return mLDao.getById(newId);
	}

	@PutMapping("/{id}")
	public MonitoringLocation updateMonitoringLocation(@PathVariable("id") String id, @RequestBody MonitoringLocation ml,
			HttpServletResponse response) {
		BigInteger idInt = NumberUtils.parseNumber(id, BigInteger.class);

		if (null == mLDao.getById(idInt)) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}
		else {
			ml.setId(idInt);
			ml.setUpdatedBy(getUsername());
			mLDao.update(ml);
		}
		return mLDao.getById(idInt);
	}

	@PatchMapping()
	public MonitoringLocation patchMonitoringLocation(@RequestBody Map<String, Object> ml,
			HttpServletResponse response) {

		ml.put(UPDATED_BY, getUsername());
		mLDao.patch(ml);
		List<MonitoringLocation> lst = mLDao.getByMap(ml);
		if (lst.isEmpty()) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		} else {
			return lst.get(0);
		}
	}

	protected String getUsername() {
		String username = UNKNOWN_USERNAME;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication && !(authentication instanceof AnonymousAuthenticationToken)) {
			username= authentication.getName();
		}
		return username;
	}

}
