/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.forms;

import org.eclipse.ui.forms.widgets.FormText;

public class FormTextTextProperty extends FormWidgetStringValueProperty {

	boolean parseTags;
	boolean expandURLs;

	FormTextTextProperty() {
		this(false, false);
	}

	FormTextTextProperty(boolean parseTags, boolean expandURLs) {
		super();
		this.parseTags = parseTags;
		this.expandURLs = expandURLs;
	}

	@Override
	String doGetStringValue(Object source) {
		return ""; //$NON-NLS-1$
	}

	@Override
	void doSetStringValue(Object source, String value) {
		((FormText) source).setText(value == null ? "" : value, this.parseTags, //$NON-NLS-1$
				this.expandURLs);
	}

	@Override
	public String toString() {
		return "FormText.text <String>"; //$NON-NLS-1$
	}
}
