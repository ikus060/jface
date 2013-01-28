/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.collections;

import java.util.HashSet;

import org.eclipse.core.databinding.observable.IObservableCollection;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.property.value.IValueProperty;

/**
 * Utility class to create value property for {@link IObservableCollection}
 * 
 * @author Patrik Dufresne
 * 
 */
public class CollectionProperties {

	/**
	 * Private constructor to avoid creating a utility class.
	 */
	private CollectionProperties() {
		// Nothing to do
	}

	/**
	 * This value property return the observed {@link IObservableSet} as a new
	 * instance of {@link HashSet}.
	 * 
	 * @return the value property.
	 */
	public static IValueProperty set() {
		return new ObservableSetValueProperty();
	}

}
