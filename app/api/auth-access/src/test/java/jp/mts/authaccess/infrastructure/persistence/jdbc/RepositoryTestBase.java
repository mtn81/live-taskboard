package jp.mts.authaccess.infrastructure.persistence.jdbc;

import javax.sql.DataSource;

import jp.mts.authaccess.infrastructure.persistence.jdbc.RepositoryTestBase.TestConfig;

import org.javalite.activejdbc.DB;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class )
public abstract class RepositoryTestBase {

	@Autowired protected DataSource dataSource;
	
	@Rule
	public WithActiveJdbc WithActiveJdbc = new WithActiveJdbc(this);
	
	@Configuration
	public static class TestConfig {
		
		@Bean
		public DataSource dataSource(){
			return new TransactionAwareDataSourceProxy(
				new EmbeddedDatabaseBuilder()
					.setType(EmbeddedDatabaseType.H2)
					.addScript("classpath:schema.sql")
					.addScript("classpath:test-schema.sql")
					.build());
		}
	}
	
	public static class WithActiveJdbc extends ExternalResource {
		
		private RepositoryTestBase test;
		
		public WithActiveJdbc(RepositoryTestBase test) {
			this.test = test;
		}
		
		@Override
		protected void before() throws Throwable {
			System.setProperty("activejdbc.log", "");
			new DB("default").open(test.dataSource);
		}

		@Override
		protected void after() {
			new DB("default").detach();
		}
		
	}
	
}
