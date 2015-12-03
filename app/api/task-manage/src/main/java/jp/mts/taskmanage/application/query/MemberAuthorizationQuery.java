package jp.mts.taskmanage.application.query;

import java.util.Map;
import java.util.Set;

public interface MemberAuthorizationQuery {

	Map<String, Boolean> belongingByMember(String string);

	Set<String> joinsByMember(String memberId);

}
