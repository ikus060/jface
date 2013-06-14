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

import java.util.Date;
import java.util.Set;

import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.MapChangeEvent;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.widgets.Display;

import com.patrikdufresne.planner.viewer.PlannerLabelProvider;

/**
 * This label provider may be used as a label provider for the planner view.
 * It's allow the user to define observable map for the starting date and ending
 * date.
 * 
 * @author ikus060
 * 
 */
public class ObservableMapPlannerLabelProvider extends PlannerLabelProvider {

    /**
     * Observable maps typically mapping from viewer elements to label values.
     * Subclasses may use these maps to provide custom labels.
     * 
     * @since 1.4
     */
    protected IObservableMap[] attributeMaps;

    private IMapChangeListener mapChangeListener = new IMapChangeListener() {
        @Override
        public void handleMapChange(MapChangeEvent event) {
            ObservableMapPlannerLabelProvider.this.handleMapChange(event);
        }
    };

    /**
     * Notify this class about a map change.
     * 
     * @param event
     */
    protected void handleMapChange(MapChangeEvent event) {
        Set affectedElements = event.diff.getChangedKeys();
        final LabelProviderChangedEvent newEvent = new LabelProviderChangedEvent(ObservableMapPlannerLabelProvider.this, affectedElements.toArray());
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                fireLabelProviderChanged(newEvent);
            }
        });
    }

    /**
     * Create a new label provider for a planner viewer.
     * 
     * @param attributeMap
     *            map used for label
     * @param start
     *            map used for start time
     * @param end
     *            map used for end time
     */
    public ObservableMapPlannerLabelProvider(IObservableMap attributeMap, IObservableMap start, IObservableMap end) {
        this(new IObservableMap[] { attributeMap, start, end });
    }

    /**
     * Creates a new label provider that tracks changes to more than one
     * attribute. This constructor should be used by subclasses that override
     * {@link #update(ViewerCell)} and make use of more than one attribute.
     * 
     * @param attributeMaps
     */
    protected ObservableMapPlannerLabelProvider(IObservableMap[] attributeMaps) {
        System.arraycopy(attributeMaps, 0, this.attributeMaps = new IObservableMap[attributeMaps.length], 0, attributeMaps.length);
        for (int i = 0; i < attributeMaps.length; i++) {
            attributeMaps[i].addMapChangeListener(this.mapChangeListener);
        }
    }

    @Override
    public void dispose() {
        for (int i = 0; i < this.attributeMaps.length; i++) {
            this.attributeMaps[i].removeMapChangeListener(this.mapChangeListener);
        }
        super.dispose();
        this.attributeMaps = null;
        this.mapChangeListener = null;
    }

    @Override
    public Date getEndTime(Object element) {
        return (Date) this.attributeMaps[2].get(element);
    }

    @Override
    public Date getStartTime(Object element) {
        return (Date) this.attributeMaps[1].get(element);
    }

    @Override
    public String getText(Object element) {
        return this.attributeMaps[0].get(element).toString();
    }

}
