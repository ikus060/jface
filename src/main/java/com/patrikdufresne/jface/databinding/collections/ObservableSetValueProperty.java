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
public class ObservableSetValueProperty extends ObservableCollectionValueProperty {

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
