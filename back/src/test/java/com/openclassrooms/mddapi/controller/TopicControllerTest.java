package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.service.TopicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test de tranche web ({@code @WebMvcTest}) : HTTP → {@link TopicController}.
 * Le {@link TopicService} est mocké (pas de MySQL dans ce test).
 */
@WebMvcTest(TopicController.class)
class TopicControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TopicService topicService;

	@Test
	void listTopics_returnsJsonArray() throws Exception {
		when(topicService.findAll()).thenReturn(List.of(
				TopicDto.builder().id(1L).name("Java").description("JVM").build()
		));

		mockMvc.perform(get("/api/topics"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Java"))
				.andExpect(jsonPath("$[0].description").value("JVM"));
	}
}
