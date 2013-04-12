/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;

/**
 * This concrete implementation of {@link ObservableCollectionValueProperty} is
 * specialized to observe {@link IObservableList}.
 * 
 * @author Patrik Dufresne
 * 
 */
public class ObservableListValueProperty extends
		ObservableCollectionValueProperty {

	/**
	 * Create a new value property.
	 */
	public ObservableListValueProperty() {
		super();
	}

	/**
	 * This implementation always return a new instance of {@link HashSet}.
	 */
	@Override
	protected Object doGetValue(final Object source) {
		return new ArrayList((IObservableList) source);
	}

	/**
	 * This implementation clear the content of the {@link IObservableSet} and
	 * replace it with the new value.
	 */
	@Override
	protected void doSetValue(Object source, Object value) {
		((IObservableList) source).clear();
		if (value instanceof Collection) {
			((IObservableList) source).addAll((Collection) value);
		}
	}

	/**
	 * This implementation always return <code>Set.class</code>.
	 */
	@Override
	public Object getValueType() {
		return List.class;
	}

}
