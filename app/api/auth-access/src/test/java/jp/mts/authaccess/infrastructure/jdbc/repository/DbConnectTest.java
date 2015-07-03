package jp.mts.authaccess.infrastructure.jdbc.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

public class DbConnectTest {

	@Test
	public void test() throws ClassNotFoundException, SQLException{
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(
				"jdbc:postgresql://192.168.77.11:5432/auth-access", 
				"app", "pass");
		ResultSet rs = con.createStatement().executeQuery("select * from users");
		rs.next();
		System.out.println(rs.getString("id"));
	}
}
