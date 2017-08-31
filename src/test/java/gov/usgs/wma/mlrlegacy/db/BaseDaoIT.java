package gov.usgs.wma.mlrlegacy.db;

import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.context.annotation.Import;

import gov.usgs.wma.mlrlegacy.MonitoringLocationDao;

@MybatisTest
@Import({MonitoringLocationDao.class, DBTestConfig.class})
public abstract class BaseDaoIT extends BaseIT {

}
