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
package com.patrikdufresne.jface.databinding.forms;

import org.eclipse.jface.databinding.swt.WidgetValueProperty;

/**
 * Abstract class to eases the implementation of a property returning a string
 * value.
 * <p>
 * Subclasses must implement the abstract function
 * {@link #doGetStringValue(Object)} and
 * {@link #doSetStringValue(Object, String)}.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class FormWidgetStringValueProperty extends WidgetValueProperty {

    FormWidgetStringValueProperty() {
        super();
    }

    FormWidgetStringValueProperty(int event) {
        super(event);
    }

    FormWidgetStringValueProperty(int[] events) {
        super(events);
    }

    FormWidgetStringValueProperty(int[] events, int[] staleEvents) {
        super(events, staleEvents);
    }

    @Override
    public Object getValueType() {
        return String.class;
    }

    @Override
    protected Object doGetValue(Object source) {
        return doGetStringValue(source);
    }

    @Override
    protected void doSetValue(Object source, Object value) {
        doSetStringValue(source, (String) value);
    }

    abstract String doGetStringValue(Object source);

    abstract void doSetStringValue(Object source, String value);
}
