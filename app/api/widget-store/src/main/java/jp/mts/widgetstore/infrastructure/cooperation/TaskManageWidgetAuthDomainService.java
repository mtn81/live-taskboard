package jp.mts.widgetstore.infrastructure.cooperation;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.mts.base.domain.model.DomainObject;
import jp.mts.widgetstore.domain.model.WidgetAuthDomainService;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskManageWidgetAuthDomainService extends DomainObject implements WidgetAuthDomainService {
	
	private Map<String, Date> authMap = new ConcurrentHashMap<>();
	
	@Autowired
	private TaskManageApi taskManageApi;

	@Override
	public boolean isAvailable(String authId, String categoryId) {
		if (expired(authId, categoryId)) {
			String groupId = taskManageApi.loadGroup(authId, categoryId);
			if(categoryId.equals(groupId)){
				addAuthCache(authId, categoryId);
				return true;
			}else {
				return false;
			}
		}
		return true;
	}

	private void addAuthCache(String authId, String categoryId) {
		String key = authId + "@" + categoryId;
		authMap.put(key, calendar.systemDate());
	}

	private boolean expired(String authId, String categoryId) {
		String key = authId + "@" + categoryId;
		if(!authMap.containsKey(key)) return true;
		return calendar.systemDate().after(DateUtils.addMinutes(authMap.get(key), 5));
	}

}
