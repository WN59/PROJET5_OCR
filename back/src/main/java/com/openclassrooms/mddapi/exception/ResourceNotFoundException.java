package com.openclassrooms.mddapi.exception;

/**
 * Ressource absente → traduite en HTTP 404 par {@link GlobalExceptionHandler}.
 * <p>
 * Exemple d’usage dans un service :
 * {@code throw ResourceNotFoundException.of("Topic", id);}
 * </p>
 */
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String message) {
		super(message);
	}

	/**
	 * Fabrique un message standard {@code "<resource> introuvable (id=...)"}.
	 *
	 * @param resource nom métier (Topic, User, Post…)
	 * @param id       identifiant recherché
	 */
	public static ResourceNotFoundException of(String resource, Object id) {
		return new ResourceNotFoundException(resource + " introuvable (id=" + id + ").");
	}
}
