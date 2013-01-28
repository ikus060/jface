/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.forms;

import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.ui.forms.widgets.FormText;

/**
 * Delegating property to access the text field of form widgets.
 * 
 * @author Patrik Dufresne
 * 
 */
public class FormWidgetTextProperty extends FormWidgetDelegatingValueProperty {
	private IValueProperty formtext;

	/**
	 * Create a new instance of this class.
	 */
	public FormWidgetTextProperty() {
		super(String.class);
	}

	/**
	 * This implementation return the delegating property according to the
	 * widget type. This function throws an exception if the widget is not
	 * supported.
	 */
	@Override
	protected IValueProperty doGetDelegate(Object source) {
		if (source instanceof FormText) {
			if (this.formtext == null)
				this.formtext = new FormTextTextProperty();
			return this.formtext;
		}
		throw notSupported(source);
	}
}
