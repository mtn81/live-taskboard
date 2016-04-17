package jp.mts.taskmanage.rest.aspect;

import jp.mts.taskmanage.application.MemberAppService;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.member.Member;

public class MemberContext {
	
	private static MemberAppService memberAppService;
	
	private static final ThreadLocal<MemberAuthorization> value = new ThreadLocal<MemberAuthorization>(){
		@Override
		protected MemberAuthorization initialValue() {
			return null;
		}
	};

	public static String memberId() {
		return value.get().memberId;
	}
	public static boolean hasMemberId(String memberId) {
		return value.get().memberId.equals(memberId);
	}

	public static void start(String memberId) {
		value.set(new MemberAuthorization(memberId));
	}

	public static boolean isGroupAdmin(String adminGroupId) {
		Member member = loadMember();
		return member.belongsAsAdmin(new GroupId(adminGroupId));
	}
	public static boolean belongs(String belongGroupId) {
		Member member = loadMember();
		return member.belongsTo(new GroupId(belongGroupId));
	}

	private static Member loadMember() {
		return value.get().loadMember(memberAppService);
	} 

	public static void setMemberAppService(MemberAppService memberAppService) {
		MemberContext.memberAppService = memberAppService;
	}

	private static class MemberAuthorization {
		private String memberId;
		private Member member;
		private boolean isMemberLoaded;

		public MemberAuthorization(String memberId) {
			this.memberId = memberId;
		}
		public Member loadMember(MemberAppService memberAppService) {
			if (!isMemberLoaded) {
				this.member = memberAppService.findById(memberId);
				this.isMemberLoaded = true;
			}
			return this.member;
		}
	}


}
