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
package com.patrikdufresne.planner.viewer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.patrikdufresne.planner.Planner;
import com.patrikdufresne.planner.PlannerItem;

/**
 * A concrete viewer based on a <code>Planner</code> control.
 * <p>
 * The style SWT.WRAP may be used to create new item with WRAP style.
 * </p>
 */
public class PlannerViewer extends StructuredViewer {

    private Planner planner;

    /**
     * Default wrap style for item.
     */
    private boolean wrap;

    /**
     * Creates a planner viewer on a newly-created Planner control under the
     * given parent. The viewer has no input, no content provider, a default
     * label provider, no sorter, and no filters.
     * 
     * @param parent
     *            the parent control
     */
    public PlannerViewer(Composite parent) {
        this(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    }

    /**
     * Creates a planner viewer on a newly-created Planner control under the
     * given parent. The viewer has no input, no content provider, a default
     * label provider, no sorter, and no filters.
     * 
     * @param parent
     *            the parent control
     */
    public PlannerViewer(Composite parent, int style) {
        this(new Planner(parent, style));
        this.wrap = (style & SWT.WRAP) != 0;
    }

    /**
     * Creates a planner viewer on the given Planner control. The viewer has no
     * input, no content provider, a default label provider, no sorter, and no
     * filters.
     * 
     * @param planner
     *            the Planner control
     * 
     */
    public PlannerViewer(Planner planner) {
        this.planner = planner;
        hookControl(planner);
    }

    /**
     * Adds the given element to this planner viewer. If this viewer does not
     * have a sorter, the element is added at the end; otherwise the element is
     * inserted at the appropriate position.
     * <p>
     * This method should be called (by the content provider) when a single
     * element has been added to the model, in order to cause the viewer to
     * accurately reflect the model. This method only affects the viewer, not
     * the model. Note that there is another method for efficiently processing
     * the simultaneous addition of multiple elements.
     * </p>
     * 
     * @param element
     *            the element
     */
    public void add(Object element) {
        add(new Object[] { element });
    }

    /**
     * Adds the given elements to this planner viewer. If this viewer does not
     * have a sorter, the elements are added at the end in the order given;
     * otherwise the elements are inserted at appropriate positions.
     * <p>
     * This method should be called (by the content provider) when elements have
     * been added to the model, in order to cause the viewer to accurately
     * reflect the model. This method only affects the viewer, not the model.
     * </p>
     * 
     * @param elements
     *            the elements to add
     */
    public void add(Object[] elements) {
        assertElementsNotNull(elements);
        Object[] filtered = filter(elements);
        ILabelProvider labelProvider = (ILabelProvider) getLabelProvider();
        for (int i = 0; i < filtered.length; i++) {
            Object element = filtered[i];
            int ix = indexForElement(element);
            insertItem(labelProvider, element, ix);
        }
    }

    @Override
    protected Widget doFindInputItem(Object element) {
        if (equals(element, getRoot())) {
            return getControl();
        }
        return null;
    }

    @Override
    protected Widget doFindItem(Object element) {
        PlannerItem[] children = planner.getItems();
        for (PlannerItem item : children) {
            Object data = item.getData();
            if (data != null && equals(data, element)) {
                return item;
            }
        }
        return null;
    }

    @Override
    protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
        if (item instanceof PlannerItem) {
            // remember element we are showing
            if (fullMap) {
                associate(element, (PlannerItem) item);
            } else {
                Object data = item.getData();
                if (data != null) {
                    unmapElement(data, item);
                }
                item.setData(element);
                mapElement(element, item);
            }

            if (getLabelProvider() instanceof PlannerLabelProvider) {
                ((PlannerLabelProvider) getLabelProvider()).update(new PlannerViewerItem((PlannerItem) item));
            }
        }
    }

    /**
     * This implementation return a reference to the planner control.
     */
    @Override
    public Control getControl() {
        return planner;
    }

    /**
     * Returns the planner embedded in this viewer.
     * 
     * @return
     */
    public Planner getPlanner() {
        return planner;
    }

    /**
     * This implementation retrieved the selection from the TabFolder widget
     */
    @Override
    protected List getSelectionFromWidget() {
        ArrayList list = new ArrayList(1);
        PlannerItem item = this.planner.getSelection();
        if (item != null) {
            list.add(item.getData());
        }
        return list;
    }

    /**
     * @param element
     *            the element to insert
     * @return the index where the item should be inserted.
     */
    protected int indexForElement(Object element) {
        ViewerComparator comparator = getComparator();
        if (comparator == null) {
            return this.planner.getItemCount();
        }
        int count = this.planner.getItemCount();
        int min = 0, max = count - 1;
        while (min <= max) {
            int mid = (min + max) / 2;
            Object data = this.planner.getItem(mid).getData();
            int compare = comparator.compare(this, data, element);
            if (compare == 0) {
                // find first item > element
                while (compare == 0) {
                    ++mid;
                    if (mid >= count) {
                        break;
                    }
                    data = this.planner.getItem(mid).getData();
                    compare = comparator.compare(this, data, element);
                }
                return mid;
            }
            if (compare < 0) {
                min = mid + 1;
            } else {
                max = mid - 1;
            }
        }
        return min;
    }

    /*
     * (non-Javadoc) Method declared on Viewer.
     */
    @Override
    protected void inputChanged(Object input, Object oldInput) {
        getControl().setRedraw(false);
        try {
            preservingSelection(new Runnable() {
                @Override
                public void run() {
                    internalRefresh(getRoot());
                }
            });
        } finally {
            getControl().setRedraw(true);
        }
    }

    private void insertItem(ILabelProvider labelProvider, Object element, int index) {
        PlannerItem item = new PlannerItem(this.planner, wrap ? SWT.WRAP : SWT.NONE, index);
        updateItem(item, element);
        mapElement(element, getControl()); // must map it, since findItem only
        // looks in map, if enabled
    }

    @Override
    protected void internalRefresh(Object element) {
        if (element == null || equals(element, getRoot())) {
            internalRefreshAll();
        } else {
            Widget w = findItem(element);
            if (w != null) {
                updateItem(w, element);
            }
        }
    }

    /**
     * Refresh all of the elements of the table. update the labels if
     * updatLabels is true;
     * 
     * @param updateLabels
     * 
     * @since 3.1
     */
    private void internalRefreshAll() {
        // the parent

        // in the code below, it is important to do all disassociates
        // before any associates, since a later disassociate can undo an
        // earlier associate
        // e.g. if (a, b) is replaced by (b, a), the disassociate of b to
        // item 1 could undo
        // the associate of b to item 0.
        Object[] children = getSortedChildren(getRoot());
        PlannerItem[] items = this.planner.getItems();
        int min = Math.min(children.length, items.length);
        for (int i = 0; i < min; ++i) {
            PlannerItem item = items[i];

            // if the element is unchanged, update its label if appropriate
            if (equals(children[i], item.getData())) {
                updateItem(item, children[i]);
            } else {
                // updateItem does an associate(...), which can mess up
                // the associations if the order of elements has changed.
                // E.g. (a, b) -> (b, a) first replaces a->0 with b->0, then
                // replaces b->1 with a->1, but this actually removes b->0.
                // So, if the object associated with this item has changed,
                // just disassociate it for now, and update it below.
                // we also need to reset the item (set its text,images etc. to
                // default values) because the label decorators rely on this
                disassociate(item);
            }
        }
        // dispose of all items beyond the end of the current elements
        if (min < items.length) {
            for (int i = items.length; --i >= min;) {
                disassociate(items[i]);
                items[i].dispose();
            }
        }
        // Update items which were dissociated above
        for (int i = 0; i < min; ++i) {
            PlannerItem item = items[i];
            if (item.getData() == null) {
                updateItem(item, children[i]);
            }
        }
        // Add any remaining elements
        for (int i = min; i < children.length; ++i) {
            add(children[i]);
        }
    }

    /**
     * Removes the given elements from this list viewer.
     * 
     * @param elements
     *            the elements to remove
     */
    private void internalRemove(final Object[] elements) {
        Object input = getInput();
        for (int i = 0; i < elements.length; ++i) {
            if (equals(elements[i], input)) {
                setInput(null);
                return;
            }
            Widget item = doFindItem(elements[i]);
            if (item != null) {
                unmapElement(elements[i], item);
                item.dispose();
            }
        }
    }

    /**
     * Removes the given element from this list viewer. The selection is updated
     * if necessary.
     * <p>
     * This method should be called (by the content provider) when a single
     * element has been removed from the model, in order to cause the viewer to
     * accurately reflect the model. This method only affects the viewer, not
     * the model. Note that there is another method for efficiently processing
     * the simultaneous removal of multiple elements.
     * </p>
     * 
     * @param element
     *            the element
     */
    public void remove(Object element) {
        remove(new Object[] { element });
    }

    /**
     * Removes the given elements from this list viewer. The selection is
     * updated if required.
     * <p>
     * This method should be called (by the content provider) when elements have
     * been removed from the model, in order to cause the viewer to accurately
     * reflect the model. This method only affects the viewer, not the model.
     * </p>
     * 
     * @param elements
     *            the elements to remove
     */
    public void remove(final Object[] elements) {
        assertElementsNotNull(elements);
        if (elements.length == 0) {
            return;
        }
        preservingSelection(new Runnable() {
            public void run() {
                internalRemove(elements);
            }
        });
    }

    @Override
    public void reveal(Object element) {

    }

    @Override
    protected void setSelectionToWidget(List in, boolean reveal) {
        if (in == null || in.size() == 0) { // clear selection
            this.planner.setSelection(-1);
        } else {
            Widget item = doFindItem(in.get(0));
            if (item != null) {
                this.planner.setSelection((PlannerItem) item);
            } else {
                this.planner.setSelection(-1);
            }
        }

    }

}
