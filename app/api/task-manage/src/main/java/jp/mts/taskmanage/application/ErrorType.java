package jp.mts.taskmanage.application;

public enum ErrorType implements jp.mts.base.application.ErrorType{
	MEMBER_NOT_EXIST("e-tm-001", "指定されたメンバーは存在しません"),
	GROUP_NOT_EXIST("e-tm-002", "指定されたグループは存在しません"),
	GROUP_NOT_AVAILABLE("e-tm-003", "指定されたグループは利用できません"),
	GROUP_REMOVE_DISABLED("e-tm-004", "管理者ではないのでグループを削除できません"),
	GROUP_CHANGE_DISABLED("e-tm-004", "管理者ではないのでグループを編集できません"),
	NOT_AUTHENTICATED("e-tm-005", "認証が必要です"),
	JOIN_NOT_EXIST("e-tm-006", "参加申請が見つかりません。"),
	CANNOT_CANCEL_JOIN("e-tm-007", "自分が申請した申請のみキャンセルできます。"),
	CANNOT_ACCEPT_JOIN("e-tm-007", "参加申請グループの管理者ではありません"),
	CANNOT_REJECT_JOIN("e-tm-007", "参加申請グループの管理者ではありません"),
	CANNOT_LEAVE_MEMBER_NORMAL("e-tm-008", "管理者ではないためメンバーを削除できません"),
	CANNOT_LEAVE_MEMBER_OWNER("e-tm-009", "所有者は削除できません"),
	CANNOT_CHANGE_NORMAL_OWNER("e-tm-010", "所有者を一般に変更できません"),
	NOT_AUTHORIZED("e-tm-011", "指定されたデータへのアクセスが許可させていません")
	;
	
	private String errorCode;
	private String message;
	
	private ErrorType(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}
	public String getMessage() {
		return message;
	}
	
}
