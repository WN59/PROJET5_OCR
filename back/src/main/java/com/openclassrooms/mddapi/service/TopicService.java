package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Règles métier liées aux thèmes.
 * <p>
 * Isolé du controller : la couche web ne parle qu’à ce service,
 * qui lui-même s’appuie sur {@link TopicRepository}.
 * </p>
 */
@Service
public class TopicService {

	private final TopicRepository topicRepository;

	public TopicService(TopicRepository topicRepository) {
		this.topicRepository = topicRepository;
	}

	/**
	 * Récupère tous les thèmes et les mappe en DTO (pas d’entité JPA exposée).
	 *
	 * @return liste de {@link TopicDto} (jamais {@code null})
	 */
	@Transactional(readOnly = true)
	public List<TopicDto> findAll() {
		return topicRepository.findAll().stream()
				.map(TopicDto::fromEntity)
				.collect(Collectors.toList());
	}
}
