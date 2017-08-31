package gov.usgs.wma.mlrlegacy.db;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.csv.CsvURLDataSet;
import org.springframework.core.io.Resource;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;

public class CsvDataSetLoader extends AbstractDataSetLoader {

	@Override
	protected IDataSet createDataSet(Resource resource) throws Exception {
		ReplacementDataSet replacementDataSet = new ReplacementDataSet(new CsvURLDataSet(resource.getURL()));
		return replacementDataSet;
	}

}
