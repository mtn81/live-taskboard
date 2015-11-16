package jp.mts.base.domain.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CompositeId extends DomainId<List<DomainId<?>>>{

	private CompositeId(DomainId<?>... ids) {
		super(Arrays.asList(ids));
	}
	public static CompositeId of(DomainId<?>... ids) {
		return new CompositeId(ids);
	}
	
	public List<Object> values() {
		return value().stream()
				.map(v -> v.value())
				.collect(Collectors.toList());
	}

}
