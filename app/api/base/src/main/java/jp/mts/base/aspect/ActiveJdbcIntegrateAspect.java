package jp.mts.base.aspect;

import java.sql.Connection;

import javax.sql.DataSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.javalite.activejdbc.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ActiveJdbcIntegrateAspect {
	
	@Autowired
	private DataSource dataSource;

	@Around("jp.mts.base.aspect.AppArchitecture.repository() || " + 
			"jp.mts.base.aspect.AppArchitecture.query() || " +
			"jp.mts.base.aspect.AppArchitecture.event()")
	public Object attachDb(ProceedingJoinPoint pjp) throws Throwable {
		Connection connection = DataSourceUtils.getConnection(dataSource);
		try {
			Base.attach(connection);
			return pjp.proceed();
		} finally {
			if (DataSourceUtils.isConnectionTransactional(connection, dataSource)) {
				Base.detach();
			}else{
				Base.close();
			}
		}
	}

}
