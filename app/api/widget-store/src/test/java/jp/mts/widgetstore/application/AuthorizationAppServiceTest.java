package jp.mts.widgetstore.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.widgetstore.domain.model.WidgetAuthDomainService;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

public class AuthorizationAppServiceTest {
	
	@Tested AuthorizationAppService target = new AuthorizationAppService();
	@Injectable WidgetAuthDomainService widgetAuthDomainService;

	@Test
	public void test() {
		
		new Expectations() {{
			widgetAuthDomainService.isAvailable("a01", "g01");
				result = true;
		}};
		
		boolean actual = target.isAccesible("a01", "g01");
		
		assertThat(actual, is(true));
	}

}
