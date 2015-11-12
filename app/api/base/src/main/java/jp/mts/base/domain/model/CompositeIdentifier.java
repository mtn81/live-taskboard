package jp.mts.base.domain.model;

import java.util.Arrays;
import java.util.List;

public class CompositeIdentifier extends Identifier<List<Identifier<?>>>{

	private CompositeIdentifier(Identifier<?>... ids) {
		super(Arrays.asList(ids));
	}
	public static CompositeIdentifier of(Identifier<?>... ids) {
		return new CompositeIdentifier(ids);
	}

}
