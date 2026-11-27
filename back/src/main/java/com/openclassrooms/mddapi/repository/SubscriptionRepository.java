package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Subscription;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Accès JPA à la table {@code subscriptions} (abonnements thèmes).
 */
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

	List<Subscription> findByUser(User user);

	Optional<Subscription> findByUserAndTopic(User user, Topic topic);

	boolean existsByUserAndTopic(User user, Topic topic);
}
