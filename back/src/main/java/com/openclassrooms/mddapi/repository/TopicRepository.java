package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Accès JPA à la table {@code topics}.
 * <p>
 * Les méthodes CRUD ({@code findAll}, {@code findById}, {@code save}…)
 * sont fournies par {@link JpaRepository}.
 * </p>
 */
public interface TopicRepository extends JpaRepository<Topic, Long> {

	/**
	 * Recherche un thème par son nom unique (utile pour le seed / contrôles).
	 */
	Optional<Topic> findByName(String name);
}
