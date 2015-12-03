package jp.mts.taskmanage.rest.aspect;

import java.util.Map;
import java.util.Set;

import jp.mts.taskmanage.application.query.MemberAuthorizationQuery;

public class MemberContext {
	
	private static MemberAuthorizationQuery memberAuthorizationQuery;
	
	private static final ThreadLocal<MemberAuthorization> value = new ThreadLocal<MemberAuthorization>(){
		@Override
		protected MemberAuthorization initialValue() {
			return null;
		}
	};

	public static boolean hasMemberId(String memberId) {
		return value.get().memberId.equals(memberId);
	}

	public static void start(String memberId) {
		value.set(new MemberAuthorization(memberId));
	}

	public static boolean isGroupAdmin(String adminGroupId) {
		Map<String, Boolean> groupAdminMap = loadGroupAdmin();
		return groupAdminMap.containsKey(adminGroupId) ? 
				groupAdminMap.get(adminGroupId) : false;
	}
	public static boolean belongs(String belongGroupId) {
		Map<String, Boolean> groupAdminMap = loadGroupAdmin();
		return groupAdminMap.containsKey(belongGroupId);
	}
	public static boolean applied(String groupJoinId) {
		Set<String> appliedJoinIds = loadAppliedJoins();
		return appliedJoinIds.contains(groupJoinId);
	}

	private static Set<String> loadAppliedJoins() {
		MemberAuthorization memberAuthorization = value.get();
		if (!memberAuthorization.isJoinApplicationLoaded) {
			memberAuthorization.setAppliedJoins(
					memberAuthorizationQuery.joinsByMember(memberAuthorization.memberId));
		}
		return memberAuthorization.appliedJoins;
	}

	private static Map<String, Boolean> loadGroupAdmin() {
		MemberAuthorization memberAuthorization = value.get();
		if (!memberAuthorization.isGroupAdminLoaded) {
			memberAuthorization.setGroupAdminMap(
					memberAuthorizationQuery.belongingByMember(memberAuthorization.memberId));
		}
		return memberAuthorization.groupAdminMap;
	} 

	public static void setMemberAuthorizationQuery(
			MemberAuthorizationQuery memberAuthorizationQuery) {
		MemberContext.memberAuthorizationQuery = memberAuthorizationQuery;
	}
	
	private static class MemberAuthorization {
		private String memberId;

		private Map<String, Boolean> groupAdminMap;
		private boolean isGroupAdminLoaded;

		private Set<String> appliedJoins;
		private boolean isJoinApplicationLoaded;

		public MemberAuthorization(String memberId) {
			this.memberId = memberId;
		}
		public void setGroupAdminMap(Map<String, Boolean> groupAdminMap) {
			this.groupAdminMap = groupAdminMap;
			this.isGroupAdminLoaded = true;
		}
		public void setAppliedJoins(Set<String> appliedJoins) {
			this.appliedJoins = appliedJoins;
			this.isJoinApplicationLoaded = true;
		}
	}


}
