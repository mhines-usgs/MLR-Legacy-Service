package gov.usgs.wma.mlrlegacy.db;

import java.math.BigInteger;

import org.dbunit.dataset.ReplacementDataSet;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.ReplacementDataSetModifier;

import gov.usgs.wma.mlrlegacy.MonitoringLocationDao;

@RunWith(SpringRunner.class)
@MybatisTest
@ActiveProfiles("it")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class
})
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Import({MonitoringLocationDao.class, DBTestConfig.class})
@Transactional(propagation=Propagation.NOT_SUPPORTED)
public abstract class BaseDBTest {

	public static final BigInteger ONE_MILLION = BigInteger.valueOf(1000000);

	protected String id;

	protected class IdModifier extends ReplacementDataSetModifier {

		@Override
		protected void addReplacements(ReplacementDataSet dataset) {
			dataset.addReplacementSubstring("[id]", id);
		}
	}

}
