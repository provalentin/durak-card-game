package com.smartgames.client;

import java.io.Serializable;

public class NotLoggedInException extends Exception implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = -3633306318737407447L;

public NotLoggedInException() {
    super();
  }

  public NotLoggedInException(String message) {
    super(message);
  }

}