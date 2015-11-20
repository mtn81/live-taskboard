package jp.mts.base.domain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainEventPublisher {

	private static ThreadLocal<Map<Class<?>, List<Subscriber<?>>>> subscribers 
		= new ThreadLocal<Map<Class<?>, List<Subscriber<?>>>>(){
				@Override
				protected Map<Class<?>, List<Subscriber<?>>> initialValue() {
					return new HashMap<>();
				}
			};
	private static ThreadLocal<List<DomainEvent>> events 
		= new ThreadLocal<List<DomainEvent>>(){
				@Override
				protected List<DomainEvent> initialValue() {
					return new ArrayList<>();
				}
			};
			
	public void publish(DomainEvent event) {
		this.events.get().add(event);
	} 
	
	public void fire() {
		this.events.get().forEach(event -> {
			this.subscribers.get().keySet().forEach(keyType -> {
				if(keyType.isAssignableFrom(event.getClass())){
					List<Subscriber<?>> subscribers = this.subscribers.get().get(keyType);
					subscribers.forEach(s -> {
						((Subscriber<DomainEvent>)s).subscribe(event);
					});
				}
			});
		});
	}
	public <T extends DomainEvent> void register(Class<T> eventType, Subscriber<T> subscriber){
		if(!subscribers.get().containsKey(eventType)){
			subscribers.get().put(eventType, new ArrayList<>());
		}
		subscribers.get().get(eventType).add(subscriber);
	}
	public void initialize(){
		subscribers.remove();
		events.remove();
	}
	
	@FunctionalInterface
	public static interface Subscriber<T extends DomainEvent> {
		void subscribe(T event);
	}
}
