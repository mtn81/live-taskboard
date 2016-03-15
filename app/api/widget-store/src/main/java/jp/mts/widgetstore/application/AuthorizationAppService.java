package jp.mts.widgetstore.application;

import jp.mts.widgetstore.domain.model.WidgetAuthDomainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationAppService {
	
	@Autowired
	private WidgetAuthDomainService widgetAuthDomainService;

	public boolean isAccesible(String authId, String categoryId) {
		return widgetAuthDomainService.isAvailable(authId, categoryId);
	}

}
