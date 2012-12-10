/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.preference;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.NativePropertyListener;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * This implementation of {@link IValueProperty} allow to observe a preference
 * value from a {@link IPreferenceStore}.
 * 
 * @author Patrik Dufresne
 * 
 */
public class PreferenceStoreProperty extends SimpleValueProperty {

	/**
	 * Adapt listener
	 * 
	 * @author Patrik Dufresne
	 * 
	 */
	private class Listener extends NativePropertyListener implements
			IPropertyChangeListener {

		public Listener(ISimplePropertyListener listener) {
			super(PreferenceStoreProperty.this, listener);
		}

		@Override
		protected void doAddTo(Object model) {
			((IPreferenceStore) model).addPropertyChangeListener(this);
		}

		@Override
		protected void doRemoveFrom(Object model) {
			((IPreferenceStore) model).removePropertyChangeListener(this);
		}

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getProperty().equals(
					PreferenceStoreProperty.this.property)) {
				fireChange(
						event.getSource(),
						Diffs.createValueDiff(event.getOldValue(),
								event.getNewValue()));
			}
		}
	}

	private Method getterMethod;
	final String property;
	private Method setterMethod;
	private Class valueType;

	/**
	 * Create a new preference value property.
	 * 
	 * @param property
	 *            the property name as define in the preference store
	 * @param valueType
	 *            the property type. Must be a primitive data type.
	 */
	public PreferenceStoreProperty(String property, Class valueType) {
		this.property = property;
		this.valueType = valueType;
		try {
			String getterName = getGetterName();
			this.getterMethod = IPreferenceStore.class.getMethod(getterName,
					new Class[] { String.class });
			this.valueType = this.getterMethod.getReturnType();
			this.setterMethod = IPreferenceStore.class.getMethod(
					getSetterName(), new Class[] { String.class, valueType });
		} catch (SecurityException e) {
			throw new IllegalArgumentException();
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException();
		}

	}

	/**
	 * This implementation create a new private listener.
	 */
	@Override
	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		return new Listener(listener);
	}

	/**
	 * This implementation call the getter method.
	 */
	@Override
	protected Object doGetValue(Object model) {
		if (model instanceof IPreferenceStore) {
			try {
				return this.getterMethod.invoke(model, this.property);
			} catch (InvocationTargetException e) {
				throw new IllegalStateException(e.getMessage());
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * This implementation call the setter method.
	 */
	@Override
	protected void doSetValue(Object model, Object value) {
		if (model instanceof IPreferenceStore) {
			try {
				this.setterMethod.invoke(model, this.property, value);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e.getMessage());
			} catch (InvocationTargetException e) {
				throw new IllegalStateException(e.getMessage());
			}
		}
	}

	/**
	 * Return the getter method name according to value type.
	 * 
	 * @return
	 */
	protected String getGetterName() {
		if (this.valueType.equals(Double.TYPE)) {
			return "getDouble"; //$NON-NLS-1$
		} else if (this.valueType.equals(Float.TYPE)) {
			return "getFloat"; //$NON-NLS-1$
		} else if (this.valueType.equals(Integer.TYPE)) {
			return "getInt"; //$NON-NLS-1$
		} else if (this.valueType.equals(Long.TYPE)) {
			return "getLong"; //$NON-NLS-1$
		} else if (this.valueType.equals(String.class)) {
			return "getString"; //$NON-NLS-1$
		}
		throw new IllegalArgumentException("unsupported value type"); //$NON-NLS-1$

	}

	/**
	 * This implementation always return 'setValue'.
	 * 
	 * @return
	 */
	protected String getSetterName() {
		return "setValue"; //$NON-NLS-1$
	}

	@Override
	public Object getValueType() {
		return this.valueType;
	}

}
