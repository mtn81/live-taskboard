package jp.mts.base.domain.model;

import java.util.Optional;

public interface DomainRepository<I extends Identifier<?>, E extends DomainEntity<I>> {

	Optional<E> findById(I id);
	void save(E entity);
	void remove(E entity);
}
