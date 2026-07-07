package com.jwt.tok.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jwt.tok.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse<?>> handleApiException(ApiException ex) {
		return ResponseEntity.badRequest().body(ApiResponse.fail(ex.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {
		ex.printStackTrace(); // logging
		return ResponseEntity
		        .status(HttpStatus.INTERNAL_SERVER_ERROR)
		        .body(ApiResponse.fail("Something went wrong"));
	}
}
