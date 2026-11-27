package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Accès JPA à la table {@code comments}.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

	/** Commentaires d’un article, du plus ancien au plus récent. */
	List<Comment> findByPostOrderByCreatedAtAsc(Post post);
}
