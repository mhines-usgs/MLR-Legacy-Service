package gov.usgs.wma.mlrlegacy.db;

import java.io.IOException;
import java.math.BigInteger;

import org.dbunit.dataset.ReplacementDataSet;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.ReplacementDataSetModifier;

@RunWith(SpringRunner.class)
@ActiveProfiles("it")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class
})
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Transactional(propagation=Propagation.NOT_SUPPORTED)
public abstract class BaseIT {

	public static final BigInteger ONE_MILLION = BigInteger.valueOf(1000000);

	protected String id;
	protected class IdModifier extends ReplacementDataSetModifier {
		@Override
		protected void addReplacements(ReplacementDataSet dataset) {
			dataset.addReplacementSubstring("[id]", id);
		}
	}

	protected String created;
	protected class CreatedModifier extends ReplacementDataSetModifier {
		@Override
		protected void addReplacements(ReplacementDataSet dataset) {
			dataset.addReplacementSubstring("[created]", created);
		}
	}

	protected String updated;
	protected class UpdatedModifier extends ReplacementDataSetModifier {
		@Override
		protected void addReplacements(ReplacementDataSet dataset) {
			dataset.addReplacementSubstring("[updated]", updated);
		}
	}

	public String getCompareFile(String folder, String file) throws IOException {
		return new String(FileCopyUtils.copyToByteArray(new ClassPathResource(folder + file).getInputStream()));
	}

}
