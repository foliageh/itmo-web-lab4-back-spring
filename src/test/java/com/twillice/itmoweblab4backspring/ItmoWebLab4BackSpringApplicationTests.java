package com.twillice.itmoweblab4backspring;

import com.twillice.itmoweblab4backspring.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

// TODO
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ItmoWebLab4BackSpringApplicationTests {
	@Autowired
	private WebTestClient client;
	@Autowired
	private UserService userService;

	@Test
	void testGetShoots() {
		client.get().uri("/my-shots").exchange().expectStatus().isOk();
	}
}
