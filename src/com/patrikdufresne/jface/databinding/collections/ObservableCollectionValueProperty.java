/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.collections;

import java.util.Collection;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IObservableCollection;
import org.eclipse.core.databinding.observable.IStaleListener;
import org.eclipse.core.databinding.observable.StaleEvent;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.NativePropertyListener;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;

/**
 * This abstract value property may be used to observe an observable collection.
 * Subclasses should be specialized implementation to observe either an
 * {@link IObservableList} or an {@link IObservableSet}.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class ObservableCollectionValueProperty extends
		SimpleValueProperty {

	/**
	 * Private interface to implement interface we don't want to exposed
	 * publicly.
	 * 
	 * @author Patrik Dufresne
	 * 
	 */
	private class PrivateListener extends NativePropertyListener implements
			IChangeListener, IStaleListener {

		PrivateListener(IProperty property, ISimplePropertyListener listener) {
			super(property, listener);
		}

		@Override
		public void handleStale(StaleEvent event) {
			fireStale(event.getObservable());
		}

		/**
		 * This implementation add a change listener.
		 */
		@Override
		protected void doAddTo(Object source) {
			IObservableCollection observable = (IObservableCollection) source;
			observable.addChangeListener(this);
			observable.addStaleListener(this);
		}

		/**
		 * This implementation remove the change listener.
		 */
		@Override
		protected void doRemoveFrom(Object source) {
			IObservableCollection observable = (IObservableCollection) source;
			observable.removeChangeListener(this);
			observable.removeStaleListener(this);
		}

		/**
		 * This implementation fire a change.
		 */
		@Override
		public void handleChange(ChangeEvent event) {
			// Fire a change without a IDiff. At this point it's hard to compute
			// a IDiff of the collection, since the previous value of the
			// IObservableCollection is not available.
			fireChange(event.getObservable(), null);
		}

	}

	/**
	 * Default constructor.
	 */
	public ObservableCollectionValueProperty() {
		super();
	}

	/**
	 * This implementation always return <code> Collection.class</code>.
	 */
	@Override
	public Object getValueType() {
		return Collection.class;
	}

	/**
	 * This implementation create a private listener to adapt the
	 * {@link ISimplePropertyListener}.
	 */
	@Override
	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		return new PrivateListener(this, listener);
	}

}
