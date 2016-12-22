package test.spring;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import test.spring.dbo.DbAccess;

@Component
public class TestBean {
	@Autowired
	@Qualifier(value="dbAccess")
	private DbAccess dbAccess;
	public TestBean() {
	}
	public String getMessage() {
	  return "Hello World!";
	}
	
	public String getSelect(int id)  {
		String sql = "SELECT * FROM MESSAGE WHERE ID = ?";		
		@SuppressWarnings("unchecked")
		String message = (String)dbAccess.getJDBCTemplate().queryForObject(
				sql, new Object[] {Integer.valueOf(id)}, new StringRowMapper());
		return message;
	}
	private static class StringRowMapper implements RowMapper
	{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			String result = rs.getString("message");
			return result;
		}
	}
}