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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;

import com.patrikdufresne.planner.Planner;
import com.patrikdufresne.planner.databinding.PlannerProperties;

/**
 * Test case for {@link PlannerProperties}.
 * 
 * @author ikus060
 * 
 */
public class PlannersObservablesTest extends AbstractDatabindingSWTTestCase {

    @Test
    public void testObserveDateSelection() {
        Planner planner = new Planner(getShell(), SWT.NONE);
        getShell().open();

        IObservableValue value = PlannerProperties.dateSelection().observe(planner);
        assertNotNull(value);
        assertEquals(Date.class, value.getValueType());
        value.addChangeListener(getChangeListener());
        value.addValueChangeListener(getValueChangeListener());

        // Test sets observable value
        Date date1 = new Date();
        value.setValue(date1);
        assertEquals(date1, planner.getDateSelection());
        assertEquals(1, getChangeEvents().size());
        assertEquals(1, getValueChangeEvents().size());
        resetEventLists();

        // Test sets planner value
        Date date2 = new Date();
        planner.setDateSelection(date2);
        assertEquals(date2, value.getValue());

        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                assertEquals(1, getChangeEvents().size());
                assertEquals(1, getValueChangeEvents().size());
            }
        });

    }

    @Test
    public void testObserveEnd() {
        Planner planner = new Planner(getShell(), SWT.NONE);
        getShell().open();

        IObservableValue value = PlannerProperties.endDateValue().observe(planner);
        assertNotNull(value);
        assertEquals(Date.class, value.getValueType());
        value.addChangeListener(getChangeListener());
        value.addValueChangeListener(getValueChangeListener());

        try {
            value.setValue(new Date());
            fail("Setting the observable value should fail.");
        } catch (UnsupportedOperationException e) {
            // OK
        }

        // Test sets planner date value
        Date date = new Date();
        date = new Date(date.getTime() - 1000 * 60 * 60 * 24 * 7);
        planner.setDateSelection(date);
        assertEquals(1, getChangeEvents().size());
        assertEquals(1, getValueChangeEvents().size());
        assertEquals(planner.getEndDate(), value.getValue());

    }

    @Test
    public void testObserveStart() {
        Planner planner = new Planner(getShell(), SWT.NONE);
        getShell().open();

        IObservableValue value = PlannerProperties.startDateValue().observe(planner);
        assertNotNull(value);
        assertEquals(Date.class, value.getValueType());
        value.addChangeListener(getChangeListener());
        value.addValueChangeListener(getValueChangeListener());

        try {
            value.setValue(new Date());
            fail("Setting the observable value should fail.");
        } catch (UnsupportedOperationException e) {
            // OK
        }

        // Test sets planner date value
        Date date = new Date();
        date = new Date(date.getTime() - 1000 * 60 * 60 * 24 * 7);
        planner.setDateSelection(date);
        assertEquals(1, getChangeEvents().size());
        assertEquals(1, getValueChangeEvents().size());
        assertEquals(planner.getStartDate(), value.getValue());

    }

}
