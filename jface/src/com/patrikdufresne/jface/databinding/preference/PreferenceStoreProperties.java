/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.preference;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Utility class to create value property for {@link IPreferenceStore}.
 * 
 * @author Patrik Dufresne
 * 
 */
public class PreferenceStoreProperties {

	/**
	 * Create a new value property.
	 * 
	 * @param property
	 *            the property name as define in the preference store to be
	 *            observed.
	 * @param valueType
	 *            the property value type. Must be a primitive data type. If you
	 *            need to convert it use an {@link IConverter}
	 * @return a value property.
	 */
	public static IValueProperty value(String property, Class valueType) {
		return new PreferenceStoreProperty(property, valueType);

	}

}
