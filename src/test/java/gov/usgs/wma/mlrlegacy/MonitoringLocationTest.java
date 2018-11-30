package gov.usgs.wma.mlrlegacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;

public class MonitoringLocationTest {
	private ObjectMapper objectMapper;
	
	@Before
	public void setUp() {
		objectMapper = new ObjectMapper();
	}
	
	/**
	 * @see http://www.davismol.net/2015/03/21/jackson-using-jsonignore-and-jsonproperty-annotations-to-exclude-a-property-only-from-json-deserialization/
	 * @throws JsonProcessingException 
	 */
	@Test
	public void testSerializationExcludesTransactionType () throws JsonProcessingException {
		MonitoringLocation ml = new MonitoringLocation();
		ml.setTransactionType("A");
		String json = objectMapper.writeValueAsString(ml);
		assertFalse(json.contains("transactionType"));
	}
	
	/**
	 * @see http://www.davismol.net/2015/03/21/jackson-using-jsonignore-and-jsonproperty-annotations-to-exclude-a-property-only-from-json-deserialization/
	 * @throws IOException 
	 */
	@Test
	public void testDeserializationIncludesTransactionType() throws IOException {
		String input = "{\"transactionType\":\"M\"}";
		MonitoringLocation ml = objectMapper.readValue(input, MonitoringLocation.class);
		String actualTransactionType = ml.getTransactionType();
		assertEquals("M", actualTransactionType);
	}
}
