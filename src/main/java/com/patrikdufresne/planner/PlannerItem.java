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

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Item;

/**
 * Instances of this class represent a selectable user interface object
 * corresponding to an event in a planner.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>WRAP : to display the item's text in wrap mode.</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class PlannerItem extends Item {

    private static int checkStyle(int style) {
        return style & SWT.WRAP;
    }

    /**
     * The color used to draw the background of this item.
     */
    protected Color background;
    /**
     * The color used to draw the border of this item.
     */
    protected Color borderColor;

    /**
     * The rectangles where to draw the item in the receiver.
     */
    Rectangle[] bounds;

    /**
     * The event's end time.
     */
    protected Date endTime;
    /**
     * Font used to draw the text of this item.
     */
    protected Font font;
    /**
     * The color used to draw the foreground color of this item.
     */
    protected Color foreground;
    /**
     * True if the item should be drawn in the long event cell area.
     */
    boolean inLongEventCells;
    /**
     * The event's parent widget (a planner).
     */
    protected Planner parent;
    /**
     * The event's start time.
     */
    protected Date startTime;

    /**
     * Item's tool tip
     */
    protected String toolTipText;

    /**
     * True to enable the item text wrapping.
     */
    protected boolean wrap;

    /**
     * Create a new Planner item
     * 
     * @param parent
     *            a Planner which will be the parent of the new instance (cannot
     *            be null)
     * @param style
     *            the style of control to construct
     */
    public PlannerItem(Planner parent, int style) {
        this(parent, style, parent.getItemCount());
        this.wrap = (style & SWT.WRAP) != 0;
    }

    /**
     * Create a new Planner item
     * 
     * @param parent
     *            a Planner which will be the parent of the new instance (cannot
     *            be null)
     * @param style
     *            the style of control to construct
     * @param index
     *            the zero-relative index to store the receiver in its parent
     */
    public PlannerItem(Planner parent, int style, int index) {
        super(parent, checkStyle(style), index);
        // Create the item
        parent.createItem(this, index);
    }

    /**
     * Free the item resources.
     */
    public void dispose() {
        if (isDisposed()) return;
        parent.destroyItem(this);
        super.dispose();
        parent = null;
        background = null;
        borderColor = null;
        foreground = null;
        bounds = null;
    }

    /**
     * Returns the receiver's background color.
     * 
     * @return the background color
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public Color getBackground() {
        checkWidget();
        return background;
    }

    /**
     * Returns the receiver's border color.
     * 
     * @return the border color
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public Color getBorderColor() {
        checkWidget();
        return borderColor;
    }

    /**
     * Rectangles where to draw the item.
     * 
     * @return array of rectangle
     */
    public Rectangle[] getBounds() {
        return bounds;
    }

    public Date getEndTime() {
        return endTime;
    }

    /**
     * Returns the font that the receiver will use to paint textual information.
     * 
     * @return the receiver's font
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @since 3.0
     */
    public Font getFont() {
        checkWidget();
        if (font != null) return font;
        return parent.getFont();
    }

    /**
     * Returns the foreground color that the receiver will use to draw.
     * 
     * @return the receiver's foreground color
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public Color getForeground() {
        checkWidget();
        return foreground;
    }

    /**
     * Returns the receiver's parent, which must be a <code>CTabFolder</code>.
     * 
     * @return the receiver's parent
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public Planner getParent() {
        checkWidget();
        return parent;
    }

    public Date getStartTime() {
        return startTime;
    }

    /**
     * This implementation include the wrapping flags.
     */
    public int getStyle() {
        return super.getStyle() | (this.wrap ? SWT.WRAP : SWT.NONE);
    }

    /**
     * Return the text wrapping mode.
     * 
     * @return True if the wrapping is enabled.
     */
    public boolean getTextWrap() {
        return this.wrap;
    }

    /**
     * Returns the receiver's tool tip text, or null if it has not been set.
     * 
     * @return the receiver's tool tip text
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public String getToolTipText() {
        checkWidget();
        if (toolTipText == null) {
            return getText();
        }
        return toolTipText;
    }

    /**
     * Sets the receiver's background color to the color specified by the
     * argument, or to the default system color for the item if the argument is
     * null.
     * 
     * @param color
     *            the new color (or null)
     * 
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
     *                disposed</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public void setBackground(Color color) {
        checkWidget();
        if (color != null && color.isDisposed()) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        // Check if the background value changed
        if (background == null && color == null || background != null && background.equals(color)) return;
        background = color;
        parent.redraw();
    }

    /**
     * Sets the receiver's border color to the color specified by the argument,
     * or to the default system color for the item if the argument is null.
     * 
     * @param color
     *            the new color (or null)
     * 
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
     *                disposed</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public void setBorderColor(Color color) {
        checkWidget();
        if (color != null && color.isDisposed()) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        // Check if the background value changed
        if (borderColor == null && color == null || borderColor != null && borderColor.equals(color)) return;
        borderColor = color;
        parent.redraw();
    }

    public void setEndTime(Date end) {
        checkWidget();
        if (end == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        if (end.equals(getText())) return;
        endTime = end;
        parent.updateItems();
        parent.redraw();
    }

    /**
     * Sets the font that the receiver will use to paint textual information for
     * this item to the font specified by the argument, or to the default font
     * for that kind of control if the argument is null.
     * 
     * @param font
     *            the new font (or null)
     * 
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
     *                disposed</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @since 3.0
     */
    public void setFont(Font font) {
        checkWidget();
        if (font != null && font.isDisposed()) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        if (font == null && this.font == null) return;
        if (font != null && font.equals(this.font)) return;
        this.font = font;
        if (parent.updateItems()) {
            parent.redraw();
        }
    }

    /**
     * Sets the receiver's foreground color to the color specified by the
     * argument, or to the default system color for the item if the argument is
     * null.
     * 
     * @param color
     *            the new color (or null)
     * 
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
     *                disposed</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @since 2.0
     */
    public void setForground(Color color) {
        checkWidget();
        if (color != null && color.isDisposed()) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        // Check if the background value changed
        if (foreground == null && color == null || foreground != null && foreground.equals(color)) return;
        foreground = color;
        parent.redraw();
    }

    public void setStartTime(Date start) {
        checkWidget();
        if (start == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        if (start.equals(getText())) return;
        startTime = start;
        parent.updateItems();
        parent.redraw();
    }

    /**
     * Sets the receiver's text.
     * 
     * @param string
     *            the new text
     * 
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the text is null</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public void setText(String string) {
        if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        if (string.equals(getText())) return;
        super.setText(string);
        parent.redraw();
    }

    /**
     * Sets the wrapping mode.
     * 
     * @param wrap
     */
    public void setTextWrap(boolean wrap) {
        this.wrap = wrap;
    }

    @Override
    public String toString() {
        return "PlannerItem [text=" + getText() + ", startTime=" + startTime + ", endTime=" + endTime + "]";
    }
}
