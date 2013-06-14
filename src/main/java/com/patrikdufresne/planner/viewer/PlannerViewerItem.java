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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.patrikdufresne.planner.PlannerItem;

/**
 * PlannerViewerEvent is the concrete implementation of the part that represents
 * items in a Planner.
 * 
 * @author Patrik Dufresne
 * 
 */
public class PlannerViewerItem {

    private PlannerItem item;

    /**
     * Create a new instance of the receiver.
     * 
     * @param item
     *            GridItem source.
     */
    PlannerViewerItem(PlannerItem item) {
        this.item = item;
    }

    public Object clone() {
        return new PlannerViewerItem(item);
    }

    public Color getBackground() {
        return item.getBackground();
    }

    public Color getBorderColor() {
        return item.getBorderColor();
    }

    public Rectangle[] getBounds() {
        return item.getBounds();
    }

    public Control getControl() {
        return item.getParent();
    }

    public Object getElement() {
        return item.getData();
    }

    public Date getEndTime() {
        return item.getEndTime();
    }

    public Font getFont() {
        return item.getFont();
    }

    public Color getForeground() {
        return item.getForeground();
    }

    public Image getImage() {
        return item.getImage();
    }

    public Widget getItem() {
        return item;
    }

    public Date getStartTime() {
        return item.getStartTime();
    }

    public String getText() {
        return item.getText();
    }

    public void setBackground(Color color) {
        item.setBackground(color);
    }

    public void setBorderColor(Color color) {
        item.setBorderColor(color);
    }

    public void setEndTime(Date end) {
        item.setEndTime(end);
    }

    public void setFont(Font font) {
        item.setFont(font);
    }

    public void setForeground(Color color) {
        item.setForground(color);
    }

    public void setImage(Image image) {
        item.setImage(image);
    }

    void setItem(PlannerItem item) {
        this.item = item;
    }

    public void setStartTime(Date start) {
        item.setStartTime(start);
    }

    public void setText(String text) {
        item.setText(text);
    }

}
