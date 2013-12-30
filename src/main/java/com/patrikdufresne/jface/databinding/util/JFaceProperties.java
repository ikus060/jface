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

import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.core.databinding.property.set.ISetProperty;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.jface.util.IPropertyChangeListener;

/**
 * Helper class for providing observable instances for properties of an object that fires property changes events to an
 * {@link IPropertyChangeListener}.
 * 
 * @author Patrik Dufresne
 * 
 */
public class JFaceProperties extends org.eclipse.jface.databinding.util.JFaceProperties {

    /**
     * Returns a property for observing the property of the given model object whose getter and setter use the suffix
     * fieldName in the same manner as a Java bean and which fires events to an {@link IPropertyChangeListener} for the
     * given propertyName when the value of the field changes.
     * 
     * @param cls
     *            the class defining the getter and setter
     * @param fieldName
     *            the field name
     * 
     * @return an observable list
     */
    public static IListProperty list(Class cls, String propertyName) {
        return new JFaceListProperty(propertyName, propertyName, cls);
    }

    /**
     * Returns a property for observing the property of the given model object whose getter and setter use the suffix
     * fieldName in the same manner as a Java bean and which fires events to an {@link IPropertyChangeListener} for the
     * given propertyName when the value of the field changes.
     * 
     * @param cls
     *            the class defining the getter and setter
     * @param fieldName
     *            the field name
     * @param propertyName
     *            the property name
     * 
     * @return an observable list
     */
    public static IListProperty list(Class cls, String fieldName, String propertyName) {
        return new JFaceListProperty(fieldName, propertyName, cls);
    }

    /**
     * Returns a property for observing the property of the given model object whose getter and setter use the suffix
     * fieldName in the same manner as a Java bean and which fires events to an {@link IPropertyChangeListener} for the
     * given propertyName when the value of the field changes.
     * 
     * @param cls
     *            the class defining the getter and setter
     * @param propertyName
     *            the property name
     * @return an observable set
     */
    public static ISetProperty set(Class cls, String propertyName) {
        return new JFaceSetProperty(propertyName, propertyName, cls, null);
    }

    /**
     * Returns a property for observing the property of the given model object whose getter and setter use the suffix
     * fieldName in the same manner as a Java bean and which fires events to an {@link IPropertyChangeListener} for the
     * given propertyName when the value of the field changes.
     * 
     * @param cls
     *            the class defining the getter and setter
     * @param propertyName
     *            the property name
     * @param elementType
     *            the element type of the returned set property
     * @return an observable set
     */
    public static ISetProperty set(Class cls, String propertyName, Class elementType) {
        return new JFaceSetProperty(propertyName, propertyName, cls, null);
    }

    /**
     * Returns a property for observing the property of the given model object whose getter and setter use the suffix
     * fieldName in the same manner as a Java bean and which fires events to an {@link IPropertyChangeListener} for the
     * given propertyName when the value of the field changes.
     * 
     * @param cls
     *            the class defining the getter and setter
     * @param fieldName
     *            the field name
     * @param propertyName
     *            the property name
     * @return an observable set
     */
    public static ISetProperty set(Class cls, String fieldName, String propertyName) {
        return new JFaceSetProperty(fieldName, propertyName, cls, null);
    }

    /**
     * Returns a property for observing the property of the given model object whose getter and setter use the suffix
     * fieldName in the same manner as a Java bean and which fires events to an {@link IPropertyChangeListener} for the
     * given propertyName when the value of the field changes.
     * 
     * @param cls
     *            the class defining the getter and setter
     * @param fieldName
     *            the field name
     * @param propertyName
     *            the property name
     * @param elementType
     *            the element type of the returned set property
     * @return an observable set
     */
    public static ISetProperty set(Class cls, String fieldName, String propertyName, Class elementType) {
        return new JFaceSetProperty(fieldName, propertyName, cls, null);
    }

    /**
     * Returns a property for observing the property of the given model object whose getter and setter use the suffix
     * fieldName in the same manner as a Java bean and which fires events to an {@link IPropertyChangeListener} for the
     * given propertyName when the value of the field changes.
     * 
     * @param clazz
     *            the class defining the getter and setter
     * @param propertyName
     *            the property name
     * 
     * @return an observable value
     */
    public static IValueProperty value(Class cls, String propertyName) {
        return org.eclipse.jface.databinding.util.JFaceProperties.value(cls, propertyName, propertyName);
    }
}
