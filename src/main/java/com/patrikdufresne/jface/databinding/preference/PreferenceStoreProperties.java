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
