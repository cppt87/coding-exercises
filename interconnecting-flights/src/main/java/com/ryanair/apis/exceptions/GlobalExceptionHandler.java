package com.ryanair.apis.exceptions;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.ryanair.apis.models.Error;
import com.ryanair.apis.models.ErrorResource;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = { ConstraintViolationException.class })
	@ResponseBody
	public ResponseEntity<ErrorResource> handleBadRequestException(ConstraintViolationException e) {
		LOGGER.error("Exception caught: ", e);
		return new ResponseEntity<ErrorResource>(this.errorBuilder(e), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
	@ResponseBody
	public ResponseEntity<ErrorResource> handleBadRequestException(MethodArgumentTypeMismatchException e) {
		LOGGER.error("Exception caught: ", e);
		return new ResponseEntity<ErrorResource>(this.errorBuilder(e), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { BadRequestException.class })
	@ResponseBody
	public ResponseEntity<ErrorResource> handleBadRequestException(BadRequestException e) {
		LOGGER.error("Exception caught: ", e);
		return new ResponseEntity<ErrorResource>(this.errorBuilder(e), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { NotFoundException.class })
	@ResponseBody
	public ResponseEntity<ErrorResource> handleNotFoundException(NotFoundException e) {
		LOGGER.error("Exception caught: ", e);
		return new ResponseEntity<ErrorResource>(this.errorBuilder(e), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { RequestRangeNotSatisfiableException.class })
	@ResponseBody
	public ResponseEntity<ErrorResource> handleRequestRangeNotSatisfiableException(
			RequestRangeNotSatisfiableException e) {
		LOGGER.error("Exception caught: ", e);
		return new ResponseEntity<ErrorResource>(this.errorBuilder(e), HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
	}

	@ExceptionHandler(value = { ServiceUnavailableException.class })
	@ResponseBody
	public ResponseEntity<ErrorResource> handleServiceUnavailableException(ServiceUnavailableException e) {
		LOGGER.error("Exception caught: ", e);
		return new ResponseEntity<ErrorResource>(this.errorBuilder(e), HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(value = { Exception.class })
	@ResponseBody
	public ResponseEntity<ErrorResource> handleInternalServerErrorException(Exception e) {
		LOGGER.error("Exception caught: ", e);
		return new ResponseEntity<ErrorResource>(this.errorBuilder(e), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ErrorResource errorBuilder(Exception e) {
		ErrorResource resource = new ErrorResource();
		Error error;
		Set<Error> errorList;
		if (e instanceof ConstraintViolationException) {
			Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) e).getConstraintViolations();
			errorList = new LinkedHashSet<Error>(violations.size());
			for (ConstraintViolation<?> violation : violations) {
				error = new Error();
				error.setTitle(e.getClass().getName());
				error.setDetail(e.getMessage());
				error.setSource(violation.getRootBean().getClass().getName());
				error.setDetail(String.format(violation.getMessage(), violation.getInvalidValue()));
				error.setStatus(HttpStatus.BAD_REQUEST.toString());
				errorList.add(error);
			}
		} else {
			errorList = new LinkedHashSet<Error>(1);
			error = new Error();
			error.setTitle(e.getClass().getName());
			error.setDetail(e.getMessage());

			if (e instanceof BadRequestException)
				error.setStatus(HttpStatus.BAD_REQUEST.toString());
			else if (e instanceof MethodArgumentTypeMismatchException)
				error.setStatus(HttpStatus.BAD_REQUEST.toString());
			else if (e instanceof NotFoundException)
				error.setStatus(HttpStatus.NOT_FOUND.toString());
			else if (e instanceof RequestRangeNotSatisfiableException)
				error.setStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.toString());
			else if (e instanceof ServiceUnavailableException)
				error.setStatus(HttpStatus.SERVICE_UNAVAILABLE.toString());
			else
				error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());

			errorList.add(error);
		}
		resource.setErrors(errorList);
		return resource;
	}
}
