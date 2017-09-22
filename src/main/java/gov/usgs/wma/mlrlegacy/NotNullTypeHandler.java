package gov.usgs.wma.mlrlegacy;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.StringTypeHandler;
import org.apache.ibatis.type.TypeException;

public class NotNullTypeHandler extends StringTypeHandler {

	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
		if (parameter == null) {
			if (jdbcType == null) {
				throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
			}
			ps.setString(i, " ");
		} else {
			try {
				setNonNullParameter(ps, i, parameter, jdbcType);
			} catch (Exception e) {
				throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType + " . " +
						"Try setting a different JdbcType for this parameter or a different configuration property. " +
						"Cause: " + e, e);
			}
		}
	}

}
