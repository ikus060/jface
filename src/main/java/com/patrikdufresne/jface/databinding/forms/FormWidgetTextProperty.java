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

import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.ui.forms.widgets.FormText;

/**
 * Delegating property to access the text field of form widgets.
 * 
 * @author Patrik Dufresne
 * 
 */
public class FormWidgetTextProperty extends FormWidgetDelegatingValueProperty {
    private IValueProperty formtext;

    /**
     * Create a new instance of this class.
     */
    public FormWidgetTextProperty() {
        super(String.class);
    }

    /**
     * This implementation return the delegating property according to the
     * widget type. This function throws an exception if the widget is not
     * supported.
     */
    @Override
    protected IValueProperty doGetDelegate(Object source) {
        if (source instanceof FormText) {
            if (this.formtext == null) this.formtext = new FormTextTextProperty();
            return this.formtext;
        }
        throw notSupported(source);
    }
}
