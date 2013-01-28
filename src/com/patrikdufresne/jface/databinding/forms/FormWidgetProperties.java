/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.forms;

import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.ui.forms.widgets.FormText;

/**
 * A factory for creating properties form form widget.
 */
public class FormWidgetProperties {

	/**
	 * Returns a value property for observing the text of a {@link FormText}.
	 * 
	 * @return a value property for observing the text of a {@link FormText}.
	 */
	public static IWidgetValueProperty text() {
		return new FormWidgetTextProperty();
	}

}
