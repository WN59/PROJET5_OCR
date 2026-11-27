package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Accès JPA à la table {@code posts} (articles).
 */
public interface PostRepository extends JpaRepository<Post, Long> {
}
