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
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

/**
 * This interface is used to update the viewer column.
 * 
 * @author Patrik Dufresne
 * 
 */
public interface ViewerColumnUpdater {

    /**
     * Sub-classes should attach a listern to the column widget.
     * 
     * @param viewerColumn
     *            the viewer column
     * @param type
     *            the event type
     * @param listener
     *            the listener
     */
    void addListener(ViewerColumn column, int type, Listener listener);

    /**
     * Return the viewer column at the given index
     * 
     * @param index
     *            the index
     * @return the column
     */
    ViewerColumn getColumn(ColumnViewer viewer, int index);

    /**
     * Return the number of column;
     * 
     * @param viewer
     */
    int getColumnCount(ColumnViewer viewer);

    /**
     * Gets columns order from the attach viewer. It's use
     * <code>getColumnOrder</code> on a <code>Tree</code> or <code>Table</code>.
     * 
     * @return array of orders
     */
    int[] getColumnOrder(ColumnViewer viewer);

    /**
     * Return the composite widget (Table or Tree).
     * 
     * @param viewer
     * @return
     */
    Composite getComposite(ColumnViewer viewer);

    /**
     * Return the data value
     * 
     * @param column
     *            the column
     * @param key
     *            the key
     * @return the data
     */
    Object getData(ViewerColumn column, String key);

    /**
     * This function return the column used for sorting.
     */
    ViewerColumn getSortColumn(ColumnViewer viewer);

    /**
     * This function return the sorting direction;
     * 
     * @return the direction;
     */
    int getSortDirection(ColumnViewer viewer);

    /**
     * Return the column width.
     * 
     * @param column
     * @return
     */
    int getWidth(ViewerColumn column);

    /**
     * Return the column index.
     * 
     * @param column
     *            the viewer column
     * @return the index
     */
    int indexOf(ViewerColumn column);

    /**
     * Sub-classes should sets the widgets column order.
     * 
     * @param orders
     */
    void setColumnOrder(ColumnViewer viewer, int[] orders);

    /**
     * Sub-classes should called the setDate function of the column widget.
     * 
     * @param column
     */
    void setData(ViewerColumn column, String key, Object data);

    /**
     * Sets the moveable property of the column widget.
     * 
     * @param column
     *            the column viewer
     * @param moveable
     */
    void setMoveable(ViewerColumn column, boolean moveable);

    /**
     * Sets the resizable property of the column.
     * 
     * @param column
     *            the column
     * @param resizable
     *            True to make it resizable.
     */
    void setResizable(ViewerColumn column, boolean resizable);

    /**
     * Sub-classes should call the setSortColum of the underlying widget.
     * 
     * @param viewer
     *            the viewer
     * @param column
     *            the column
     */
    void setSortColumn(ColumnViewer viewer, ViewerColumn column);

    /**
     * Sub-classes should called the setSortDirection of the underlying widget.
     * 
     * @param viewer
     *            the viewer
     * @param direction
     *            the direction
     */
    void setSortDirection(ColumnViewer viewer, int direction);

    /**
     * Sets the column label.
     * 
     * @param column
     *            the column
     * @param string
     *            the label value
     */
    void setText(ViewerColumn column, String string);

    /**
     * Sets the column's tool tip.
     * 
     * @param column
     *            the column
     * @param string
     *            the tooltip value
     */
    void setToolTipText(ViewerColumn column, String string);

    /**
     * Sets the column width.
     * 
     * @param column
     *            the column
     * @param width
     *            the new width value
     */
    void setWidth(ViewerColumn column, int width);

    /**
     * Remove the listener specified from the column widget.
     * 
     * @param column
     *            the column viewer
     * @param type
     *            the event type
     * @param listener
     *            the listener
     */
    void removeListener(ViewerColumn column, int type, Listener listener);

}