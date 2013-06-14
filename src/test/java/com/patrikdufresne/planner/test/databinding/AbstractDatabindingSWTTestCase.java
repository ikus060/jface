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
package com.patrikdufresne.planner.test.databinding;

/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Ashley Cambrell - bug 198904
 ******************************************************************************/

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.patrikdufresne.planner.test.AbstractSWTTestCase;

/**
 * Abstract test case that handles disposing of the Shell after each test.
 */
@RunWith(DatabindingClassRunner.class)
public abstract class AbstractDatabindingSWTTestCase extends AbstractSWTTestCase {
    private List<ChangeEvent> changeEvents;
    private List<ValueChangeEvent> valueChangeEvents;
    private IChangeListener changeListener;
    private IValueChangeListener valueChangeListener;

    protected List<ChangeEvent> getChangeEvents() {
        if (changeEvents == null) {
            changeEvents = new ArrayList<ChangeEvent>();
        }
        return changeEvents;
    }

    protected List<ValueChangeEvent> getValueChangeEvents() {
        if (valueChangeEvents == null) {
            valueChangeEvents = new ArrayList<ValueChangeEvent>();
        }
        return valueChangeEvents;
    }

    protected IValueChangeListener getValueChangeListener() {
        if (valueChangeListener == null) {
            valueChangeListener = new IValueChangeListener() {
                @Override
                public void handleValueChange(ValueChangeEvent event) {
                    getValueChangeEvents().add(event);
                }
            };
        }
        return valueChangeListener;
    }

    protected IChangeListener getChangeListener() {
        if (changeListener == null) {
            changeListener = new IChangeListener() {
                @Override
                public void handleChange(ChangeEvent event) {
                    getChangeEvents().add(event);
                }
            };
        }
        return changeListener;
    }

    @Before
    public void resetEventLists() {
        if (changeEvents != null) {
            changeEvents.clear();
        }
        if (valueChangeEvents != null) {
            valueChangeEvents.clear();
        }
    }

}
