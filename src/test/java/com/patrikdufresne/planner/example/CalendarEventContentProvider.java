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
package com.patrikdufresne.planner.example;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.patrikdufresne.planner.example.data.CalendarEvent;
import com.patrikdufresne.planner.example.data.ModelObserver;
import com.patrikdufresne.planner.example.data.Models;
import com.patrikdufresne.planner.viewer.PlannerViewer;

public class CalendarEventContentProvider implements IStructuredContentProvider, ModelObserver {

    private Models input;
    private PlannerViewer viewer;

    @Override
    public Object[] getElements(Object inputElement) {
        return ((Models) inputElement).list().toArray();
    }

    @Override
    public void dispose() {
        // Nothing to do
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (input != null) {
            // Detach
            this.viewer = null;
            input.observers.remove(this);
        }
        if (newInput != null) {
            // Attach
            this.viewer = (PlannerViewer) viewer;
            this.input = (Models) newInput;
            input.observers.add(this);
        }
    }

    @Override
    public void modelChanged(Models models, int type, Collection<CalendarEvent> event) {
        switch (type) {
        case Models.ADD:
            viewer.add(event.toArray());
            break;
        case Models.UPDATE:
            viewer.update(event.toArray(), null);
            break;
        case Models.REMOVE:
            viewer.remove(event.toArray());
            break;
        }
    }
}
