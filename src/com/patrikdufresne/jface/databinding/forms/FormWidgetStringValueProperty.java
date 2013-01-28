/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.forms;

import org.eclipse.jface.databinding.swt.WidgetValueProperty;

/**
 * Abstract class to eases the implementation of a property returning a string
 * value.
 * <p>
 * Subclasses must implement the abstract function
 * {@link #doGetStringValue(Object)} and
 * {@link #doSetStringValue(Object, String)}.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class FormWidgetStringValueProperty extends WidgetValueProperty {

	FormWidgetStringValueProperty() {
		super();
	}

	FormWidgetStringValueProperty(int event) {
		super(event);
	}

	FormWidgetStringValueProperty(int[] events) {
		super(events);
	}

	FormWidgetStringValueProperty(int[] events, int[] staleEvents) {
		super(events, staleEvents);
	}

	@Override
	public Object getValueType() {
		return String.class;
	}

	@Override
	protected Object doGetValue(Object source) {
		return doGetStringValue(source);
	}

	@Override
	protected void doSetValue(Object source, Object value) {
		doSetStringValue(source, (String) value);
	}

	abstract String doGetStringValue(Object source);

	abstract void doSetStringValue(Object source, String value);
}
