package jp.mts.base.infrastructure.jdbc.repository;

import java.lang.reflect.ParameterizedType;

import jp.mts.base.domain.model.DomainEntity;
import jp.mts.base.domain.model.DomainId;

import org.javalite.activejdbc.Model;
import org.springframework.util.ReflectionUtils;

public abstract class AbstractSimpleJdbcDomainRepository<
		I extends DomainId<?>, 
		E extends DomainEntity<I>, 
		M extends Model>
	extends AbstractJdbcDomainRepository<I, E, M>{

	
	public AbstractSimpleJdbcDomainRepository() {
		setTableModelClass((Class<?>) 
				((ParameterizedType)this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[2]);
	}

	@Override
	protected M findModelById(I id) {
		return (M)ReflectionUtils.invokeMethod(findFirstMethod, null, 
				idColumnName() + "=?", 
				new Object[]{id.value()});
	}
	
	protected abstract String idColumnName();

}
