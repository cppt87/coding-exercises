package com.buddybank.api.exceptions;

import org.restlet.data.Status;

public class ClientErrorBadRequestException extends RestletApplicationException {

  private static final long serialVersionUID = -4111232114040763274L;

  public ClientErrorBadRequestException(String message) {
    super(message);
    status = Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY;
  }

  public ClientErrorBadRequestException(Throwable t) {
    super("raise 400 bad request", t);
    status = Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY;
  }

  public ClientErrorBadRequestException() {
    super();
  }

}
