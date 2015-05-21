package jp.mts.authaccess.infrastructure.persistence.jdbc;

import java.sql.SQLException;

import javax.sql.DataSource;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthRepository;
import jp.mts.authaccess.infrastructure.persistence.jdbc.model.UserModel;

import org.javalite.activejdbc.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcAuthRepository implements AuthRepository {
	
	@Autowired
	private DataSource dataSource;

	@Override
	public Auth authOf(String id, String password){
		DB db = new DB("default");
		try {
			db.attach(dataSource.getConnection());
			
			UserModel user = UserModel.findFirst("user_id = ? and password = ?", id, password);
			if(user == null) return null;
			return new Auth(user.getString("user_id"), user.getString("name"));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			db.detach();
		}
	}
}
