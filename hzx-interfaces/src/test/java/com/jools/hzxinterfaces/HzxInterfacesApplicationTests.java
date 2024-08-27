package com.jools.hzxinterfaces;

import com.jools.joolsclientsdk.JoolsClientConfig;
import com.jools.joolsclientsdk.client.JoolsHttpClient;
import com.jools.joolsclientsdk.model.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(JoolsClientConfig.class)
class HzxInterfacesApplicationTests {

	@Resource
	private JoolsHttpClient joolsHttpClient;

	@Test
	void contextLoads() {
		joolsHttpClient.testGetRequest();
		joolsHttpClient.testPostRequest("Jools Wakoo");

		User user = new User();
		user.setName("Jools HHH");
		joolsHttpClient.testModelPost(user);
	}

}
