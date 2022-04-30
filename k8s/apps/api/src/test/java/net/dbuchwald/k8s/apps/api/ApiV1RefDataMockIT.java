package net.dbuchwald.k8s.apps.api;

import net.dbuchwald.k8s.apps.api.models.constants.GreetingCode;
import net.dbuchwald.k8s.apps.api.models.constants.LanguageIdentifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiV1RefDataMockIT {

	private static final String REF_DATA_API_V1 = "/api/v1/ref-data";

	@Autowired
	private MockMvc mvc;

	@Test
	@DisplayName("REST endpoint for /languages should return correct values")
	void getLanguagesListShouldWorkCorrectly () throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(REF_DATA_API_V1 + "/languages"))
				.andExpect(status().isOk())
				.andExpect(content().json("[{'langId':'en','description':'English'},{'langId':'pl','description':'Polski'},{'langId':'es','description':'Español'}]"));
	}

	@Test
	@DisplayName("REST endpoint for /language/langId should return correct value")
	void getLanguageShouldWorkCorrectly () throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(REF_DATA_API_V1 + "/language/" + LanguageIdentifier.POLISH))
				.andExpect(status().isOk())
				.andExpect(content().json("{'langId':'pl','description':'Polski'}"));
	}

	@Test
	@DisplayName("REST endpoint for /language/langId should return BAD REQUEST for invalid value")
	void getLanguageShouldWorkCorrectlyForInvalidValue () throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(REF_DATA_API_V1 + "/language/ru"))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("REST endpoint for /greeting/langId/code should return correct value")
	void getGreetingShouldWorkCorrectly () throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(REF_DATA_API_V1 + "/greeting/" + LanguageIdentifier.ENGLISH + "/" + GreetingCode.HELLO))
				.andExpect(status().isOk())
				.andExpect(content().json("{greetingId:{'langId':'en','code':'hello'},'text':'Hello!'}"));
	}

	@Test
	@DisplayName("REST endpoint for /greeting/langId/code should return NOT FOUND for invalid value")
	void getGreetingShouldWorkCorrectlyForInvalidValue () throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(REF_DATA_API_V1 + "/greeting/ru/hello"))
				.andExpect(status().isNotFound());
	}
}
