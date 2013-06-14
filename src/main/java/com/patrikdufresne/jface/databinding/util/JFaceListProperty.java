/**
 * Copyright(C) 2013 Patrik Dufresne Service Logiciel <info@patrikdufresne.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrikdufresne.jface.databinding.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.NativePropertyListener;
import org.eclipse.core.databinding.property.list.SimpleListProperty;
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
public class JFaceListProperty extends SimpleListProperty {

    /**
     * Private listener implementation.
     * 
     * @author Patrik Dufresne
     * 
     */
    private class Listener extends NativePropertyListener implements IPropertyChangeListener {

        /**
         * Create a an listener apdater
         * 
         * @param listener
         */
        public Listener(ISimplePropertyListener listener) {
            super(JFaceListProperty.this, listener);
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
                removePropertyListenerMethod.invoke(model, new Object[] { this });
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage());
            }
        }

        /**
         * This implementation fire a change event.
         */
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (event.getProperty().equals(JFaceListProperty.this.property)) {
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
            return Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
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

    /**
     * @param fieldName
     *            the field name i.e.: the function name without get/set
     *            keywork.
     * @param property
     *            the property key fired
     * @param cls
     *            the class
     * @throws IllegalArgumentException
     *             if the return type of the property is not a subclass of
     *             {@link List}
     */
    public JFaceListProperty(String fieldName, String property, Class cls) {
        this.property = property;
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
            setterMethod = cls.getMethod(getSetterName(fieldName), new Class[] { returnType });
            addPropertyListenerMethod = cls.getMethod("addPropertyChangeListener", new Class[] { IPropertyChangeListener.class }); //$NON-NLS-1$
            removePropertyListenerMethod = cls.getMethod("removePropertyChangeListener", new Class[] { IPropertyChangeListener.class }); //$NON-NLS-1$
        } catch (SecurityException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public INativePropertyListener adaptListener(ISimplePropertyListener listener) {
        return new Listener(listener);
    }

    /**
     * This implementation get the list object using java reflection.
     */
    @Override
    protected List doGetList(Object source) {
        try {
            Object obj = getterMethod.invoke(source, new Object[] {});
            if (obj instanceof List) {
                return (List) obj;
            }
            return Collections.EMPTY_LIST;
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
    protected void doSetList(Object source, List list, ListDiff diff) {
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
        return Object.class;
    }

}
