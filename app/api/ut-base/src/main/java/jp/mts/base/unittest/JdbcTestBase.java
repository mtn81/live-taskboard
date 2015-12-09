package jp.mts.base.unittest;

import javax.sql.DataSource;

import jp.mts.base.unittest.JdbcTestBase.TestConfig;

import org.javalite.activejdbc.Base;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
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
public abstract class JdbcTestBase {

	@Autowired protected DataSource dataSource;
	
	@Rule
	public WithActiveJdbc WithActiveJdbc = new WithActiveJdbc(this);
	
	@Configuration
	@Profile("test")
	public static class TestConfig {
		@Bean
		public DataSource dataSource(){
			return new TransactionAwareDataSourceProxy(
				new EmbeddedDatabaseBuilder()
					.setType(EmbeddedDatabaseType.H2)
					.addScript("classpath:/develop/V1.1__schema.sql")
					.addScript("classpath:test-schema-id.sql")
					.addScript("classpath:test-schema.sql")
					.build());
		}
	}
	
	public static class WithActiveJdbc extends ExternalResource {
		
		private JdbcTestBase test;
		
		public WithActiveJdbc(JdbcTestBase test) {
			this.test = test;
		}
		
		@Override
		protected void before() throws Throwable {
			System.setProperty("activejdbc.log", "");
			Base.open(test.dataSource);
		}

		@Override
		protected void after() {
			Base.detach();
		}
		
	}
	
}
