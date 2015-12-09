package jp.mts.base.infrastructure.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.sql.DataSource;

import jp.mts.base.infrastructure.jdbc.JdbcEventStoreTest.TestConfig;
import jp.mts.libs.event.eventstore.StoredEvent;
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
public class JdbcEventStoreTest {
	
	@Autowired protected DataSource dataSource;
	
	
	@Test
	public void test_persistence() {
		
		System.setProperty("activejdbc.log", "");
		Base.open(dataSource);
		
		
		
		JdbcEventStore target = new JdbcEventStore();
		target.add(new StoredEvent("publisher01", Dates.dateTime("2016/01/15 12:01:02.003"), "test", "testbody".getBytes()));
		List<StoredEvent> storedEvents = target.findEventsAfter(0);
		
		assertThat(storedEvents.size(), is(1));
		assertThat(storedEvents.get(0).getPublisherId(), is("publisher01"));
		assertThat(storedEvents.get(0).getOccurred(), is(Dates.dateTime("2016/01/15 12:01:02.003")));
		assertThat(storedEvents.get(0).getEventType(), is("test"));
		assertThat(new String(storedEvents.get(0).getEventBody()), is("testbody"));
		
		
		Base.detach();
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
