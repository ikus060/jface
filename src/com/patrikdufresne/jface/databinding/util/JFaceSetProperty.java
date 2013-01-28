package com.patrikdufresne.jface.databinding.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.SetDiff;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.NativePropertyListener;
import org.eclipse.core.databinding.property.set.SimpleSetProperty;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * Class that supports the use of {@link IObservableList} with objects that
 * follow standard bean method naming conventions but notify an
 * {@link IPropertyChangeListener} when the property changes.
 * <p>
 * This class is an adaptation of the JFaceProperty to support
 * {@link IObservableList} instead of {@link IObservableValue}.
 */
public class JFaceSetProperty extends SimpleSetProperty {

	/**
	 * Private listener implementation.
	 * 
	 * @author Patrik Dufresne
	 * 
	 */
	private class Listener extends NativePropertyListener implements
			IPropertyChangeListener {

		/**
		 * Create a an listener apdater
		 * 
		 * @param listener
		 */
		public Listener(ISimplePropertyListener listener) {
			super(JFaceSetProperty.this, listener);
		}

		protected void doAddTo(Object model) {
			try {
				addPropertyListenerMethod.invoke(model, new Object[] { this });
			} catch (Exception e) {
				throw new IllegalStateException(e.getMessage());
			}
		}

		protected void doRemoveFrom(Object model) {
			try {
				removePropertyListenerMethod.invoke(model,
						new Object[] { this });
			} catch (Exception e) {
				throw new IllegalStateException(e.getMessage());
			}
		}

		/**
		 * This implementation fire a change event.
		 */
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getProperty().equals(JFaceSetProperty.this.property)) {
				fireChange(event.getSource(), null);
			}
		}

	}

	private static String getGetterName(String fieldName) {
		return "get" + toMethodSuffix(fieldName); //$NON-NLS-1$
	}

	private static String getSetterName(String fieldName) {
		return "set" + toMethodSuffix(fieldName); //$NON-NLS-1$
	}

	private static String toMethodSuffix(String fieldName) {
		if (Character.isLowerCase(fieldName.charAt(0))) {
			return Character.toUpperCase(fieldName.charAt(0))
					+ fieldName.substring(1);
		}
		return fieldName;
	}

	/**
	 * Method used to add listener.
	 */
	private Method addPropertyListenerMethod;

	/**
	 * Method to get the list value.
	 */
	private Method getterMethod;

	/**
	 * The property key fire on change.
	 */
	private final String property;

	/**
	 * Method used to remove listener.
	 */
	private Method removePropertyListenerMethod;

	/**
	 * The return type of this property, should be a subclass of {@link List}.
	 */
	private Class returnType;

	/**
	 * Method to set the list value.
	 */
	private Method setterMethod;

	private Class elementType;

	/**
	 * @param fieldName
	 *            the field name i.e.: the function name without get/set
	 *            keywork.
	 * @param property
	 *            the property key fired
	 * @param cls
	 *            the class
	 * @param elementType
	 *            the element type contain in the set or null
	 * @throws IllegalArgumentException
	 *             if the return type of the property is not a subclass of
	 *             {@link List}
	 */
	public JFaceSetProperty(String fieldName, String property, Class cls,
			Class elementType) {
		if (fieldName == null || property == null || cls == null) {
			throw new IllegalArgumentException();
		}
		this.property = property;
		this.elementType = elementType;
		// Create all the necessary method ahead of time to ensure they are
		// available
		try {
			String getterName = getGetterName(fieldName);
			getterMethod = cls.getMethod(getterName, new Class[] {});
			returnType = getterMethod.getReturnType();
			// Make sure the return type os subclass of List
			if (!List.class.isAssignableFrom(returnType)) {
				throw new IllegalArgumentException("return type is not List");
			}
			setterMethod = cls.getMethod(getSetterName(fieldName),
					new Class[] { returnType });
			addPropertyListenerMethod = cls
					.getMethod(
							"addPropertyChangeListener", new Class[] { IPropertyChangeListener.class }); //$NON-NLS-1$
			removePropertyListenerMethod = cls
					.getMethod(
							"removePropertyChangeListener", new Class[] { IPropertyChangeListener.class }); //$NON-NLS-1$
		} catch (SecurityException e) {
			throw new IllegalArgumentException();
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		return new Listener(listener);
	}

	/**
	 * This implementation get the list object using java reflection.
	 */
	@Override
	protected Set doGetSet(Object source) {
		try {
			Object obj = getterMethod.invoke(source, new Object[] {});
			if (obj instanceof Set) {
				return (Set) obj;
			}
			return Collections.EMPTY_SET;
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * This implementation set the list value using java reflection.
	 */
	@Override
	protected void doSetSet(Object source, Set list, SetDiff diff) {
		try {
			setterMethod.invoke(source, new Object[] { list });
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Override
	public Object getElementType() {
		return this.elementType != null ? this.elementType : Object.class;
	}

}
