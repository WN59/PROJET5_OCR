package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

/**
 * Corps JSON homogène renvoyé par {@code GlobalExceptionHandler} en cas d’erreur.
 * <p>
 * Exemple : {@code {"status":404,"code":"NOT_FOUND","message":"...","path":"/api/topics/99"}}.
 * </p>
 */
@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {

	private final Instant timestamp;
	private final int status;
	private final String code;
	private final String message;
	private final String path;
	/** Détails optionnels (ex. erreurs de validation par champ). */
	private final List<FieldErrorItem> errors;

	/**
	 * Erreur de validation rattachée à un champ du body / des query params.
	 */
	@Getter
	@AllArgsConstructor
	@Builder
	public static class FieldErrorItem {
		private final String field;
		private final String message;
	}
}
