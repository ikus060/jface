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
package com.patrikdufresne.planner.test.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.junit.Test;

import com.patrikdufresne.planner.Planner;
import com.patrikdufresne.planner.PlannerItem;
import com.patrikdufresne.planner.test.databinding.AbstractDatabindingSWTTestCase;
import com.patrikdufresne.planner.viewer.PlannerViewer;

public class PlannerViewerTest extends AbstractDatabindingSWTTestCase {

    @Test
    public void testAddRemove() {

        Planner planner = new Planner(getShell(), SWT.NONE);
        PlannerViewer viewer = new PlannerViewer(planner);

        // Add object
        viewer.add(Integer.valueOf(0));
        assertEquals(1, planner.getItemCount());
        assertEquals(Integer.valueOf(0), planner.getItem(0).getData());

        // Remove object
        viewer.remove(Integer.valueOf(0));
        assertEquals(0, planner.getItemCount());

    }

    @Test
    public void testSetGetSelection() {

        Planner planner = new Planner(getShell(), SWT.NONE);
        PlannerViewer viewer = new PlannerViewer(planner);
        viewer.add(Integer.valueOf(0));
        viewer.add(Integer.valueOf(1));
        viewer.add(Integer.valueOf(2));
        assertEquals(3, planner.getItemCount());
        assertNull(null, planner.getSelection());
        assertEquals(-1, planner.getSelectionIndex());

        // Sets viewer selection
        viewer.setSelection(new StructuredSelection(Integer.valueOf(1)));
        assertNotNull(planner.getSelection());
        assertEquals(Integer.valueOf(1), planner.getSelection().getData());
        assertEquals(1, planner.getSelectionIndex());

        // Sets planner selection
        planner.setSelection(0);
        Object selection = ((StructuredSelection) viewer.getSelection()).getFirstElement();
        assertEquals(Integer.valueOf(0), selection);

    }

    @Test
    public void testGetControl() {

        Planner planner = new Planner(getShell(), SWT.NONE);
        PlannerViewer viewer = new PlannerViewer(planner);
        assertEquals(planner, viewer.getControl());
        assertEquals(planner, viewer.getPlanner());

    }

    @Test
    public void testComparator() {

        Planner planner = new Planner(getShell(), SWT.NONE);
        PlannerViewer viewer = new PlannerViewer(planner);
        viewer.setComparator(new ViewerComparator());
        viewer.add(Integer.valueOf(5));
        viewer.add(Integer.valueOf(1));
        viewer.add(Integer.valueOf(2));

        assertEquals(3, planner.getItemCount());
        PlannerItem[] items = planner.getItems();
        assertEquals(Integer.valueOf(1), items[0].getData());
        assertEquals(Integer.valueOf(2), items[1].getData());
        assertEquals(Integer.valueOf(5), items[2].getData());

    }

    @Test
    public void testSetInput() {

        Planner planner = new Planner(getShell(), SWT.NONE);
        PlannerViewer viewer = new PlannerViewer(planner);
        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setInput(Arrays.asList(Integer.valueOf(5), Integer.valueOf(1), Integer.valueOf(2)));

        assertEquals(3, planner.getItemCount());
        PlannerItem[] items = planner.getItems();
        assertEquals(Integer.valueOf(5), items[0].getData());
        assertEquals(Integer.valueOf(1), items[1].getData());
        assertEquals(Integer.valueOf(2), items[2].getData());

    }

}
