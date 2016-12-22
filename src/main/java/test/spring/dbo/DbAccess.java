package test.spring.dbo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

public class DbAccess {	
	private JdbcTemplate jt;
	public DbAccess() {
		
	}
	@Autowired
	@Qualifier(value="dataSource")
	public void setDs(DataSource ds) {
		if (ds == null) {
			throw new IllegalArgumentException("ds is null");
		}
		jt = new JdbcTemplate(ds);
	}
	public JdbcTemplate getJDBCTemplate() {
		return jt;
	}
	
}
