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
package com.patrikdufresne.planner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * Define a default implementation of the planner look interface. This
 * implementation is used by default by the planner.
 * 
 * @author Patrik Dufresne
 * 
 */
public class DefaultPlannerLook implements PlannerLook {

    private DateFormat columnFormat1 = new SimpleDateFormat("EEE d");

    private DateFormat dayOfMonthFormat = new SimpleDateFormat("d");

    private Font font = Display.getDefault().getSystemFont();

    private Color gridColor = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);

    private Color itemBorderColor = Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION);

    private Color itemColor = Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND);

    private Color itemForeground = Display.getDefault().getSystemColor(SWT.COLOR_LIST_FOREGROUND);

    private Color selectedItemBorderColor = Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION);

    private Color selectedItemColor = Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION);

    private Color selectedItemForeground = Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);

    private DateFormat timeFormatter1 = new SimpleDateFormat("H'h'");

    private DateFormat timeFormatter2 = new SimpleDateFormat("H'h'mm");

    private Font titleFont = Display.getDefault().getSystemFont();

    private DateFormat titleFormat = new SimpleDateFormat("MMMM yyyy");

    /**
     * Create a planner look.
     */
    public DefaultPlannerLook() {
        // Nothing to do
    }

    @Override
    public String forDayOfMonth(Planner planner, Date t) {
        return dayOfMonthFormat.format(t);
    }

    @Override
    public String formatColumnTitle(Planner planner, Date t) {
        return columnFormat1.format(t);
    }

    @Override
    public String formatHour(Planner planner, Date t) {
        return this.timeFormatter1.format(t);
    }

    @Override
    public String formatItemTimeRange(Planner planner, Date startTime, Date endTime, boolean small) {
        if (small) {
            return String.format("(%s)", timeFormatter2.format(startTime));
        } else {
            return String.format("%s - %s", timeFormatter2.format(startTime), timeFormatter2.format(endTime));
        }

    }

    @Override
    public String formatTitle(Planner planner, Date start, Date End, Date curDateSelection) {
        String s = titleFormat.format(start);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    @Override
    public Color getGridColor(Planner planner) {
        return this.gridColor;
    }

    @Override
    public Color getItemBorderColor(Planner planner) {
        return this.itemBorderColor;
    }

    @Override
    public Color getItemColor(Planner planner) {
        return this.itemColor;
    }

    @Override
    public Font getItemFont(Planner planner) {
        return this.font;
    }

    @Override
    public Color getItemForeground(Planner planner) {
        return this.itemForeground;
    }

    @Override
    public Color getSelectedItemBorderColor(Planner planner) {
        return this.selectedItemBorderColor;
    }

    @Override
    public Color getSelectedItemColor(Planner planner) {
        return selectedItemColor;
    }

    @Override
    public Color getSelectedItemForeground(Planner planner) {
        return this.selectedItemForeground;
    }

    @Override
    public Font getTitleFont(Planner planner) {
        return this.titleFont;
    }

}
