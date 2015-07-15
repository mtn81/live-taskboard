package jp.mts.base.config.aspect;

import javax.sql.DataSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.javalite.activejdbc.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ActiveJdbcIntegrateAspect {
	
	@Autowired
	private DataSource dataSource;

	@Around("jp.mts.base.config.aspect.AppArchitecture.repository() || " + 
			"jp.mts.base.config.aspect.AppArchitecture.eventStore()")
	public Object attachDb(ProceedingJoinPoint pjp) throws Throwable {
		DB db = new DB("default");
		try {
			db.attach(dataSource.getConnection());
			return pjp.proceed();
		} finally {
			db.detach();
		}
	}

}
