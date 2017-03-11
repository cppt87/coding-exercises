package com.ryanair.apis.exceptions;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ryanair.apis.models.Error;
import com.ryanair.apis.models.ErrorResource;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = { ConstraintViolationException.class })
	@ResponseBody
	public ResponseEntity<ErrorResource> handleBadRequestException(ConstraintViolationException e) {
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		ErrorResource resource = new ErrorResource();
		Set<Error> errorList = new LinkedHashSet<Error>(violations.size());
		for (ConstraintViolation<?> violation : violations) {
			Error error = new Error();
			error.setStatus(HttpStatus.BAD_REQUEST.toString());
			error.setSource(violation.getRootBean().getClass().getName());
			error.setTitle(e.toString());
			error.setDetail(String.format(violation.getMessage(), violation.getInvalidValue()));
			errorList.add(error);
		}

		resource.setErrors(errorList);
		return new ResponseEntity<ErrorResource>(resource, HttpStatus.BAD_REQUEST);
	}
}
