package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.entity.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Représentation API d’un thème (ce que le front consomme).
 * <p>
 * On n’expose pas l’entité JPA {@link Topic} telle quelle
 * (meilleure isolation, évolution du modèle sans casser le contrat JSON).
 * </p>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicDto {

	private Long id;
	private String name;
	private String description;

	/**
	 * Convertit une entité persistée en DTO de sortie.
	 *
	 * @param topic entité issue du repository (non {@code null})
	 * @return DTO prêt à être sérialisé en JSON
	 */
	public static TopicDto fromEntity(Topic topic) {
		return TopicDto.builder()
				.id(topic.getId())
				.name(topic.getName())
				.description(topic.getDescription())
				.build();
	}
}
