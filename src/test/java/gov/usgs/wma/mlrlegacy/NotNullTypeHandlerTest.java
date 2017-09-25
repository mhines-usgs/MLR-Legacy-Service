package gov.usgs.wma.mlrlegacy;

import static org.mockito.Mockito.verify;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import gov.usgs.wma.mlrlegacy.NotNullTypeHandler;

@RunWith(SpringRunner.class)
public class NotNullTypeHandlerTest {

	private NotNullTypeHandler handler;

	@MockBean
	private PreparedStatement ps;

	@Before
	public void init() {
		handler = new NotNullTypeHandler();
	}

	@Test
	public void givenNullParameter_thenSetSpace() throws SQLException {
		handler.setParameter(ps, 0, null, JdbcType.CHAR);
		verify(ps).setString(0, " ");
	}

	@Test
	public void givenNotNullParameter_thenSetValue() throws SQLException {
		handler.setParameter(ps, 0, "steve", JdbcType.CHAR);
		verify(ps).setString(0, "steve");
	}

}
