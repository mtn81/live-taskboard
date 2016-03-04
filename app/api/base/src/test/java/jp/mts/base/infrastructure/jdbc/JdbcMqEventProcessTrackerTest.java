package jp.mts.base.infrastructure.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.sql.DataSource;

import jp.mts.base.infrastructure.jdbc.JdbcMqEventProcessTrackerTest.TestConfig;
import jp.mts.libs.unittest.Dates;

import org.javalite.activejdbc.Base;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class)
@ActiveProfiles("test")
public class JdbcMqEventProcessTrackerTest {

	@Autowired DataSource dataSource;
	JdbcMqEventProcessTracker target = new JdbcMqEventProcessTracker();

	@Test public void 
	test() {
		System.setProperty("activejdbc.log", "");
		Base.open(dataSource);
		
		assertThat(target.ignoreProcess("cate1", "id1", Dates.dateShortTime("2016/03/03 12:00")), is(false));
		target.endProcess("cate1", "id1", Dates.dateShortTime("2016/03/03 12:00"));
		assertThat(target.ignoreProcess("cate1", "id1", Dates.dateShortTime("2016/03/03 11:59")), is(true));
		assertThat(target.ignoreProcess("cate1", "id1", Dates.dateShortTime("2016/03/03 12:01")), is(false));

		Base.close();
	}

	@Configuration
	@Profile("test")
	public static class TestConfig {
		@Bean
		public DataSource dataSource(){
			return new TransactionAwareDataSourceProxy(
				new EmbeddedDatabaseBuilder()
					.setType(EmbeddedDatabaseType.H2)
					.addScript("classpath:test-schema.sql")
					.build());
		}
	}
}
