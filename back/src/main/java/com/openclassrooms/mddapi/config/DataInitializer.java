package com.openclassrooms.mddapi.config;

import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Subscription;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initialise des données de démo au premier démarrage (base vide).
 * <p>
 * Crée users ({@code alice}/{@code bob}), thèmes, abonnements, posts et commentaires.
 * Idempotent : si des users existent déjà, le seed est ignoré.
 * </p>
 */
@Configuration
public class DataInitializer {

	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

	/** Mot de passe de démo pour tous les users seed : Password1! */
	public static final String DEMO_PASSWORD = "Password1!";

	/**
	 * Encodeur BCrypt utilisé pour hasher le mot de passe de démo
	 * (et plus tard les inscriptions).
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Exécuté une fois au démarrage de Spring : peuplement initial MySQL.
	 */
	@Bean
	CommandLineRunner seedDatabase(
			UserRepository userRepository,
			TopicRepository topicRepository,
			SubscriptionRepository subscriptionRepository,
			PostRepository postRepository,
			CommentRepository commentRepository,
			PasswordEncoder passwordEncoder
	) {
		return args -> {
			if (userRepository.count() > 0) {
				log.info("Seed ignoré : la base contient déjà des utilisateurs");
				return;
			}

			String hash = passwordEncoder.encode(DEMO_PASSWORD);

			User alice = userRepository.save(User.builder()
					.email("alice@mdd.local")
					.username("alice")
					.passwordHash(hash)
					.build());
			User bob = userRepository.save(User.builder()
					.email("bob@mdd.local")
					.username("bob")
					.passwordHash(hash)
					.build());

			Topic java = topicRepository.save(Topic.builder()
					.name("Java")
					.description("Langage, JVM, Spring et bonnes pratiques Java")
					.build());
			Topic angular = topicRepository.save(Topic.builder()
					.name("Angular")
					.description("SPA TypeScript, composants, RxJS")
					.build());
			Topic devops = topicRepository.save(Topic.builder()
					.name("DevOps")
					.description("Docker, CI/CD, environnements de développement")
					.build());

			subscriptionRepository.save(Subscription.builder().user(alice).topic(java).build());
			subscriptionRepository.save(Subscription.builder().user(alice).topic(angular).build());
			subscriptionRepository.save(Subscription.builder().user(bob).topic(java).build());
			subscriptionRepository.save(Subscription.builder().user(bob).topic(devops).build());

			Post post1 = postRepository.save(Post.builder()
					.title("Démarrer une API avec Spring Boot")
					.content("Quelques conseils pour structurer controllers, services et repositories.")
					.topic(java)
					.author(alice)
					.build());
			Post post2 = postRepository.save(Post.builder()
					.title("Angular Material pour un MVP")
					.content("Réutiliser les composants Material accélère beaucoup l'UI d'un starter.")
					.topic(angular)
					.author(bob)
					.build());

			commentRepository.save(Comment.builder()
					.content("Très utile, merci pour le partage !")
					.post(post1)
					.author(bob)
					.build());
			commentRepository.save(Comment.builder()
					.content("Je confirme pour un MVP pédagogique.")
					.post(post2)
					.author(alice)
					.build());

			log.info("Seed OK — users: alice/bob — mot de passe: {}", DEMO_PASSWORD);
		};
	}
}
