package jp.mts.taskmanage.infrastructure.jdbc.repository;

import org.springframework.stereotype.Repository;

import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.MemberRepository;
import jp.mts.taskmanage.infrastructure.jdbc.model.MemberModel;

@Repository
public class JdbcMemberRepository implements MemberRepository {

	@Override
	public Member findById(MemberId memberId) {
		MemberModel model = MemberModel.findFirst("member_id = ?", memberId.value());
		if(model == null){
			return null;
		}
		return new Member(new MemberId(model.getString("member_id")));
	}

	@Override
	public void save(Member member) {
		MemberModel model = MemberModel.findFirst("member_id = ?", member.memberId().value());
		if(model == null){
			model = new MemberModel();
		}

		model.set("member_id", member.memberId().value());
		model.saveIt();
	}

}
