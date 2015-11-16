package jp.mts.base.infrastructure.jdbc.repository;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.mts.base.domain.model.DomainEntity;
import jp.mts.base.domain.model.DomainRepository;
import jp.mts.base.domain.model.DomainId;

import org.javalite.activejdbc.Model;
import org.springframework.util.ReflectionUtils;

public abstract class AbstractJdbcDomainRepository<
		I extends DomainId<?>, 
		E extends DomainEntity<I>, 
		M extends Model>
	implements DomainRepository<I, E>{

	protected Class<?> tableModelClass;
	protected Method findFirstMethod;
	protected Method findMethod;
	
	protected void setTableModelClass(Class<?> tableModelClass) {
		try {
			this.tableModelClass = tableModelClass;
			this.findFirstMethod = tableModelClass.getMethod("findFirst", String.class, Object[].class);
			this.findMethod = tableModelClass.getMethod("find", String.class, Object[].class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<E> findById(I id) {
		M model = findModelById(id);
		if (model == null) return Optional.empty();
		return Optional.of(toDomain(model));
	}

	@Override
	public void save(E entity) {
		M model = findModelById(entity.id());
		if (model == null) {
			try {
				model = (M)tableModelClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		toModel(model, entity);
		model.saveIt();
	}
	
	@Override
	public void remove(E entity) {
		M model = findModelById(entity.id());
		model.delete();
	}
	
	protected List<E> findList(String subquery, Object... params) {
		List<M> models = (List<M>) ReflectionUtils.invokeMethod(findMethod, null, subquery, params);
		return models.stream()
				.map(model -> toDomain(model))
				.collect(Collectors.toList());
	}
	
	protected abstract M findModelById(I idValue);
	protected abstract E toDomain(M model);
	protected abstract void toModel(M model, E endity);

}
