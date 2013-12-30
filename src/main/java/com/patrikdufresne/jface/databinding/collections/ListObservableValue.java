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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.ComputedValue;

/**
 * Used to adapt a IObservableList into a IObservalbeValue of type List..
 */
public class ListObservableValue extends ComputedValue {

    private IObservableList list;

    public ListObservableValue(IObservableList list, Object valueType) {
        super(valueType);
        if (list == null) {
            throw new NullPointerException();
        }
        this.list = list;
    }

    @Override
    public Object getValueType() {
        return List.class;
    }

    @Override
    protected Object calculate() {
        return new ArrayList(list);
    }

}
