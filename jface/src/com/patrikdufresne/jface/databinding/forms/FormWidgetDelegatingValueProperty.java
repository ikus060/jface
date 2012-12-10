/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.forms;

import org.eclipse.core.databinding.property.value.DelegatingValueProperty;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Widget;

abstract class FormWidgetDelegatingValueProperty extends
		DelegatingValueProperty implements IWidgetValueProperty {
	RuntimeException notSupported(Object source) {
		return new IllegalArgumentException(
				"Widget [" + source.getClass().getName() + "] is not supported."); //$NON-NLS-1$//$NON-NLS-2$
	}

	public FormWidgetDelegatingValueProperty() {
	}

	public FormWidgetDelegatingValueProperty(Object valueType) {
		super(valueType);
	}

	public ISWTObservableValue observe(Widget widget) {
		return (ISWTObservableValue) observe(
				SWTObservables.getRealm(widget.getDisplay()), widget);
	}

	public ISWTObservableValue observeDelayed(int delay, Widget widget) {
		return SWTObservables.observeDelayedValue(delay, observe(widget));
	}
}
