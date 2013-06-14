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

import org.eclipse.ui.forms.widgets.FormText;

public class FormTextTextProperty extends FormWidgetStringValueProperty {

    boolean parseTags;
    boolean expandURLs;

    FormTextTextProperty() {
        this(false, false);
    }

    FormTextTextProperty(boolean parseTags, boolean expandURLs) {
        super();
        this.parseTags = parseTags;
        this.expandURLs = expandURLs;
    }

    @Override
    String doGetStringValue(Object source) {
        return ""; //$NON-NLS-1$
    }

    @Override
    void doSetStringValue(Object source, String value) {
        ((FormText) source).setText(value == null ? "" : value, this.parseTags, //$NON-NLS-1$
                this.expandURLs);
    }

    @Override
    public String toString() {
        return "FormText.text <String>"; //$NON-NLS-1$
    }
}
