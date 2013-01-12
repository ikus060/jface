/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.dialogs;

import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.jface.databinding.dialog.ValidationMessageProvider;

/**
 * This implementation of ValidationMessageProvider allow the user to specify a
 * default message when everything is ok.
 * 
 * @author Patrik Dufresne
 * 
 */
public class DefaultValidationMessageProvider extends ValidationMessageProvider {

	/**
	 * The default message value.
	 */
	private String defaultMessage;

	/**
	 * Create a new validation message provider with default message.
	 * 
	 * @param defaultMessage
	 */
	public DefaultValidationMessageProvider(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	/**
	 * This implementation return the default message if everything is ok.
	 */
	@Override
	public String getMessage(ValidationStatusProvider statusProvider) {
		String message = super.getMessage(statusProvider);
		if (message == null || message.isEmpty()) {
			return this.defaultMessage;
		}
		return message;
	}

}
