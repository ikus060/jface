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
package com.patrikdufresne.planner.databinding;

import org.eclipse.jface.databinding.viewers.IViewerUpdater;

import com.patrikdufresne.planner.viewer.PlannerViewer;

public class PlannerViewerUpdater implements IViewerUpdater {

    private PlannerViewer viewer;

    public PlannerViewerUpdater(PlannerViewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void insert(Object element, int position) {
        this.viewer.add(element);
    }

    @Override
    public void remove(Object element, int position) {
        this.viewer.remove(element);
    }

    @Override
    public void replace(Object oldElement, Object newElement, int position) {
        this.viewer.remove(oldElement);
        this.viewer.add(newElement);
    }

    @Override
    public void move(Object element, int oldPosition, int newPosition) {
        // Nothing to do. An element can't be move since the order is define by
        // the ViewerSorter.
    }

    @Override
    public void add(Object[] elements) {
        this.viewer.add(elements);
    }

    @Override
    public void remove(Object[] elements) {
        this.viewer.remove(elements);
    }

}
