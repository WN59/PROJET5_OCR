package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.service.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API REST des thèmes (topics).
 * <p>
 * Première action bout-en-bout (Feature/4) pour valider la chaîne
 * front → controller → service → repository → MySQL.
 * </p>
 */
@RestController
@RequestMapping("/api/topics")
public class TopicController {

	private final TopicService topicService;

	public TopicController(TopicService topicService) {
		this.topicService = topicService;
	}

	/**
	 * Liste tous les thèmes présents en base.
	 *
	 * @return {@code 200 OK} avec la liste JSON des {@link TopicDto}
	 *         (vide si aucun thème). Les erreurs DB remontent via
	 *         {@link com.openclassrooms.mddapi.exception.GlobalExceptionHandler}.
	 */
	@GetMapping
	public ResponseEntity<List<TopicDto>> listTopics() {
		return ResponseEntity.ok(topicService.findAll());
	}
}
