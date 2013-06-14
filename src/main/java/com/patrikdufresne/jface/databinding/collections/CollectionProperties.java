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
