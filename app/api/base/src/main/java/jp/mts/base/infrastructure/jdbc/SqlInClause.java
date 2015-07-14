package jp.mts.base.infrastructure.jdbc;

import java.util.List;

public class SqlInClause<P> {

	private String columnName;
	private List<P> params;
	private ParamEncoder<P> paramEncoder;

	public SqlInClause(String columnName, List<P> params, ParamEncoder<P> encorder) {
		this.columnName = columnName;
		this.params = params;
		this.paramEncoder = encorder;
	}
	
	public String condition() {
		StringBuilder sb = new StringBuilder(columnName);
		sb.append(" in (");
		params.forEach(p -> {
			sb.append("?,");
		});
		sb.delete(sb.length() - 1, sb.length());
		sb.append(")");
		return sb.toString();
	}
	
	public Object[] params() {
		return params.stream().map(p -> paramEncoder.encode(p)).toArray();
	}
	
	@FunctionalInterface
	public interface ParamEncoder<T> {
		Object encode(T param);
	}
	
}
