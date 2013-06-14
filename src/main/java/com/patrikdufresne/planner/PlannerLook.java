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

import java.util.Date;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * A Planner look is used to define the look of a Planner widget by defining the
 * font and color to use while drawing the widget.
 * 
 * @author Patrik Dufresne
 * 
 */
public interface PlannerLook {
    /**
     * This function is used to format the day of month
     * 
     * @param t
     * @return
     */
    String forDayOfMonth(Planner planner, Date t);

    /**
     * This function is used to format the column title.
     * 
     * @param t
     * @return
     */
    String formatColumnTitle(Planner planner, Date t);

    /**
     * This function is used to format the hours displayed on the left of a
     * planner with Day or WEEK style.
     * 
     * @param t
     */
    String formatHour(Planner planner, Date t);

    /**
     * This function is used to format the hours label to be displayed for the
     * item.
     * 
     * @param startTime
     *            the item start time
     * @param endTime
     *            the item end time
     * @param small
     *            True if the event is drawn as a small event
     * @return
     */
    String formatItemTimeRange(Planner planner, Date startTime, Date endTime, boolean small);

    /**
     * This function is used to format the planner title to display the
     * currently displayed time range.
     * 
     * @param curDateSelection
     * @return
     */
    String formatTitle(Planner planner, Date start, Date End, Date curDateSelection);

    /**
     * This function return the color used to draw the grid.
     * 
     * @return
     */
    Color getGridColor(Planner planner);

    /**
     * This function return the default color used to draw the item border. This
     * value may be overridden by the item's border color (if not null).
     * 
     * @return a color
     */
    Color getItemBorderColor(Planner planner);

    /**
     * This function return the default color used to draw the item body. Take
     * note, this value may be override by the item's background color (if not
     * null).
     * 
     * @return a color
     */
    Color getItemColor(Planner planner);

    /**
     * This function return the default font used to draw the item's text. This
     * value may be override by the item's font (if not null).
     * 
     * @return
     */
    Font getItemFont(Planner planner);

    /**
     * This function return the default color used to draw the item's text. This
     * value may be override by the item's foreground (if not null).
     * 
     * @return
     */
    Color getItemForeground(Planner planner);

    /**
     * Return the color used to draw the border of selected items.
     * 
     * @param planner
     *            the planner
     * @return the color
     */
    Color getSelectedItemBorderColor(Planner planner);

    /**
     * Return the color used to draw selected items.
     * 
     * @param planner
     *            the planner
     * @return the color
     */
    Color getSelectedItemColor(Planner planner);

    /**
     * This function return the font used to draw the title text.
     * 
     * @return
     */
    Font getTitleFont(Planner planner);

    /**
     * Return the color used to draw selected item's text.
     * 
     * @param planner
     *            the planer
     * @return the color
     */
    Color getSelectedItemForeground(Planner planner);

}