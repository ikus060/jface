/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.IObservableSet;

/**
 * This concrete implementation of {@link ObservableCollectionValueProperty} is
 * specialized to observe {@link IObservableSet}.
 * 
 * @author Patrik Dufresne
 * 
 */
public class ObservableSetValueProperty extends
		ObservableCollectionValueProperty {

	/**
	 * Create a new value property.
	 */
	public ObservableSetValueProperty() {
		super();
	}

	/**
	 * This implementation always return a new instance of {@link HashSet}.
	 */
	@Override
	protected Object doGetValue(final Object source) {
		return new HashSet((IObservableSet) source);
	}

	/**
	 * This implementation clear the content of the {@link IObservableSet} and
	 * replace it with the new value.
	 */
	@Override
	protected void doSetValue(Object source, Object value) {
		((IObservableSet) source).clear();
		if (value instanceof Collection) {
			((IObservableSet) source).addAll((Collection) value);
		}
	}

	/**
	 * This implementation always return <code>Set.class</code>.
	 */
	@Override
	public Object getValueType() {
		return Set.class;
	}

}
