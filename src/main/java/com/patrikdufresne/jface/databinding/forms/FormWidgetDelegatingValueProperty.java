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

import org.eclipse.core.databinding.property.value.DelegatingValueProperty;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Widget;

abstract class FormWidgetDelegatingValueProperty extends DelegatingValueProperty implements IWidgetValueProperty {
    RuntimeException notSupported(Object source) {
        return new IllegalArgumentException("Widget [" + source.getClass().getName() + "] is not supported."); //$NON-NLS-1$//$NON-NLS-2$
    }

    public FormWidgetDelegatingValueProperty() {
    }

    public FormWidgetDelegatingValueProperty(Object valueType) {
        super(valueType);
    }

    public ISWTObservableValue observe(Widget widget) {
        return (ISWTObservableValue) observe(SWTObservables.getRealm(widget.getDisplay()), widget);
    }

    public ISWTObservableValue observeDelayed(int delay, Widget widget) {
        return SWTObservables.observeDelayedValue(delay, observe(widget));
    }
}
