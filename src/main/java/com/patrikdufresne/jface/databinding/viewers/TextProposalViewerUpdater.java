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
package com.patrikdufresne.jface.databinding.viewers;

import org.eclipse.jface.databinding.viewers.IViewerUpdater;

import com.patrikdufresne.jface.viewers.TextProposalViewer;

// TODO This should be deprecated since the viewer will be deprecated.
public class TextProposalViewerUpdater implements IViewerUpdater {
    /**
     * The viewer to update.
     */
    private final TextProposalViewer viewer;

    /**
     * Constructs a ViewerUpdater for updating the specified viewer.
     * 
     * @param viewer
     *            the viewer which will be updated through this instance.
     */
    public TextProposalViewerUpdater(TextProposalViewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void insert(Object element, int position) {
        this.viewer.refresh();
    }

    @Override
    public void remove(Object element, int position) {
        this.viewer.refresh();
    }

    @Override
    public void replace(Object oldElement, Object newElement, int position) {
        this.viewer.refresh();
    }

    @Override
    public void move(Object element, int oldPosition, int newPosition) {
        this.viewer.refresh();
    }

    @Override
    public void add(Object[] elements) {
        this.viewer.refresh();
    }

    @Override
    public void remove(Object[] elements) {
        this.viewer.refresh();
    }

}
