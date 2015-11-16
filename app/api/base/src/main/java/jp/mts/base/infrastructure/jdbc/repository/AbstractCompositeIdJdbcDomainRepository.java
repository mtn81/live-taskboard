package jp.mts.base.infrastructure.jdbc.repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import jp.mts.base.domain.model.CompositeId;
import jp.mts.base.domain.model.DomainEntity;

import org.javalite.activejdbc.Model;
import org.springframework.util.ReflectionUtils;

public abstract class AbstractCompositeIdJdbcDomainRepository<
	E extends DomainEntity<CompositeId>, 
	M extends Model>
	extends AbstractJdbcDomainRepository<CompositeId, E, M>{

	public AbstractCompositeIdJdbcDomainRepository() {
		setTableModelClass((Class<?>) 
				((ParameterizedType)this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[1]);
	}

	@Override
	protected M findModelById(CompositeId id) {
		final StringBuilder subquery = new StringBuilder();
		idColumnNames().forEach(idColumnName -> {
			subquery.append(idColumnName);
			subquery.append("=? and ");
		});
		
		return (M)ReflectionUtils.invokeMethod(findFirstMethod, null, 
				subquery.replace(subquery.length() - 5, subquery.length(), "").toString(), 
				id.values().toArray());
	}

	protected abstract List<String> idColumnNames();
}
