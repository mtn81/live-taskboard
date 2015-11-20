package jp.mts.base.domain.model;

import jp.mts.base.domain.model.DomainEventPublisher.Subscriber;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class DomainEventPublisherTest {

	@Mocked Subscriber<TestEvent> subscriber;
	@Mocked Subscriber<TestEvent> subscriber1;
	@Mocked Subscriber<TestEvent2> subscriber2;

	@Test
	public void test() {
		DomainEventPublisher publisher = new DomainEventPublisher();
		TestEvent event = new TestEvent();
		publisher.register(TestEvent.class, subscriber);
		new Expectations() {{
			subscriber.subscribe(event);
		}};

		publisher.publish(event);
		publisher.fire();
	}
	@Test
	public void test_eventSubTyping() {
		DomainEventPublisher publisher = new DomainEventPublisher();
		TestEventSub event = new TestEventSub();
		publisher.register(TestEvent.class, subscriber);
		new Expectations() {{
			subscriber.subscribe(event);
		}};

		publisher.publish(event);
		publisher.fire();
	}
	
	@Test
	public void test_multiThread() throws InterruptedException {

		DomainEventPublisher publisher = new DomainEventPublisher();
		new Expectations() {{
			subscriber1.subscribe((TestEvent)any);
				times = 1;
			subscriber2.subscribe((TestEvent2)any);
				times = 1;
		}};
		
		
		Thread t1 = new Thread(() -> {
			publisher.register(TestEvent.class, subscriber1);
			publisher.publish(new TestEvent());
			publisher.fire();
		});
		Thread t2 = new Thread(() -> {
			publisher.register(TestEvent2.class, subscriber2);
			publisher.publish(new TestEvent2());
			publisher.fire();
		});
		
		t1.start(); t2.start();
		t1.join(); t2.join();

	}
	@Test
	public void test_threadReused() throws InterruptedException {

		DomainEventPublisher publisher = new DomainEventPublisher();
		new Expectations() {{
			subscriber.subscribe((TestEvent)any);
				times = 2;
		}};

		Thread t = new Thread(() -> {
			publisher.initialize();
			publisher.register(TestEvent.class, subscriber);
			publisher.publish(new TestEvent());
			publisher.fire();
		});
		
		t.run(); t.run();
		
	}

	public static class TestEvent extends DomainEvent{
	}
	public static class TestEventSub extends TestEvent{
	}
	public static class TestEvent2 extends DomainEvent{
	}
	public static class TestEvent3 extends DomainEvent{
		static int subscribed = 0;
	}


}
