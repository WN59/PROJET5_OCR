package com.openclassrooms.mddapi.exception;

import com.openclassrooms.mddapi.dto.ErrorResponse;
import com.openclassrooms.mddapi.dto.ErrorResponse.FieldErrorItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Point unique de traduction des exceptions en réponses HTTP JSON.
 * <p>
 * Utilisation côté métier : {@code throw ResourceNotFoundException.of("Topic", id);}
 * → le client reçoit un {@link ErrorResponse} avec {@code status=404}.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/** Ressource absente → 404. */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
		return build(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), request, null);
	}

	/** Échec {@code @Valid} sur le body → 400 + détail par champ. */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
		List<FieldErrorItem> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(this::toFieldError)
				.collect(Collectors.toList());
		return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR",
				"La requête contient des données invalides.", request, errors);
	}

	/** Contraintes Bean Validation hors body (path/query) → 400. */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex, WebRequest request) {
		List<FieldErrorItem> errors = ex.getConstraintViolations().stream()
				.map(v -> FieldErrorItem.builder()
						.field(v.getPropertyPath().toString())
						.message(v.getMessage())
						.build())
				.collect(Collectors.toList());
		return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR",
				"La requête contient des données invalides.", request, errors);
	}

	/** Paramètre manquant, type incorrect, JSON illisible, argument illégal → 400. */
	@ExceptionHandler({
			MissingServletRequestParameterException.class,
			MethodArgumentTypeMismatchException.class,
			HttpMessageNotReadableException.class,
			IllegalArgumentException.class
	})
	public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex, WebRequest request) {
		return build(HttpStatus.BAD_REQUEST, "BAD_REQUEST",
				ex.getMessage() != null ? ex.getMessage() : "Requête incorrecte.", request, null);
	}

	/** Mauvaise méthode HTTP (ex. POST sur une ressource GET-only) → 405. */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMethodNotAllowed(
			HttpRequestMethodNotSupportedException ex, WebRequest request) {
		return build(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED",
				"Méthode HTTP non supportée pour cette ressource.", request, null);
	}

	/** Violation unique / FK → 409. */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException ex, WebRequest request) {
		log.warn("Conflit d'intégrité données", ex);
		return build(HttpStatus.CONFLICT, "CONFLICT",
				"Conflit avec une donnée existante (contrainte unique / FK).", request, null);
	}

	/** MySQL injoignable ou erreur d’accès données → 503. */
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ErrorResponse> handleDataAccess(DataAccessException ex, WebRequest request) {
		log.error("Erreur d'accès base de données", ex);
		return build(HttpStatus.SERVICE_UNAVAILABLE, "DB_ERROR",
				"Impossible d'accéder à la base de données.", request, null);
	}

	/** Filet de sécurité pour toute autre exception → 500. */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, WebRequest request) {
		log.error("Erreur inattendue", ex);
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
				"Une erreur interne est survenue.", request, null);
	}

	private FieldErrorItem toFieldError(FieldError error) {
		return FieldErrorItem.builder()
				.field(error.getField())
				.message(error.getDefaultMessage())
				.build();
	}

	private ResponseEntity<ErrorResponse> build(
			HttpStatus status,
			String code,
			String message,
			WebRequest request,
			List<FieldErrorItem> errors
	) {
		String path = request.getDescription(false).replace("uri=", "");
		ErrorResponse body = ErrorResponse.builder()
				.timestamp(Instant.now())
				.status(status.value())
				.code(code)
				.message(message)
				.path(path)
				.errors(errors)
				.build();
		return ResponseEntity.status(status).body(body);
	}
}
