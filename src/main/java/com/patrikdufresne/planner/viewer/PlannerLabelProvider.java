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

import java.util.Date;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * A planner label provider used to update the planner viewer item.
 * 
 * @author ikus060
 * 
 */
public class PlannerLabelProvider extends BaseLabelProvider implements IFontProvider, IColorProvider, ILabelProvider, ITimeRangeProvider {

    /**
     * Update the label for a cell.
     * 
     * @param item
     *            {@link PlannerViewerItem}
     */
    public void update(PlannerViewerItem item) {
        Object element = item.getElement();
        item.setText(getText(element));
        Image image = getImage(element);
        item.setImage(image);
        item.setBackground(getBackground(element));
        item.setBorderColor(getBorderColor(element));
        item.setForeground(getForeground(element));
        item.setFont(getFont(element));
        item.setStartTime(getStartTime(element));
        item.setEndTime(getEndTime(element));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IFontProvider#getFont(java.lang.Object)
     */
    public Font getFont(Object element) {
        return null;
    }

    public Color getBorderColor(Object element) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
     */
    public Color getBackground(Object element) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
     */
    public Color getForeground(Object element) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
     */
    public Image getImage(Object element) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
     */
    public String getText(Object element) {
        return element == null ? "" : element.toString();//$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.patrikdufresne.jface.plannerviewer.ITimeRangeProvider#getStartTime
     * (java .lang.Object)
     */
    public Date getStartTime(Object element) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.patrikdufresne.jface.plannerviewer.ITimeRangeProvider#getEndTime(
     * java. lang.Object)
     */
    public Date getEndTime(Object element) {
        return null;
    }

}
