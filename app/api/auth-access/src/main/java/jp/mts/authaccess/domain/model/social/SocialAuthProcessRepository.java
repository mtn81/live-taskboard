package jp.mts.authaccess.domain.model.social;

import java.util.Optional;

public interface SocialAuthProcessRepository {

	public SocialAuthProcessId newAuthProcessId();

	public void save(SocialAuthProcess authProcess);

	public Optional<SocialAuthProcess> findById(SocialAuthProcessId authProcessId);

	public void remove(SocialAuthProcess socialAuthProcess);
}
