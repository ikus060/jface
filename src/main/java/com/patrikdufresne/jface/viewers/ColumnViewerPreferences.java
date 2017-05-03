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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * This class is used to store status of a ColumnViewer.
 * 
 * @author Patrik Dufresne
 * 
 */
public class ColumnViewerPreferences {

    static String COLUMN_VIEWER_KEY = Policy.JFACE + ".columnViewer";//$NON-NLS-1$
    /**
     * Preference key used to store the column order.
     */
    private static final String PREF_COLUMN_ORDER = "%s.%d.order"; //$NON-NLS-1$
    /**
     * Preference key used to store the column width
     */
    private static final String PREF_COLUMN_WIDTH = "%s.%d.width"; //$NON-NLS-1$
    /**
     * Preference key used to store the column used for sorting.
     */
    private static final String PREF_SORT_COLUMN = ".sort"; //$NON-NLS-1$

    /**
     * Preference key used to store the sorting direction.
     */
    private static final String PREF_SORT_DIRECTION = ".direction"; //$NON-NLS-1$
    /**
     * Define the minimum width of a column. When a column width is restore, we
     * don't want to hide a column, so we are not restoring small column width.
     * Instead we keep the default value.
     */
    private static final int MIN_WIDTH = 10;

    /**
     * Create a new column preferences.
     * 
     * @param viewer
     *            the table viewer
     * @param store
     *            the preference store
     * @param key
     *            the preference key
     * @return
     */
    public static ColumnViewerPreferences create(TableViewer viewer, IPreferenceStore store, String key) {
        return new ColumnViewerPreferences(viewer, new TableViewerUpdater(), store, key);
    }

    /**
     * Create a new column preferences.
     * 
     * @param viewer
     *            the tree viewer
     * @param store
     *            the preference store
     * @param key
     *            the preference key
     * @return
     */
    public static ColumnViewerPreferences create(TreeViewer viewer, IPreferenceStore store, String key) {
        return new ColumnViewerPreferences(viewer, new TreeViewerUpdater(), store, key);
    }

    private String key;

    private Listener listener = new Listener() {
        @Override
        public void handleEvent(Event event) {
            handleDispose();
        }
    };

    private IPreferenceStore store;

    private ViewerColumnUpdater updater;

    private ColumnViewer viewer;

    /**
     * Create a ColumnPreferences for the given viewer.
     * 
     * @param viewer
     *            the viewer
     * @param store
     *            the preference store
     * @param key
     *            the base key
     */
    public ColumnViewerPreferences(ColumnViewer viewer, ViewerColumnUpdater updater, IPreferenceStore store, String key) {
        this.viewer = viewer;
        this.store = store;
        this.key = key;
        this.updater = updater;

        restoreState();

        // Add a dispose listener to retrieve the information before the widget
        // get dispose
        this.viewer.getControl().addListener(SWT.Dispose, this.listener);

    }

    /**
     * Get the column width.
     * 
     * @return
     */
    private int[] getColumnWidths() {
        int count = this.updater.getColumnCount(this.viewer);
        int[] widths = new int[count];
        for (int i = 0; i < widths.length && i < count; i++) {
            widths[i] = this.updater.getWidth(this.updater.getColumn(this.viewer, i));
        }
        return widths;
    }

    /**
     * Get attaches viewer.
     * 
     * @return the viewer
     */
    protected ColumnViewer getViewer() {
        return this.viewer;
    }

    /**
     * Handle disposable event from the viewer.
     */
    protected void handleDispose() {

        persistState();

        this.viewer = null;
        this.store = null;
        this.listener = null;

    }

    /**
     * Read an integer array from a PreferenceStore.
     * 
     * @param keyPattern
     *            the based key
     * @return integer array
     */
    protected int[] loadPreferences(String keyPattern) {
        // Read array length
        String name = String.format(keyPattern, this.key, Integer.valueOf(-1));
        int count = this.store.getInt(name);

        int[] values = new int[count];
        for (int i = 0; i < count; i++) {
            name = String.format(keyPattern, this.key, Integer.valueOf(i));
            values[i] = this.store.getInt(name);
        }
        return values;
    }

    /**
     * This function is called to save the viewer state.
     */
    protected void persistState() {
        int[] orders = this.updater.getColumnOrder(this.viewer);
        savePreferences(PREF_COLUMN_ORDER, orders);

        int[] widths = getColumnWidths();
        savePreferences(PREF_COLUMN_WIDTH, widths);

        ViewerColumn column = this.updater.getSortColumn(this.viewer);
        int columnIdx = column != null ? this.updater.indexOf(column) : -1;
        this.store.setValue(this.key + PREF_SORT_COLUMN, columnIdx);

        int direction = this.updater.getSortDirection(this.viewer);
        this.store.setValue(this.key + PREF_SORT_DIRECTION, direction);
    }

    /**
     * This function is called to restore the viewer state.
     */
    protected void restoreState() {
        // Apply preferences
        int[] orders = loadPreferences(PREF_COLUMN_ORDER);
        if (orders.length == this.updater.getColumnCount(this.viewer)) {
            this.updater.setColumnOrder(this.viewer, orders);
        }

        int[] widths = loadPreferences(PREF_COLUMN_WIDTH);
        if (widths.length == this.updater.getColumnCount(this.viewer)) {
            setColumnWidths(widths);
        }

        int direction = this.store.getInt(this.key + PREF_SORT_DIRECTION);
        if (direction != 0) {

            // Find the sorter related to the column
            int columnIdx = this.store.getInt(this.key + PREF_SORT_COLUMN);
            if (0 <= columnIdx && columnIdx < this.updater.getColumnCount(this.viewer)) {
                ViewerColumn column = this.updater.getColumn(this.viewer, columnIdx);
                Object data = this.updater.getData(column, AbstractColumnSorter.COLUMN_SORTER_KEY);
                if (data instanceof AbstractColumnSorter) {
                    ((AbstractColumnSorter) data).activate(direction);
                }
            }
        }

    }

    /**
     * Write an integer array to a PreferenceStore.
     * 
     * @param keyPattern
     *            the based key
     * @param values
     *            integer array to write
     */
    protected void savePreferences(String keyPattern, int[] values) {
        // Save array length
        String name = String.format(keyPattern, this.key, Integer.valueOf(-1));
        this.store.setValue(name, values.length);
        // Save arrays value
        for (int i = 0; i < values.length; i++) {
            name = String.format(keyPattern, this.key, Integer.valueOf(i));
            this.store.setValue(name, values[i]);
        }
    }

    /**
     * Sets the columns width
     * 
     * @param widths
     */
    private void setColumnWidths(int[] widths) {
        int count = this.updater.getColumnCount(this.viewer);
        for (int i = 0; i < widths.length && i < count - 1; i++) {
            if (widths[i] >= MIN_WIDTH) {
                this.updater.setWidth(this.updater.getColumn(this.viewer, i), widths[i]);
            }
        }
    }
}
