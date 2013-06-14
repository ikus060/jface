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
package com.patrikdufresne.planner.databinding.internal;

import java.util.Date;

import org.eclipse.jface.databinding.swt.WidgetValueProperty;
import org.eclipse.swt.SWT;

import com.patrikdufresne.planner.Planner;

public class PlannerDateSelectionProperty extends WidgetValueProperty {

    public PlannerDateSelectionProperty() {
        super(new int[] { SWT.Modify }, null);
    }

    @Override
    public Object getValueType() {
        return Date.class;
    }

    @Override
    protected Object doGetValue(Object source) {
        return ((Planner) source).getDateSelection();
    }

    @Override
    protected void doSetValue(Object source, Object value) {
        ((Planner) source).setDateSelection((Date) value);
    }

}
