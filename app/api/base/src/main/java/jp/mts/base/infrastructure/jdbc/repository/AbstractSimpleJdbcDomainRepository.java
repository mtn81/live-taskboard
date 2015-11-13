package jp.mts.base.infrastructure.jdbc.repository;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.mts.base.domain.model.DomainEntity;
import jp.mts.base.domain.model.DomainRepository;
import jp.mts.base.domain.model.Identifier;

import org.javalite.activejdbc.Model;
import org.springframework.util.ReflectionUtils;

public abstract class AbstractSimpleJdbcDomainRepository<
		I extends Identifier<?>, 
		E extends DomainEntity<I>, 
		M extends Model>
	implements DomainRepository<I, E>{

	private Class<?> tableModelClass;
	private Method findFirstMethod;
	private Method findMethod;
	
	public AbstractSimpleJdbcDomainRepository() {
		
		try {
			this.tableModelClass = (Class<?>) ((ParameterizedType)this.getClass().getGenericSuperclass())
						.getActualTypeArguments()[2];
			this.findFirstMethod = tableModelClass.getMethod("findFirst", String.class, Object[].class);
			this.findMethod = tableModelClass.getMethod("find", String.class, Object[].class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<E> findById(I id) {
		M model = findModelById(id.value());
		if (model == null) return Optional.empty();
		return Optional.of(toDomain(model));
	}

	@Override
	public void save(E entity) {
		M model = findModelById(entity.id().value());
		if (model == null) {
			try {
				model = (M)tableModelClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		toModel(model, entity).saveIt();
	}
	
	protected List<E> findList(String subquery, Object... params) {
		List<M> models = (List<M>) ReflectionUtils.invokeMethod(findMethod, null, subquery, params);
		return models.stream()
				.map(model -> toDomain(model))
				.collect(Collectors.toList());
	}
	
	private M findModelById(Object idValue) {
		return (M)ReflectionUtils.invokeMethod(findFirstMethod, null, idColumnName() + "=?", new Object[]{idValue});
	}
	
	protected abstract String idColumnName();
	protected abstract E toDomain(M model);
	protected abstract M toModel(M model, E endity);

}
