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

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;

import com.patrikdufresne.jface.viewers.TextProposalViewer;

/**
 * Utility class to easily create observable for viewer selections
 * 
 * @author Patrik Dufresne
 * 
 */
public class ViewerSupport extends org.eclipse.jface.databinding.viewers.ViewerSupport {

    /**
     * Binds the viewer to the specified input, using the specified label
     * properties to generate labels.
     * 
     * @param viewer
     *            the viewer to set up
     * @param input
     *            the input to set on the viewer
     * @param labelProperty
     *            the property to use for labels
     */
    public static void bind(TextProposalViewer viewer, IObservableSet input, IValueProperty labelProperty) {
        ObservableSetContentProvider contentProvider = new ObservableSetContentProvider(new TextProposalViewerUpdater(viewer));
        if (viewer.getInput() != null) viewer.setInput(null);
        viewer.setContentProvider(contentProvider);
        viewer.setLabelProvider(new ObservableMapLabelProvider(labelProperty.observeDetail(contentProvider.getKnownElements())));
        if (input != null) viewer.setInput(input);
    }

    /**
     * Create an observable list of selected items.
     * <p>
     * This function create an observable list representing the selection of the
     * last focused viewer. This is useful to implement a contextual function.
     * i.e.: Every time the focus of the <code>viewers</code> changes, this
     * observable will be updated.
     * 
     * @param viewers
     *            the viewer to observed.
     */
    public static IObservableList observeMultiSelectionWithFocus(final Viewer... viewers) {

        final WritableValue lastFocusViewer = new WritableValue(null, TableViewer.class);

        FocusListener listener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                for (Viewer viewer : viewers) {
                    if (viewer.getControl() == e.widget) {
                        lastFocusViewer.setValue(viewer);
                        return;
                    }
                }
            }
        };

        for (Viewer viewer : viewers) {
            viewer.getControl().addFocusListener(listener);
            if (viewer.getControl().isFocusControl()) {
                lastFocusViewer.setValue(viewer);
            }
        }

        return ViewerProperties.multipleSelection().observeDetail(lastFocusViewer);
    }

    /**
     * Create an observable value of selected item.
     * <p>
     * This function create an observable value representing the selection of
     * the last focused viewer. This is useful to implement a contextual
     * function. i.e.: Every time the focus of the <code>viewers</code> changes,
     * this observable will be updated.
     * 
     * @param viewers
     *            the viewer to observed.
     */
    public static IObservableValue observeSingleSelectionWithFocus(final Viewer... viewers) {

        final WritableValue lastFocusViewer = new WritableValue(null, TableViewer.class);

        FocusListener listener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                for (Viewer viewer : viewers) {
                    if (viewer.getControl() == e.widget) {
                        lastFocusViewer.setValue(viewer);
                        return;
                    }
                }
            }
        };

        for (Viewer viewer : viewers) {
            viewer.getControl().addFocusListener(listener);
            if (viewer.getControl().isFocusControl()) {
                lastFocusViewer.setValue(viewer);
            }
        }

        return ViewerProperties.singleSelection().observeDetail(lastFocusViewer);
    }

}
