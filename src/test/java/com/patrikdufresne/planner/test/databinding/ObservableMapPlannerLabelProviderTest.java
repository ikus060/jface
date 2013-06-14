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

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.databinding.observable.map.WritableMap;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;

import com.patrikdufresne.planner.Planner;
import com.patrikdufresne.planner.PlannerItem;
import com.patrikdufresne.planner.databinding.ObservableMapPlannerLabelProvider;
import com.patrikdufresne.planner.databinding.PlannerViewerUpdater;
import com.patrikdufresne.planner.viewer.PlannerViewer;

public class ObservableMapPlannerLabelProviderTest extends AbstractDatabindingSWTTestCase {

    @Test
    public void testAddingElement() {

        WritableSet model = new WritableSet();
        WritableMap labels = new WritableMap();
        WritableMap start = new WritableMap();
        WritableMap end = new WritableMap();

        // Insert first element
        Calendar cal = Calendar.getInstance();
        Object element1 = Integer.valueOf(0);
        Date start1 = cal.getTime();
        cal.add(Calendar.HOUR, 5);
        Date end1 = cal.getTime();
        model.add(element1);
        labels.put(element1, "Element 1");
        start.put(element1, start1);
        end.put(element1, end1);

        // Insert second element
        Object element2 = Integer.valueOf(1);
        cal.add(Calendar.HOUR, 2);
        final Date start2 = cal.getTime();
        cal.add(Calendar.HOUR, 6);
        final Date end2 = cal.getTime();
        model.add(element2);
        labels.put(element2, "Element 2");
        start.put(element2, start2);
        end.put(element2, end2);

        // Create the viewer with the model.
        Planner planner = new Planner(getShell(), SWT.NONE);
        PlannerViewer viewer = new PlannerViewer(planner);
        viewer.setContentProvider(new ObservableSetContentProvider(new PlannerViewerUpdater(viewer)));
        viewer.setLabelProvider(new ObservableMapPlannerLabelProvider(labels, start, end));
        viewer.setInput(model);

        // Check the creation of the items
        assertEquals(2, planner.getItemCount());
        PlannerItem[] items = planner.getItems();
        assertEquals("Element 1", items[0].getText());
        assertEquals(start1, items[0].getStartTime());
        assertEquals(end1, items[0].getEndTime());

        assertEquals("Element 2", items[1].getText());
        assertEquals(start2, items[1].getStartTime());
        assertEquals(end2, items[1].getEndTime());

        // Sets the model value and assert change
        labels.put(element1, "Changed");
        start.put(element1, start2);
        start.put(element1, end2);

        assertEquals(2, planner.getItemCount());
        final PlannerItem[] finalitems = planner.getItems();
        assertEquals(element1, finalitems[0].getData());
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                assertEquals("Changed", finalitems[0].getText());
                assertEquals(start2, finalitems[0].getStartTime());
                assertEquals(end2, finalitems[0].getEndTime());
            }
        });

    }

}
