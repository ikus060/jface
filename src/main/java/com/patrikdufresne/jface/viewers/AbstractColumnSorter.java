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
package com.patrikdufresne.jface.viewers;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * This abstract class make it easier to add sort capability to a column. This
 * class need to be used in conjunction with the content provider.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class AbstractColumnSorter extends ViewerComparator {

    /**
     * Key used to store instance of this class in the column widget.
     */
    public static final String COLUMN_SORTER_KEY = "column.sorter"; //$NON-NLS-1$

    /**
     * Constants to designate the ascending mode.
     */
    private static final int ASC = 1;
    /**
     * Constants to designate the descending mode.
     */
    private static final int DESC = -1;
    /**
     * Constants to designate the no sorting mode.
     */
    private static final int NONE = 0;

    private ViewerColumn column;

    /**
     * The direction defined by NONE, ASC or DESC.
     */
    private int direction = 0;

    private ViewerColumnUpdater updater;
    /**
     * Selection listener on the column widget.
     */
    private Listener listener = new Listener() {
        @Override
        public void handleEvent(Event event) {
            activate();
        }
    };

    /**
     * Create a new Abstract sorter.
     * 
     * @param viewer
     *            the viewer
     * @param column
     *            the column
     */
    public AbstractColumnSorter(ViewerColumn column, ViewerColumnUpdater updater) {
        if (column == null || updater == null) {
            throw new NullPointerException();
        }
        this.column = column;
        this.updater = updater;

        // Attach a listener
        this.updater.addListener(this.column, SWT.Selection, this.listener);
        this.updater.setData(this.column, COLUMN_SORTER_KEY, this);
    }

    /**
     * This function is used to active this sorter. It's either called by user
     * event or programmatically.
     */
    public void activate() {
        // Check if viewer already sorted by this Sorter.
        if (getViewer().getComparator() != null && getViewer().getComparator() == AbstractColumnSorter.this) {
            if (this.direction == ASC) {
                setSorter(DESC);
            } else if (this.direction == DESC) {
                // Disable sorting
                setSorter(NONE);
            }
        } else {
            setSorter(ASC);
        }
    }

    /**
     * This function is used to activate this sorter with a specific direction.
     * 
     * @param direction
     *            the sorting direction SWT.DOWN or SWT.UP
     */
    public void activate(int direction) {
        if (direction != SWT.DOWN && direction != SWT.UP) return;
        setSorter(direction == SWT.UP ? ASC : DESC);
    }

    /**
     * This implementation compare the objects specified and consider the sorter
     * direction.
     */
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        return this.direction * doCompare(viewer, e1, e2);
    }

    /**
     * This function should compare the object.
     * 
     * @param viewer
     * @param e1
     * @param e2
     * @return
     */
    protected abstract int doCompare(Viewer viewer, Object e1, Object e2);

    /**
     * Return the viewer column.
     * 
     * @return the viewer column
     */
    public ViewerColumn getColumn() {
        return this.column;
    }

    /**
     * Return the sorting direction.
     * 
     * @return 1 for ascending sort, -1 for descending sort, 0 for no sort.
     */
    public int getDirection() {
        return this.direction;
    }

    /**
     * Return the Viewer.
     * 
     * @return the viewer.
     */
    public ColumnViewer getViewer() {
        return this.column.getViewer();
    }

    /**
     * Change the sorting on the viewer.
     * 
     * @param direction
     *            direction of sorting : NONE, ASC or DESC.
     */
    protected void setSorter(int direction) {
        if (direction == NONE) {
            this.updater.setSortColumn(getViewer(), null);
            this.updater.setSortDirection(getViewer(), SWT.NONE);
            getViewer().setComparator(null);
        } else {
            this.updater.setSortColumn(getViewer(), this.column);
            this.direction = direction;
            this.updater.setSortDirection(getViewer(), direction == ASC ? SWT.UP : SWT.DOWN);

            if (getViewer().getComparator() == this) {
                getViewer().refresh();
            } else {
                getViewer().setComparator(this);
            }
        }
    }

    /**
     * Dispose this column sorter.
     */
    public void dispose() {
        if (this.column != null && this.updater != null) {
            this.updater.removeListener(this.column, SWT.Selection, this.listener);
            this.updater.setData(this.column, COLUMN_SORTER_KEY, null);
        }
        this.column = null;
        this.updater = null;
        this.listener = null;
    }
}