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

import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.ui.forms.widgets.FormText;

/**
 * A factory for creating properties form form widget.
 */
public class FormWidgetProperties {

    /**
     * Returns a value property for observing the text of a {@link FormText}.
     * 
     * @return a value property for observing the text of a {@link FormText}.
     */
    public static IWidgetValueProperty text() {
        return new FormWidgetTextProperty();
    }

}
