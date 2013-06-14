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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.TypedListener;

/**
 * 
 * @author patapouf
 * 
 */
public class Planner extends Canvas {

    /**
     * Define the number of column in week style (value 7).
     */
    private static final int CELL_COL_COUNT = 7;
    /**
     * Define the minimum cell height for week style (in px).
     */
    private static final int CELL_MINIMUM_HEIGHT = 20;
    /**
     * Define the minimum cell width in week style (in px).
     */
    private static final int CELL_MINIMUM_WIDTH = 90;
    /**
     * Define the number of row in week style (value 48)
     */
    private static final int CELL_ROW_COUNT = 48;

    /**
     * In a situation where the cell are fill completely, we need a little space
     * to let the user create a new event.
     */
    private static final int CELL_SPACING = 10;

    /**
     * Define the line width of the grid.
     */
    private static final int GRID_LINE_WIDTH = 1;

    /**
     * Define the item's border weight
     */
    private static final int ITEM_BORDER = 1;
    /**
     * Define the height of a long event cell.
     */
    private static final int LONGEVENT_MINIMUM_CELL_HEIGHT = 20;

    private static final int LONGEVENT_TRESHOLD = 1000 * 60 * 60 * 24;
    /**
     * Page increment page for the scroll bars.
     */
    private static final int PAGE_INCREMENT_FACTOR = 3;

    private static final int SPACING = 2;

    /**
     * Check if the two time range intersect.
     * 
     * @param start1
     *            the start time for the first range
     * @param end1
     *            the endtime for the first range
     * @param start2
     *            the start time for the second range
     * @param end2
     *            the end time for the second range
     * @return True if the two time range intersect.
     */
    private static boolean intersect(Date start1, Date end1, Date start2, Date end2) {
        // TODO Use before & after function.
        if (start1 == null || end1 == null || start2 == null || end2 == null || start1.getTime() > end1.getTime() || start2.getTime() > end2.getTime()) {
            return false;
        }
        return start2.getTime() < end1.getTime() && start1.getTime() < end2.getTime();
    }

    /**
     * True if a border around the widget is visible.
     */
    private boolean borderVisible = false;
    private Calendar calendar;
    /**
     * Define the current cell's height.
     */
    private int cellHeight;

    /**
     * Define the current cell's width.
     */
    private int cellWidth = 0;

    /**
     * Define the fixed location of the corner.
     */
    private Rectangle cornerArea = new Rectangle(0, 0, 0, 0);

    /**
     * Define the day to display.
     */
    private Calendar curDateSelection;

    private int firstDayOfWeek = Calendar.SUNDAY;
    /**
     * Define the fixed location of the grid.
     */
    private Rectangle gridArea = new Rectangle(0, 0, 0, 0);

    /**
     * Define the scrolled location of the grid.
     */
    private Rectangle gridScrollRect = new Rectangle(0, 0, 0, 0);

    /**
     * Define the horizontal scroll offset.
     */
    private int horizontalScrollOffset = 0;

    private boolean inDispose = false;

    /**
     * Sets of item to display.
     */
    private List<PlannerItem> items = new ArrayList<PlannerItem>();
    /**
     * Define the fixed location of the left area.
     */
    private Rectangle leftArea = new Rectangle(0, 0, 0, 0);

    /**
     * Define the scrolled location of the left area.
     */
    private Rectangle leftScrollRect = new Rectangle(0, 0, 0, 0);

    private Listener listener;

    /**
     * Define the cell spacing required for long event.
     */
    private int LONG_EVENT_CELL_SPACING = 10;

    /**
     * Define the height of long event cells.
     */
    private int longEventCellHeight;

    /**
     * Define the with of long event cell. Should be the same as cellWidth.
     */
    private int longEventCellWidth;

    /**
     * Define the scrolled location of the long event.
     */
    private Rectangle longEventScrollRect = new Rectangle(0, 0, 0, 0);

    /**
     * Define the number of row required to display all the long event at the
     * top of the view.
     */
    private int longEventWEEK_CELL_ROW_COUNT;

    /**
     * Define the planner look.
     */
    private PlannerLook look;

    private int marginBottom = 0;

    private int marginLeft = 0;

    private int marginRight = 0;

    private int marginTop = 0;

    private Font oldFont;

    /**
     * Define the select item's index.
     */
    private int selectedIndex = -1;

    /**
     * Define the starting time displayed by this planner. This is define
     * according to the planner style.
     */
    private Date timeRangeEnd;

    /**
     * Define the starting time displayed by this planner. This is define
     * according to the planner style.
     */
    private Date timeRangeStart;

    /**
     * Define the fixed location of the top area.
     */
    private Rectangle topArea = new Rectangle(0, 0, 0, 0);

    /**
     * Define the scrolled location of the top area.
     */
    private Rectangle topScrollRect = new Rectangle(0, 0, 0, 0);

    /**
     * Define the vertical offset (according to the scroll bar).
     */
    private int verticalScrollOffset = 0;

    /**
     * Create a new planner
     * 
     * @param parent
     * @param style
     */
    public Planner(Composite parent, int style) {
        super(parent, style | SWT.DOUBLE_BUFFERED);

        // Keep reference to the original font
        // Used to know if it changed between each paint event.
        oldFont = getFont();

        // Keep an boolean value to know if a border is visible.
        borderVisible = (style & SWT.BORDER) != 0;

        // Set a default look
        look = new DefaultPlannerLook();
        setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND));

        // Add listener to the canvas
        installListeners();

        // Set the default time to display
        this.calendar = Calendar.getInstance();
        this.curDateSelection = Calendar.getInstance();
        updateDateTimeRange();

        setData("DEFAULT_DROP_TARGET_EFFECT", new PlannerDropTargetEffect(this));
    }

    String _getToolTip(int x, int y) {
        PlannerItem item = getItem(new Point(x, y));
        if (item == null) return null;
        return item.getToolTipText();
    }

    void _setToolTipText(int x, int y) {
        String oldTip = getToolTipText();
        String newTip = _getToolTip(x, y);
        if (newTip == null || !newTip.equals(oldTip)) {
            setToolTipText(newTip);
        }
    }

    /**
     * Adds the listener to the collection of listeners who will be notified
     * when the user changes the receiver's selection, by sending it one of the
     * messages defined in the <code>SelectionListener</code> interface.
     * 
     */
    public void addSelectionListener(SelectionListener listener) {
        checkWidget();
        if (listener == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }
        TypedListener typedListener = new TypedListener(listener);
        addListener(SWT.Selection, typedListener);
        addListener(SWT.DefaultSelection, typedListener);
    }

    /**
     * This function is used to calculate the current font size for the given
     * text
     * 
     * @param text
     *            the text or null to use space (" ").
     * @param font
     *            the font used to calculate the dimension or null to use the
     *            widget font.
     * @return the text dimension.
     */
    private Point calculateTextDimension(GC gc, String text, FontData[] fd) {
        Font font = null;
        boolean dispose = false;
        if (gc != null) {
            dispose = true;
            gc = new GC(this);
        }
        try {
            if (fd != null) {
                font = new Font(gc.getDevice(), fd);
                gc.setFont(font);
            }
            Point point = gc.textExtent(text == null ? " " : text);
            return point;
        } finally {
            if (font != null) {
                font.dispose();
            }
            if (dispose) {
                gc.dispose();
            }
        }
    }

    /**
     * Scrolls down the text to use new space made available by a resize or by
     * deleted lines.
     */
    private void claimBottomFreeSpace() {
        int clientAreaHeight = gridArea.height - marginTop - marginBottom;

        int newVerticalOffset = Math.max(0, gridScrollRect.height - clientAreaHeight);
        if (newVerticalOffset < verticalScrollOffset) {
            scrollVertical(newVerticalOffset - verticalScrollOffset, true);
        }
    }

    /**
     * Scrolls text to the right to use new space made available by a resize.
     */
    private void claimRightFreeSpace() {
        int newHorizontalOffset = Math.max(0, gridScrollRect.width - (gridArea.width - marginLeft - marginRight));
        if (newHorizontalOffset < horizontalScrollOffset) {
            // item is no longer drawn past the right border of the client area
            // align the right end of the item with the right border of the
            // client area (window is scrolled right).
            scrollHorizontal(newHorizontalOffset - horizontalScrollOffset, true);
        }
    }

    /**
     * Return the column index for the given day of week value.
     * 
     * @param dayOfWeek
     *            the day of week value as return by
     *            Calendar.get(Calendar.DAY_OF_WEEK)
     * @return The column index
     */
    private int colIdx(int dayOfWeek) {
        int diff = dayOfWeek - this.firstDayOfWeek;
        if (diff >= 0) {
            return diff;
        }
        return CELL_COL_COUNT - diff;
    }

    public Point computeSize(int wHint, int hHint, boolean changed) {
        Point size = new Point(wHint, hHint);
        size.x = Math.max(wHint, getLeftAreaWidth() + CELL_COL_COUNT * CELL_MINIMUM_WIDTH);
        size.y = Math.max(hHint, CELL_ROW_COUNT * CELL_MINIMUM_HEIGHT);
        return size;
    }

    /**
     * Create a new calendar instance initialized with the first day of week.
     * 
     * @return
     */
    private Calendar createCalendar(Date val) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(this.firstDayOfWeek);
        cal.setTime(val);
        return cal;
    }

    /**
     * Called by the PlannerItem constructor to notify this class about the item
     * being create in this widget.
     * 
     * @param item
     *            the item
     * @param index
     *            the position where to create the item
     */
    void createItem(PlannerItem item, int index) {
        if (0 > index || index > getItemCount()) SWT.error(SWT.ERROR_INVALID_RANGE);
        item.parent = this;

        items.add(index, item);

        // Redraw all
        updateItems();
        redraw();
    }

    /**
     * Called by the PlannerItem dispose function to notify this class about the
     * item being disposed.
     * 
     * @param item
     *            the item
     */
    void destroyItem(PlannerItem item) {
        if (inDispose) return;
        int index = indexOf(item);
        if (index == -1) return;

        if (items.size() == 1) {
            items.clear();
            selectedIndex = -1;
            setToolTipText(null);
            updateItems();
            redraw();
            return;
        }

        items.remove(index);
        if (index <= selectedIndex) selectedIndex--;

        updateItems();
        redraw();
    }

    /**
     * This function is used to draw the grid area. It's also draw the item
     * contains in it.
     * 
     * @param gc
     */
    private void drawGrid(GC gc) {
        // Keep old reference
        Color gcBackground = gc.getBackground();
        Color gcForeground = gc.getForeground();
        int gcLineStyle = gc.getLineStyle();
        int gcLineWidth = gc.getLineWidth();

        // Sets the grid color and line width
        gc.setForeground(look.getGridColor(this));
        gc.setLineWidth(GRID_LINE_WIDTH);

        // Create a path to hold all the grid
        Path path = new Path(gc.getDevice());

        // Add lines for each cell to the Path
        int x = gridScrollRect.x;
        int y = gridScrollRect.y;
        int width = gridScrollRect.width * 7;
        int height = gridScrollRect.height;
        // In DAY or WEEK style, every two line are drawn in dot
        // style
        for (int i = 0; i <= CELL_ROW_COUNT; i += 2) {
            int lineY = y + i * cellHeight;
            path.moveTo(x, lineY);
            path.lineTo(x + width, lineY);
        }
        Path dottedPath = new Path(gc.getDevice());
        for (int i = 1; i <= CELL_ROW_COUNT; i += 2) {
            int lineY = y + i * cellHeight;
            dottedPath.moveTo(x, lineY);
            dottedPath.lineTo(x + width, lineY);
        }
        gc.setLineStyle(SWT.LINE_DOT);
        gc.drawPath(dottedPath);
        dottedPath.dispose();

        // Draw lines to define columns
        int colWidth = gridScrollRect.width / CELL_COL_COUNT;
        for (int i = 1; i < CELL_COL_COUNT; i++) {
            int lineX = x + i * colWidth;
            path.moveTo(lineX, y);
            path.lineTo(lineX, y + height);
        }

        // Draw a rectangle around the grid
        path.addRectangle(gridScrollRect.x, gridScrollRect.y, gridScrollRect.width, gridScrollRect.height);

        // Draw the path
        gc.setLineStyle(SWT.LINE_SOLID);
        gc.drawPath(path);
        path.dispose();

        // Restore old values
        gc.setBackground(gcBackground);
        gc.setForeground(gcForeground);
        gc.setLineStyle(gcLineStyle);
        gc.setLineWidth(gcLineWidth);

        // Draw items
        for (int i = 0; i < items.size(); i++) {
            PlannerItem item = items.get(i);
            if (item.bounds != null && item.bounds.length > 0 && !item.inLongEventCells) {
                drawItem(gc, item, false, i == selectedIndex);
            }
        }

        // Restore old values
        gc.setBackground(gcBackground);
        gc.setForeground(gcForeground);
        gc.setLineStyle(gcLineStyle);
        gc.setLineWidth(gcLineWidth);

    }

    /**
     * Draw the given item into the grid area.
     * 
     * @param gc
     * @param item
     * @param selected
     */
    private void drawItem(GC gc, PlannerItem item, boolean small, boolean selected) {

        // Keep reference to old values
        Color gcBackground = gc.getBackground();
        Color gcForeground = gc.getForeground();
        int gcLineWidth = gc.getLineWidth();
        Font gcFont = gc.getFont();
        Rectangle gcClipping = gc.getClipping();

        // Create hours string
        String hours = look.formatItemTimeRange(this, item.startTime, item.endTime, small);

        // Draw each bound for the current item
        if (item.font != null) {
            gc.setFont(item.font);
        } else {
            // Set it to the default item font
            gc.setFont(look.getItemFont(this));
        }
        TextLayout textLayout = new TextLayout(gc.getDevice());
        for (Rectangle bound : item.bounds) {
            /*
             * Draw rectangle
             */
            if (selected) {
                gc.setBackground(look.getSelectedItemColor(this));
                gc.setForeground(look.getSelectedItemBorderColor(this));
            } else {
                gc.setBackground(item.background != null ? item.background : look.getItemColor(this));
                gc.setForeground(item.borderColor != null ? item.borderColor : look.getItemBorderColor(this));
            }
            gc.setLineWidth(ITEM_BORDER);
            gc.fillRectangle(bound.x, bound.y, bound.width, bound.height);
            gc.drawRectangle(bound.x, bound.y, bound.width, bound.height);
            gc.setLineWidth(gcLineWidth);

            /*
             * Draw hours (e.g.: 11am - 2pm)
             */
            if (selected) {
                gc.setForeground(look.getSelectedItemForeground(this));
            } else {
                gc.setForeground(item.foreground != null ? item.foreground : look.getItemForeground(this));
            }
            int x = bound.x + ITEM_BORDER + SPACING;
            int y = bound.y + SPACING;
            gc.setClipping(x, y, bound.width - SPACING - 1, bound.height - SPACING);
            // gc.drawText(hours, x, y, SWT.DRAW_TRANSPARENT);

            /*
             * Draw the event text
             */
            if (!small) {
                x = bound.x + ITEM_BORDER + SPACING;
                y = bound.y + SPACING + SPACING;
                int width = bound.width - ITEM_BORDER * 2 - SPACING * 2;
                if ((item.getStyle() & SWT.WRAP) != 0) {
                    textLayout.setWidth(width);
                    textLayout.setText(hours + "\r\n" + item.getText());
                    textLayout.draw(gc, x, y);
                } else {
                    gc.drawText(hours + "\r\n" + item.getText(), x, y, SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER);
                }
            } else {
                // In small mode, draw the text aside the hours
                x = bound.x + ITEM_BORDER + SPACING;
                y = bound.y + SPACING;
                gc.drawText(hours + " " + item.getText().replaceAll("\r\n|\r|\n", " "), x, y, SWT.DRAW_TRANSPARENT);
            }
            gc.setClipping(gcClipping);
        }

        // Restore old values
        gc.setBackground(gcBackground);
        gc.setForeground(gcForeground);
        gc.setLineWidth(gcLineWidth);
        gc.setFont(gcFont);
        gc.setClipping(gcClipping);

        textLayout.dispose();
    }

    /**
     * Draw the hours on the left.
     * 
     * @param gc
     */
    private void drawLeft(GC gc) {
        // Keep reference to old values
        Color gcBackground = gc.getBackground();
        Color gcForeground = gc.getForeground();

        // Draw background
        gc.setBackground(getBackground());
        gc.fillRectangle(leftArea);

        // Draw text hours on the left
        gc.setForeground(getForeground());
        this.calendar.setTime(timeRangeStart);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        for (int row = 0; row < CELL_ROW_COUNT; row += 2) {
            this.calendar.set(Calendar.HOUR_OF_DAY, hour);
            String text = look.formatHour(this, calendar.getTime());

            // Align the text on the right edge
            Point size = gc.textExtent(text);
            gc.drawText(text, leftArea.x + leftArea.width - size.x - SPACING, leftScrollRect.y + cellHeight * row);
            hour++;
        }

        // Restore old values
        gc.setBackground(gcBackground);
        gc.setForeground(gcForeground);
    }

    /**
     * Draw the calendar header (to contains the long events).
     * 
     * @param gc
     */
    private void drawTop(GC gc) {
        // Keep reference on old value
        Color gcForeground = gc.getForeground();
        Color gcBackground = gc.getBackground();
        Font gcFont = gc.getFont();

        // Fill the background
        gc.setBackground(getBackground());
        gc.setForeground(getForeground());
        gc.fillRectangle(topArea);

        /*
         * Draw the title centered at the top
         */
        String title = look.formatTitle(this, timeRangeStart, timeRangeEnd, curDateSelection.getTime());
        gc.setFont(this.look.getTitleFont(this));
        Point titleSize = gc.textExtent(title);
        gc.drawText(title, topScrollRect.x + (topScrollRect.width - titleSize.x) / 2, topScrollRect.y + SPACING);

        /*
         * Draw the column's title (e.g.: Mon. 4)
         */
        gc.setFont(this.look.getItemFont(this));
        // Pre-calculate the time range represented by a column.
        this.calendar.setTime(timeRangeStart);
        int y = topScrollRect.y + SPACING + titleSize.y + SPACING;
        for (int col = 0; col < CELL_COL_COUNT; col++) {
            String itemTitle = look.formatColumnTitle(this, this.calendar.getTime());
            Point itemSize = gc.textExtent(itemTitle);
            gc.drawText(itemTitle, topScrollRect.x + (cellWidth * col) + (cellWidth - itemSize.x) / 2, y);
            this.calendar.add(Calendar.DATE, 1);
        }

        /*
         * Draw the long event Cells
         */
        Path path = new Path(gc.getDevice());
        gc.setForeground(look.getGridColor(this));
        for (int col = 0; col < CELL_COL_COUNT; col++) {
            path.addRectangle(longEventScrollRect.x + col * longEventCellWidth, longEventScrollRect.y, longEventCellWidth, longEventScrollRect.height);
        }
        gc.drawPath(path);
        path.dispose();

        gc.setBackground(gcBackground);
        gc.setForeground(gcForeground);

        // Draw long events
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).bounds != null && items.get(i).bounds.length > 0 && items.get(i).inLongEventCells) {
                drawItem(gc, items.get(i), true, i == selectedIndex);
            }
        }

        // Fill the corner
        gc.fillRectangle(cornerArea);

        // Reset the old values
        gc.setForeground(gcForeground);
        gc.setBackground(gcBackground);
        gc.setFont(gcFont);

    }

    public Date getDateSelection() {
        return curDateSelection.getTime();
    }

    /**
     * Returns the ending date displayed by this planner according to the
     * planner style and the date selection.
     * 
     * @return the ending date
     * @see Planner#setDateSelection(Date)
     * @see Planner#setPlannerStyle(int)
     * 
     */
    public Date getEndDate() {
        return timeRangeEnd;
    }

    /**
     * Returns the horizontal scroll offset relative to the start of the line.
     * 
     * @return the horizontal scroll offset relative to the start of the line,
     *         measured in pixel starting at 0, if > 0 the content is scrolled.
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public int getHorizontalPixel() {
        checkWidget();
        return horizontalScrollOffset;
    }

    /**
     * Return the tab that is located at the specified index.
     * 
     * @param index
     *            the index of the tab item
     * @return the item at the specified index
     * 
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_INVALID_RANGE - if the index is out of range</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong
     *                thread</li>
     *                <li>ERROR_WIDGET_DISPOSED when the widget has been
     *                disposed</li>
     *                </ul>
     */
    public PlannerItem getItem(int index) {
        // checkWidget();
        if (index < 0 || index >= items.size()) SWT.error(SWT.ERROR_INVALID_RANGE);
        return items.get(index);
    }

    /**
     * Gets the item at a point in the widget.
     * 
     * @param pt
     *            the point in coordinates relative to the CTabFolder
     * @return the item at a point or null
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong
     *                thread</li>
     *                <li>ERROR_WIDGET_DISPOSED when the widget has been
     *                disposed</li>
     *                </ul>
     */
    public PlannerItem getItem(Point pt) {
        checkWidget();
        PlannerItem item = null;
        for (PlannerItem i : items) {
            for (Rectangle bound : i.bounds) {
                if (bound.contains(pt)) {
                    item = i;
                }
            }
        }
        return item;
    }

    /**
     * Return the number of tabs in the folder.
     * 
     * @return the number of tabs in the folder
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong
     *                thread</li>
     *                <li>ERROR_WIDGET_DISPOSED when the widget has been
     *                disposed</li>
     *                </ul>
     */
    public int getItemCount() {
        // checkWidget();
        return items.size();
    }

    /**
     * Return the tab items.
     * 
     * @return the tab items
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong
     *                thread</li>
     *                <li>ERROR_WIDGET_DISPOSED when the widget has been
     *                disposed</li>
     *                </ul>
     */
    public PlannerItem[] getItems() {
        // checkWidget();
        PlannerItem[] tabItems = new PlannerItem[items.size()];
        return items.toArray(tabItems);
    }

    /**
     * Calculate the required width of the left area to display the hours.
     * 
     * @return
     */
    private int getLeftAreaWidth() {
        GC gc = new GC(this);
        int max = 0;
        // TODO Extract the method here
        this.calendar.setTime(timeRangeStart);
        for (int row = 0; row < CELL_ROW_COUNT; row += 2) {
            max = Math.max(max, gc.textExtent(look.formatHour(this, this.calendar.getTime())).x);
            this.calendar.add(Calendar.HOUR_OF_DAY, 1);
        }
        gc.dispose();
        return max;
    }

    /**
     * Return the planner look.
     * 
     * @return
     */
    public PlannerLook getLook() {
        return look;
    }

    /**
     * Return the selected tab item, or an empty array if there is no selection.
     * 
     * @return the selected tab item
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong
     *                thread</li>
     *                <li>ERROR_WIDGET_DISPOSED when the widget has been
     *                disposed</li>
     *                </ul>
     */
    public PlannerItem getSelection() {
        // checkWidget();
        if (selectedIndex == -1) return null;
        return items.get(selectedIndex);
    }

    /**
     * Return the index of the selected tab item, or -1 if there is no
     * selection.
     * 
     * @return the index of the selected tab item or -1
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong
     *                thread</li>
     *                <li>ERROR_WIDGET_DISPOSED when the widget has been
     *                disposed</li>
     *                </ul>
     */
    public int getSelectionIndex() {
        // checkWidget();
        return selectedIndex;
    }

    /**
     * Returns the starting date displayed by this Planner according to the
     * planner style and the date selection.
     * 
     * @return the starting date
     * @see Planner#setDateSelection(Date)
     * @see Planner#setPlannerStyle(int)
     */
    public Date getStartDate() {
        return timeRangeStart;
    }

    /**
     * This implementation of the return the style of the planner.
     * 
     * @see org.eclipse.swt.widgets.Widget#getStyle()
     */
    public int getStyle() {
        int style = super.getStyle();
        if (borderVisible) style |= SWT.BORDER;
        return style;
    }

    /**
     * Gets the vertical= pixel.
     * 
     * @return pixel position of the grid's top
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public int getVerticalPixel() {
        checkWidget();
        return verticalScrollOffset;
    }

    /**
     * Scrolls the widget horizontally.
     */
    void handleHorizontalScroll(Event event) {
        int scrollPixel = getHorizontalBar().getSelection() - horizontalScrollOffset;
        scrollHorizontal(scrollPixel, false);
    }

    // private GregorianCalendar getTimeFromPoint(int x, int y) {
    // FIXME
    // if (!gridArea.contains(x, y))
    // return null;
    //
    // int index = 0;
    // while (index < cells.length && !cells[index].contains(x, y))
    // index++;
    //
    // if (index >= cells.length)
    // return null;
    //
    // GregorianCalendar time = new GregorianCalendar();
    // time.setTime(dates[index].getTime());
    //
    // int yRel = (y - centerScrollRect.y);
    // int box = (yRel / LINE_HEIGHT) * 10;
    //
    // time.set(Calendar.HOUR_OF_DAY, box / 20);
    // time.set(Calendar.MINUTE, (box % 20) == 0 ? 0 : 30);
    // time.set(Calendar.SECOND, 0);
    //
    // return time;
    // return null;
    // }

    /**
     * Scrolls the widget vertically.
     */
    void handleVerticalScroll(Event event) {
        int scrollPixel = getVerticalBar().getSelection() - verticalScrollOffset;
        scrollVertical(scrollPixel, false);
    }

    /**
     * Return the index of the specified tab or -1 if the tab is not in the
     * receiver.
     * 
     * @param item
     *            the tab item for which the index is required
     * 
     * @return the index of the specified tab item or -1
     * 
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
     *                </ul>
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong
     *                thread</li>
     *                <li>ERROR_WIDGET_DISPOSED when the widget has been
     *                disposed</li>
     *                </ul>
     */
    public int indexOf(PlannerItem item) {
        checkWidget();
        if (item == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) == item) return i;
        }
        return -1;
    }

    /**
     * Adds event listeners
     */
    private void installListeners() {
        listener = new Listener() {
            public void handleEvent(Event event) {
                switch (event.type) {
                case SWT.Dispose:
                    onDispose(event);
                    break;
                // case SWT.DragDetect:
                // onDragDetect(event);
                // break;
                // case SWT.FocusIn:
                // onFocus(event);
                // break;
                // case SWT.FocusOut:
                // onFocus(event);
                // break;
                case SWT.KeyDown:
                    onKeyDown(event);
                    break;
                case SWT.MouseDoubleClick:
                    onMouseDoubleClick(event);
                    break;
                case SWT.MouseDown:
                    onMouse(event);
                    break;
                case SWT.MouseEnter:
                    onMouse(event);
                    break;
                case SWT.MouseExit:
                    onMouse(event);
                    break;
                case SWT.MouseMove:
                    onMouse(event);
                    break;
                case SWT.MouseUp:
                    onMouse(event);
                    break;
                case SWT.Paint:
                    onPaint(event);
                    break;
                case SWT.Resize:
                    onResize();
                    break;
                case SWT.Traverse:
                    onTraverse(event);
                    break;
                }
            }
        };
        int[] events = new int[] {
                SWT.Dispose,
                SWT.DragDetect,
                SWT.FocusIn,
                SWT.FocusOut,
                SWT.KeyDown,
                SWT.MouseDoubleClick,
                SWT.MouseDown,
                SWT.MouseEnter,
                SWT.MouseExit,
                SWT.MouseMove,
                SWT.MouseUp,
                SWT.Paint,
                SWT.Resize,
                SWT.Traverse, };
        for (int i = 0; i < events.length; i++) {
            addListener(events[i], listener);
        }

        // Get scroll bar (only available if style include H_SCROLL and
        // V_SCROLL)

        ScrollBar verticalBar = getVerticalBar();
        if (verticalBar != null) {
            verticalBar.setIncrement(CELL_MINIMUM_HEIGHT);
            verticalBar.setPageIncrement(CELL_MINIMUM_HEIGHT * 3);
            verticalBar.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event event) {
                    handleVerticalScroll(event);
                }
            });
        }
        ScrollBar horizontalBar = getHorizontalBar();
        if (horizontalBar != null) {
            horizontalBar.setIncrement(CELL_MINIMUM_HEIGHT);
            verticalBar.setPageIncrement(CELL_MINIMUM_HEIGHT * PAGE_INCREMENT_FACTOR);
            horizontalBar.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event event) {
                    handleHorizontalScroll(event);
                }
            });
        }
    }

    /**
     * Check if the two items are compatible to fit on the same line.
     * <p>
     * In DAY or WEEK style, two items are compatible if the date-time doesn't
     * intersect.
     * <p>
     * In MONTH style, two items are compatible if they occurred on different
     * days.
     * 
     * @param item
     *            an item
     * 
     * @param item2
     *            an other item
     * 
     * @param longEvent
     *            True if the item should be consider long event
     * 
     * @return True if the items doesn't intersect, False otherwise.
     */
    private boolean isCompatible(PlannerItem item, PlannerItem item2, boolean longEvent) {
        Date start1 = item.startTime;
        Date end1 = item.endTime;
        Date start2 = item2.startTime;
        Date end2 = item2.endTime;
        return !intersect(start1, end1, start2, end2);
    }

    /**
     * Check if the given item is 'compatible' with other items. By compatible,
     * it's mean there is not intersection.
     * 
     * @param item
     *            the item to check
     * @param items
     *            items to compare with
     * @param longEvents
     *            True if the item should be consider long event
     * 
     * @return True if the items doesn't intersect, False otherwise.
     */
    private boolean isItemCompatibleWith(PlannerItem item, Collection<PlannerItem> items, boolean longEvents) {

        for (PlannerItem item2 : items) {
            if (!isCompatible(item, item2, longEvents)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the given item is a short event. A short event is define by it's
     * time range being smaller then <code>longEventThreshold</code>.
     * 
     * @param item
     *            the item to check
     * @return True if the item represent a short event
     */
    private boolean isShortEvent(PlannerItem item) {
        return item.endTime.getTime() - item.startTime.getTime() < LONGEVENT_TRESHOLD;
    }

    /**
     * Layout the areas : Left, top and the grid area. The left area display the
     * hours. The top area display the columns names. The grid area display the
     * events
     * 
     * @return True if the area as moved.
     */
    private boolean layoutAreas() {
        boolean changed = false;

        // Pre-calculate the left area's width
        int leftAreaWidth = getLeftAreaWidth() + SPACING;

        // Pre-calculate the top area header height
        GC gc = new GC(this);
        int itemTextHeight = calculateTextDimension(gc, null, this.look.getItemFont(this).getFontData()).y;
        int titleTextHeight = calculateTextDimension(gc, null, this.look.getTitleFont(this).getFontData()).y;
        gc.dispose();
        gc = null;

        // Define the required size of the top area
        Rectangle newTopArea = new Rectangle(0, 0, 0, 0);
        newTopArea.x = leftAreaWidth;
        newTopArea.y = 0;
        newTopArea.width = getClientArea().width - leftAreaWidth;
        newTopArea.height = SPACING
                + titleTextHeight
                + SPACING
                + itemTextHeight
                + SPACING
                + Math.max(LONGEVENT_MINIMUM_CELL_HEIGHT, itemTextHeight + SPACING)
                * longEventWEEK_CELL_ROW_COUNT
                + LONG_EVENT_CELL_SPACING
                + SPACING;

        // Define the required size of the left area
        Rectangle newLeftArea = new Rectangle(0, 0, 0, 0);
        newLeftArea.x = 0;
        newLeftArea.y = 0 + newTopArea.height;
        newLeftArea.width = leftAreaWidth;
        newLeftArea.height = getClientArea().height - newTopArea.height;

        // Sets the rest of the space to the grid area
        Rectangle newGridArea = new Rectangle(0, 0, 0, 0);
        newGridArea.x = 0 + newLeftArea.width;
        newGridArea.y = 0 + newTopArea.height;
        newGridArea.width = getClientArea().width - newLeftArea.width;
        newGridArea.height = getClientArea().height - newTopArea.height;

        // Define the corner area
        cornerArea.x = 0;
        cornerArea.y = 0;
        cornerArea.width = newLeftArea.width;
        cornerArea.height = newTopArea.height;

        // Using the gridArea, define the position of the grid according to the
        // scroll bars. We also need to respect the minimum cell dimension.
        gridScrollRect.x = newGridArea.x - horizontalScrollOffset;
        gridScrollRect.y = newGridArea.y - verticalScrollOffset;
        gridScrollRect.width = Math.max(newGridArea.width, CELL_MINIMUM_WIDTH * CELL_COL_COUNT);
        gridScrollRect.height = Math.max(newGridArea.height, CELL_MINIMUM_HEIGHT * CELL_ROW_COUNT);

        // Using the leftArea, calculate the position of the left pane according
        // to the scroll bars.
        leftScrollRect.x = newLeftArea.x;
        leftScrollRect.y = newLeftArea.y - verticalScrollOffset;
        leftScrollRect.width = newLeftArea.width;
        leftScrollRect.height = gridScrollRect.height;

        // Using topArea, define the position of the top rectangle according to
        // the scroll bars.
        topScrollRect.x = newTopArea.x - horizontalScrollOffset;
        topScrollRect.y = newTopArea.y;
        topScrollRect.width = gridScrollRect.width;
        topScrollRect.height = newTopArea.height;

        // Update the topArea define the position of the long event according to
        // the scroll bars.
        longEventScrollRect.x = topScrollRect.x;
        longEventScrollRect.y = SPACING + titleTextHeight + SPACING + itemTextHeight + SPACING;
        longEventScrollRect.width = topScrollRect.width;
        longEventScrollRect.height = Math.max(LONGEVENT_MINIMUM_CELL_HEIGHT, itemTextHeight + SPACING) * longEventWEEK_CELL_ROW_COUNT + LONG_EVENT_CELL_SPACING;

        // Check if any of the areas has changed
        if (!newTopArea.equals(topArea)) changed = true;
        if (!newLeftArea.equals(leftArea)) changed = true;
        if (!newGridArea.equals(gridArea)) changed = true;

        topArea = newTopArea;
        leftArea = newLeftArea;
        gridArea = newGridArea;

        return changed;
    }

    /**
     * This function will return an array of rectangle corresponding to the
     * given time range. To provide this information, this function take in
     * consideration the planner style.
     * <p>
     * This function is mostly a proxy for other layout* functions.
     * 
     * @param start
     *            the start time
     * @param end
     *            the end time
     * @param longEvent
     *            True if the event should be rendered in the top area
     * @return rectangles associated with the given time range.
     */
    private Rectangle[] layoutItem(Date start, Date end, boolean longEvent) {
        // Check the date rage value
        if (start.getTime() > end.getTime()) {
            throw new IllegalArgumentException();
        }
        if (longEvent) {
            return layoutItemLongEvent(start, end);
        }
        return layoutItemShortEvent(start, end);
    }

    /**
     * This function will return an arrays of rectangle corresponding to the
     * given time range. The rectangle will be located in the longEvent cells.
     * 
     * @param start
     *            the event's start date
     * @param end
     *            the event's end date
     * @return the rectangles to represent the event
     */
    private Rectangle[] layoutItemLongEvent(Date start, Date end) {
        Rectangle rect;

        // Compute the start of the rectangle
        calendar.setTime(start);
        if (calendar.compareTo(createCalendar(timeRangeStart)) < 0) {
            calendar.setTime(timeRangeStart);
        }
        int col = colIdx(calendar.get(Calendar.DAY_OF_WEEK));
        int x = longEventScrollRect.x + (int) (col * longEventCellWidth);
        rect = new Rectangle(x, longEventScrollRect.y, 0, longEventCellHeight);

        // Compute the end of the rectangle
        calendar.setTime(end);
        int endCol;
        if (calendar.compareTo(createCalendar(timeRangeEnd)) >= 0) {
            calendar.setTime(timeRangeEnd);
            endCol = CELL_COL_COUNT;
        } else {
            endCol = colIdx(calendar.get(Calendar.DAY_OF_WEEK));
        }
        rect.width = longEventScrollRect.x + (int) (endCol * longEventCellWidth) - rect.x;
        return new Rectangle[] { rect };
    }

    /**
     * This function define the location of the areas and the items.
     * 
     * @return
     */
    private boolean layoutItems() {
        boolean changed = false;

        // Split items in two list : short and long events. This way, the long
        // events may be layout in the longEvent cells.
        List<PlannerItem> shortEvents = new ArrayList<PlannerItem>();
        List<PlannerItem> longEvents = new ArrayList<PlannerItem>();
        for (PlannerItem item : items) {
            // Check if the item need to be displayed within the current time
            // range
            if (intersect(item.startTime, item.endTime, this.timeRangeStart, this.timeRangeEnd)) {
                // Check if the item is short or long
                if (isShortEvent(item)) {
                    item.inLongEventCells = false;
                    shortEvents.add(item);
                } else {
                    item.inLongEventCells = true;
                    longEvents.add(item);
                }
            } else {
                // Remove all bounds
                item.bounds = new Rectangle[0];
            }
        }

        // To layout the items, we create a simple representation of it in two
        // dimension using a Map. This map will hold reference to the the items
        // and define in which columns the item should be layout.
        List<List<PlannerItem>> shortEventsLayout = new ArrayList<List<PlannerItem>>();
        while (0 < shortEvents.size()) {
            List<PlannerItem> dayEvents = new ArrayList<PlannerItem>();
            for (PlannerItem item : shortEvents) {
                if (isItemCompatibleWith(item, dayEvents, false)) {
                    dayEvents.add(item);
                }
            }
            shortEvents.removeAll(dayEvents);
            shortEventsLayout.add(dayEvents);
        }

        // Repeat the same step for long events.
        List<List<PlannerItem>> longEventsLayout = new ArrayList<List<PlannerItem>>();
        while (0 < longEvents.size()) {
            List<PlannerItem> dayEvents = new ArrayList<PlannerItem>();
            for (PlannerItem item : longEvents) {
                if (isItemCompatibleWith(item, dayEvents, true)) {
                    dayEvents.add(item);
                }
            }
            longEvents.removeAll(dayEvents);
            longEventsLayout.add(dayEvents);
        }

        // Sets the number of row required for long event area.
        this.longEventWEEK_CELL_ROW_COUNT = longEventsLayout.size();

        // Layout all areas
        changed |= layoutAreas();

        // Calculate the cell width and height
        cellWidth = gridScrollRect.width / CELL_COL_COUNT;
        cellHeight = gridScrollRect.height / CELL_ROW_COUNT;
        longEventCellWidth = cellWidth;
        if (longEventWEEK_CELL_ROW_COUNT > 0) {
            longEventCellHeight = (longEventScrollRect.height - LONG_EVENT_CELL_SPACING) / longEventWEEK_CELL_ROW_COUNT;
        } else {
            longEventCellHeight = 0;
        }

        /*
         * Start layout short items in grid.
         */

        // Loop on each rows
        changed |= layoutItems(shortEventsLayout, false);
        changed |= layoutItems(longEventsLayout, true);

        return changed;
    }

    /**
     * @param changed
     * @param eventsLayout
     * @return
     */
    private boolean layoutItems(List<List<PlannerItem>> eventsLayout, boolean longEventCell) {

        boolean changed = false;
        int rowIndex = 0;

        // Pre-calculate the available space to share between events
        int availableSpace = 0;
        int dayOfMonthHeight = 0;
        if (!longEventCell) {
            availableSpace = cellWidth - CELL_SPACING;
        } else {
            availableSpace = longEventScrollRect.height - LONG_EVENT_CELL_SPACING;
        }

        for (List<PlannerItem> row : eventsLayout) {

            // Loop on each item
            for (PlannerItem item : row) {

                // Get rectangles associated to the item's time range.
                Rectangle[] newBounds = layoutItem(item.startTime, item.endTime, longEventCell);

                // Check if the bound may be extend to fill other columns
                int extendTo = rowIndex + 1;
                boolean isCompatible = true;
                while (isCompatible && extendTo < eventsLayout.size()) {
                    int j = 0;
                    List<PlannerItem> otherItems = eventsLayout.get(extendTo);
                    while (isCompatible && j < otherItems.size()) {
                        PlannerItem item2 = otherItems.get(j);
                        isCompatible &= !intersect(item.startTime, item.endTime, item2.startTime, item2.endTime);
                        j++;
                    }
                    extendTo++;
                }
                if (!isCompatible) {
                    extendTo--;
                }
                // Those rectangles need to be adapted according to our layout.
                for (Rectangle newBound : newBounds) {
                    if (!longEventCell) {
                        // adjust the width.
                        newBound.x = newBound.x + (availableSpace / eventsLayout.size()) * rowIndex;
                        newBound.width = availableSpace / eventsLayout.size() * (extendTo - rowIndex);
                        if (rowIndex < eventsLayout.size() && extendTo < eventsLayout.size()) {
                            newBound.width *= 1.7;
                        }
                    } else {
                        // adjust the height
                        newBound.y = newBound.y + dayOfMonthHeight + (availableSpace / eventsLayout.size()) * rowIndex;
                        newBound.height = availableSpace / eventsLayout.size();
                    }
                }

                // Assign the new bound to the item.
                changed |= !Arrays.equals(item.bounds, newBounds);
                item.bounds = newBounds;
            }
            rowIndex++;
        }
        return changed;
    }

    /**
     * @param start
     * @param end
     * @param rects
     * @return
     */
    private Rectangle[] layoutItemShortEvent(Date start, Date end) {

        List<Rectangle> rects = new ArrayList<Rectangle>();

        // Compute start column & row
        calendar.setTime(start);
        if (calendar.compareTo(createCalendar(timeRangeStart)) < 0) {
            calendar.setTime(timeRangeStart);
        }
        int startCol = colIdx(calendar.get(Calendar.DAY_OF_WEEK));
        int startRow = 2 * calendar.get(Calendar.HOUR_OF_DAY) + (calendar.get(Calendar.MINUTE) < 30 ? 0 : 1);

        // Compute rectangle area
        int x = gridScrollRect.x + (int) (startCol * cellWidth);
        int y = gridScrollRect.y + startRow * cellHeight;
        Rectangle rect = new Rectangle(x, y, cellWidth, cellHeight);

        // Compute end column & row
        calendar.setTime(end);
        int endCol;
        int endRow;
        if (calendar.compareTo(createCalendar(timeRangeEnd)) >= 0) {
            calendar.setTime(timeRangeEnd);
            endCol = CELL_COL_COUNT - 1;
            endRow = CELL_ROW_COUNT;
        } else {
            endCol = colIdx(calendar.get(Calendar.DAY_OF_WEEK));
            endRow = 2 * calendar.get(Calendar.HOUR_OF_DAY) + (calendar.get(Calendar.MINUTE) < 30 ? 0 : 1);
        }

        // Loop over each row
        for (int colIndex = startCol; colIndex < endCol; colIndex++) {
            // Set the width to fill the rest of the row.
            rect.height = gridScrollRect.y + CELL_ROW_COUNT * cellHeight - rect.y;
            // Add the current rect to the list and create a new rectangle
            // for next day
            rects.add(rect);
            // Create a new rectangle for the row
            x = gridScrollRect.x + (colIndex + 1) * cellWidth;
            y = gridScrollRect.y;
            rect = new Rectangle(x, y, cellWidth, 0);
        }

        y = gridScrollRect.y + (int) (endRow * cellHeight);
        rect.height = y - rect.y;

        if (rect != null && rect.height != 0) {
            rects.add(rect);
        }

        Rectangle[] a = new Rectangle[rects.size()];
        return rects.toArray(a);
    }

    void onDispose(Event event) {
        removeListener(SWT.Dispose, listener);
        notifyListeners(SWT.Dispose, event);
        event.type = SWT.None;
        /*
         * Usually when an item is disposed, destroyItem will change the size of
         * the items array, reset the bounds of all the tabs and manage the
         * widget associated with the tab. Since the whole folder is being
         * disposed, this is not necessary. For speed the inDispose flag is used
         * to skip over this part of the item dispose.
         */
        inDispose = true;
        int length = items.size();
        for (int i = 0; i < length; i++) {
            if (items.get(i) != null) {
                items.get(i).dispose();
            }
        }
    }

    void onKeyDown(Event event) {

        if (event.character == '\r' && selectedIndex != -1) {
            Event newEvent = new Event();
            newEvent.item = getItem(selectedIndex);
            notifyListeners(SWT.DefaultSelection, newEvent);
            return;
        }

        switch (event.keyCode) {
        case SWT.ARROW_LEFT:
        case SWT.ARROW_RIGHT:
            int count = items.size();
            if (count == 0) return;
            if (selectedIndex == -1) return;
            int leadKey = (getStyle() & SWT.RIGHT_TO_LEFT) != 0 ? SWT.ARROW_RIGHT : SWT.ARROW_LEFT;
            int offset = event.keyCode == leadKey ? -1 : 1;
            int index;
            index = selectedIndex + offset;
            if (index < 0 || index >= count) return;
            setSelection(index, true);
            forceFocus();
        }

    }

    void onMouse(Event event) {
        int x = event.x, y = event.y;
        switch (event.type) {
        case SWT.MouseEnter:

            break;
        case SWT.MouseExit:

            break;
        case SWT.MouseDown: {
            if (event.button != 1) return;

            PlannerItem item = null;
            for (PlannerItem i : items) {
                for (Rectangle bound : i.bounds) {
                    if (bound.contains(x, y)) {
                        item = i;
                    }
                }
            }

            if (item != null) {
                int index = indexOf(item);
                int oldSelectedIndex = selectedIndex;
                setSelection(index, true);
                if (oldSelectedIndex == selectedIndex) {
                    /*
                     * If the click is on the selected planneritem, then set
                     * focus to the planner
                     */
                    forceFocus();
                }

                return;
            }
            break;
        }
        case SWT.MouseMove:
            _setToolTipText(event.x, event.y);
            break;

        case SWT.MouseUp: {

            PlannerItem item = getItem(new Point(x, y));
            int index = -1;
            if (item != null) index = indexOf(item);
            if (selectedIndex != index) {
                selectedIndex = index;
                Event selEvent = new Event();
                selEvent.widget = this;
                selEvent.item = item;
                selEvent.type = SWT.Selection;
                notifyListeners(SWT.Selection, selEvent);
                redraw();
            }
            break;
        }
        }
    }

    void onMouseDoubleClick(Event event) {
        if (event.button != 1 || (event.stateMask & SWT.BUTTON2) != 0 || (event.stateMask & SWT.BUTTON3) != 0) return;
        Event e = new Event();
        e.item = getItem(new Point(event.x, event.y));
        if (e.item != null) {

            if (isListening(SWT.DefaultSelection)) {
                Event newEvent = new Event();
                newEvent.item = e.item;
                notifyListeners(SWT.DefaultSelection, newEvent);
            }
        }
    }

    boolean onPageTraversal(Event event) {
        int count = items.size();
        if (count == 0) return false;
        int index = selectedIndex;
        if (index == -1) {
            index = 0;
        } else {
            int offset = (event.detail == SWT.TRAVERSE_PAGE_NEXT) ? 1 : -1;
            index = (selectedIndex + offset + count) % count;
        }
        setSelection(index, true);
        return true;
    }

    void onPaint(Event event) {
        if (inDispose) return;
        Font font = getFont();
        if (oldFont == null || !oldFont.equals(font)) {
            // handle case where default font changes
            oldFont = font;
            if (!layoutItems()) {
                updateItems();
                redraw();
                return;
            }
        }

        // Keep track of the original values
        GC gc = event.gc;
        Font gcFont = gc.getFont();
        Color gcBackground = gc.getBackground();
        Color gcForeground = gc.getForeground();

        // Draw the background
        gc.setBackground(getBackground());
        gc.fillRectangle(0, 0, getSize().x, getSize().y);

        // Draw the grid area
        drawGrid(gc);

        // Draw the left area (if required)
        drawLeft(gc);

        // Draw the top area
        drawTop(gc);

        // Restore old gc value
        gc.setFont(gcFont);
        gc.setForeground(gcForeground);
        gc.setBackground(gcBackground);
    }

    void onResize() {

        int oldHeight = gridArea.height;
        int oldWidth = gridArea.width;

        updateItems();

        /* Redraw the old or new right/bottom margin if needed */
        if (oldWidth != gridArea.width) {
            if (marginRight > 0) {
                int x = (oldWidth < gridArea.width ? oldWidth : gridArea.width) - marginRight;
                redraw(x, 0, marginRight, oldHeight, false);
            }
        }
        if (oldHeight != gridArea.height) {
            if (marginBottom > 0) {
                int y = (oldHeight < gridArea.height ? oldHeight : gridArea.height) - marginBottom;
                redraw(0, y, oldWidth, marginBottom, false);
            }
        }

        setScrollBars(true);
        claimRightFreeSpace();
        // Allows any value for horizontalScrollOffset when clientArea
        // is zero in setHorizontalPixel() and setHorisontalOffset().
        if (gridArea.width != 0) {
            ScrollBar horizontalBar = getHorizontalBar();
            if (horizontalBar != null && horizontalBar.getVisible()) {
                if (horizontalScrollOffset != horizontalBar.getSelection()) {
                    horizontalBar.setSelection(horizontalScrollOffset);
                    horizontalScrollOffset = horizontalBar.getSelection();
                }
            }
        }
        claimBottomFreeSpace();
    }

    void onTraverse(Event event) {
        switch (event.detail) {
        case SWT.TRAVERSE_ESCAPE:
        case SWT.TRAVERSE_RETURN:
        case SWT.TRAVERSE_TAB_NEXT:
        case SWT.TRAVERSE_TAB_PREVIOUS:
            Control focusControl = getDisplay().getFocusControl();
            if (focusControl == this) event.doit = true;
            break;
        case SWT.TRAVERSE_PAGE_NEXT:
        case SWT.TRAVERSE_PAGE_PREVIOUS:
            event.doit = onPageTraversal(event);
            event.detail = SWT.TRAVERSE_NONE;
            break;
        }
    }

    /**
     * Removes the listener from the collection of listeners who will be
     * notified when the user changes the receiver's selection.
     */
    public void removeSelectionListener(SelectionListener listener) {
        checkWidget();
        if (listener == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }
        removeListener(SWT.Selection, listener);
        removeListener(SWT.DefaultSelection, listener);
    }

    /**
     * Scrolls the widget horizontally.
     * 
     * @param pixels
     *            number of pixels to scroll, > 0 = scroll left, < 0 scroll
     *            right
     * @param adjustScrollBar
     *            true= the scroll thumb will be moved to reflect the new scroll
     *            offset. false = the scroll thumb will not be moved
     * @return true=the widget was scrolled false=the widget was not scrolled,
     *         the given offset is not valid.
     */
    boolean scrollHorizontal(int pixels, boolean adjustScrollBar) {
        if (pixels == 0) {
            return false;
        }
        ScrollBar horizontalBar = getHorizontalBar();
        if (horizontalBar != null && adjustScrollBar) {
            horizontalBar.setSelection(horizontalScrollOffset + pixels);
        }
        int scrollTop = marginTop;
        int scrollHeight = gridArea.height + topArea.height - marginBottom - marginTop;
        if (pixels > 0) {
            int sourceX = gridArea.x + pixels;
            int scrollWidth = gridArea.width;

            if (scrollWidth > 0) {
                scroll(gridArea.x, scrollTop, sourceX, scrollTop, scrollWidth, scrollHeight, false);
            }
            if (sourceX > scrollWidth) {
                super.redraw(gridArea.x, scrollTop, pixels - scrollWidth, scrollHeight, true);
            }
        } else {
            int destinationX = gridArea.x - pixels;
            int scrollWidth = gridArea.width;
            if (scrollWidth > 0) {
                scroll(destinationX, scrollTop, gridArea.x, scrollTop, scrollWidth, scrollHeight, false);
            }
            if (destinationX > scrollWidth) {
                super.redraw(gridArea.x, scrollTop, -pixels - scrollWidth, scrollHeight, true);
            }
        }
        horizontalScrollOffset += pixels;
        updateItems();
        return true;
    }

    /**
     * Scrolls the widget vertically.
     * 
     * @param pixel
     *            the new vertical scroll offset
     * @param adjustScrollBar
     *            true= the scroll thumb will be moved to reflect the new scroll
     *            offset. false = the scroll thumb will not be moved
     * @return true=the widget was scrolled false=the widget was not scrolled
     */
    boolean scrollVertical(int pixels, boolean adjustScrollBar) {
        if (pixels == 0) {
            return false;
        }
        if (verticalScrollOffset != -1) {
            ScrollBar verticalBar = getVerticalBar();
            if (verticalBar != null && adjustScrollBar) {
                verticalBar.setSelection(verticalScrollOffset + pixels);
            }
            int scrollTop = gridArea.y;
            int scrollWidth = gridArea.width + leftArea.width - marginLeft - marginRight;
            if (pixels > 0) {
                int sourceY = scrollTop + pixels;
                int scrollHeight = gridArea.height;
                if (scrollHeight > 0) {
                    scroll(marginLeft, scrollTop, marginLeft, sourceY, scrollWidth, scrollHeight, true);
                }
                if (sourceY > scrollHeight) {
                    int redrawY = Math.max(0, scrollTop + scrollHeight);
                    int redrawHeight = Math.min(gridArea.height, pixels - scrollHeight);
                    super.redraw(marginLeft, redrawY, scrollWidth, redrawHeight, true);
                }
            } else {
                int destinationY = scrollTop - pixels;
                int scrollHeight = gridArea.height;
                if (scrollHeight > 0) {
                    scroll(marginLeft, destinationY, marginLeft, scrollTop, scrollWidth, scrollHeight, true);
                }
                if (destinationY > scrollHeight) {
                    int redrawY = Math.max(0, scrollTop + scrollHeight);
                    int redrawHeight = Math.min(gridArea.height, -pixels - scrollHeight);
                    redraw();
                    super.redraw(marginLeft, redrawY, scrollWidth, redrawHeight, true);
                }
            }
            verticalScrollOffset += pixels;
            updateItems();
        } else {
            super.redraw();
        }

        return true;

    }

    public void setBackground(Color color) {
        super.setBackground(color);
        redraw();
    }

    /**
     * Sets the planner to display the given date.
     */
    public void setDateSelection(Date date) {
        checkWidget();

        this.calendar.setTime(date);

        // Check if the date changed.
        if (this.calendar.compareTo(this.curDateSelection) == 0) {
            // Nothing to do
            return;
        }

        // keep reference to the new date to display
        this.curDateSelection = (Calendar) calendar.clone();

        // Update the time range to display
        boolean changed = updateDateTimeRange();
        if (changed) {
            // The planner need to notify listener about the time range change.
            notifyListeners(SWT.Modify, new Event());

            // Re-layout the items
            updateItems();
            redraw();
        }

    }

    /**
     * Define the font used to draw the hours of an event.
     */
    // private Font itemFont;

    public void setFont(Font font) {
        checkWidget();
        if (font != null && font.equals(getFont())) return;
        super.setFont(font);
        oldFont = getFont();
        updateItems();
        redraw();
    }

    public void setForeground(Color color) {
        super.setForeground(color);
        redraw();
    }

    /**
     * Sets the horizontal pixel offset relative to the start of the line. Do
     * nothing if there is no text set.
     * <p>
     * <b>NOTE:</b> The horizontal pixel offset is reset to 0 when new text is
     * set in the widget.
     * </p>
     * 
     * @param pixel
     *            horizontal pixel offset relative to the start of the line.
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     */
    public void setHorizontalPixel(int pixel) {
        checkWidget();
        if (pixel < 0) {
            pixel = 0;
        }

        if (pixel < 0) {
            pixel = 0;
        }
        // allow any value if client area width is unknown or 0.
        // offset will be checked in resize handler.
        // don't use isVisible since width is known even if widget
        // is temporarily invisible
        if (gridArea.width > 0) {

            // prevent scrolling if the content fits in the client area.
            // align end of longest line with right border of client area
            // if offset is out of range.
            if (pixel > gridScrollRect.width - gridArea.width) {
                pixel = Math.max(0, gridScrollRect.width - gridArea.width);
            }
        }
        scrollHorizontal(pixel - horizontalScrollOffset, true);
    }

    /**
     * Sets the planner look. Allow user to change the color and font of the
     * planner.
     * 
     * @param look
     */
    public void setLook(PlannerLook look) {
        this.look = look;
    }

    /**
     * Adjusts the maximum and the page size of the scroll bars to reflect
     * content width/length changes.
     * 
     * @param vertical
     *            indicates if the vertical scrollbar also needs to be set
     */
    void setScrollBars(boolean vertical) {
        int inactive = 1;
        if (vertical) {
            ScrollBar verticalBar = getVerticalBar();
            if (verticalBar != null) {
                int maximum = gridScrollRect.height;
                // only set the real values if the scroll bar can be used
                // (ie. because the thumb size is less than the scroll maximum)
                // avoids flashing on Motif, fixes 1G7RE1J and 1G5SE92
                if (gridArea.height < maximum) {
                    verticalBar.setMaximum(maximum);
                    verticalBar.setThumb(gridArea.height);
                    verticalBar.setPageIncrement(gridArea.height);
                } else if (verticalBar.getThumb() != inactive || verticalBar.getMaximum() != inactive) {
                    verticalBar.setValues(verticalBar.getSelection(), verticalBar.getMinimum(), inactive, inactive, verticalBar.getIncrement(), inactive);
                }
            }
        }
        ScrollBar horizontalBar = getHorizontalBar();
        if (horizontalBar != null) {
            int maximum = gridScrollRect.width;
            // only set the real values if the scroll bar can be used
            // (ie. because the thumb size is less than the scroll maximum)
            // avoids flashing on Motif, fixes 1G7RE1J and 1G5SE92
            if (gridArea.width < maximum) {
                horizontalBar.setMaximum(maximum);
                horizontalBar.setThumb(gridArea.width - marginLeft - marginRight);
                horizontalBar.setPageIncrement(gridArea.width - marginLeft - marginRight);
                horizontalBar.setVisible(true);
            } else if (horizontalBar.getThumb() != inactive || horizontalBar.getMaximum() != inactive) {
                horizontalBar.setValues(horizontalBar.getSelection(), horizontalBar.getMinimum(), inactive, inactive, horizontalBar.getIncrement(), inactive);
                horizontalBar.setVisible(false);
            }
        }
    }

    /**
     * Set the selection to the tab at the specified index.
     * 
     * @param index
     *            the index of the tab item to be selected
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public void setSelection(int index) {
        checkWidget();

        if (index >= 0 && index < items.size()) {
            PlannerItem selection = items.get(index);
            if (selectedIndex == index) {
                showItem(selection);
                return;
            }

            selectedIndex = index;

            showItem(selection);
            redraw();
        } else if (index == -1) {
            selectedIndex = index;
            redraw();
        }
    }

    void setSelection(int index, boolean notify) {
        int oldSelectedIndex = selectedIndex;
        setSelection(index);
        if (notify && selectedIndex != oldSelectedIndex && selectedIndex != -1) {
            Event event = new Event();
            event.item = getItem(selectedIndex);
            notifyListeners(SWT.Selection, event);
        }
    }

    /**
     * Set the selection to the tab at the specified item.
     * 
     * @param item
     *            the tab item to be selected
     * 
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
     *                </ul>
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong
     *                thread</li>
     *                <li>ERROR_WIDGET_DISPOSED when the widget has been
     *                disposed</li>
     *                </ul>
     */
    public void setSelection(PlannerItem item) {
        checkWidget();
        if (item == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        int index = indexOf(item);
        setSelection(index);
    }

    /**
     * Sets the vertical pixel offset.
     * 
     * @param pixel
     *            new vertical pixel offset. Must be between 0 and ?. An out of
     *            range offset will be adjusted accordingly.
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     */
    public void setVerticalPixel(int pixel) {
        checkWidget();
        if (pixel < 0) {
            pixel = 0;
        }

        if (pixel < 0) {
            pixel = 0;
        }
        // allow any value if client area height is unknown or 0.
        // offset will be checked in resize handler.
        // don't use isVisible since width is known even if widget
        // is temporarily invisible
        if (gridArea.height > 0) {

            // prevent scrolling if the content fits in the client area.
            // align end of longest line with right border of client area
            // if offset is out of range.
            if (pixel > gridScrollRect.height - gridArea.height) {
                pixel = Math.max(0, gridScrollRect.height - gridArea.height);
            }
        }
        scrollVertical(pixel - verticalScrollOffset, true);
    }

    /**
     * Shows the item. If the item is already showing in the receiver, this
     * method simply returns. Otherwise, the items are scrolled until the item
     * is visible.
     * 
     * @param item
     *            the item to be shown
     * 
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
     *                <li>ERROR_INVALID_ARGUMENT - if the item has been disposed
     *                </li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @see CTabFolder#showSelection()
     * 
     * @since 2.0
     */
    public void showItem(PlannerItem item) {
        checkWidget();
        if (item == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        if (item.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        int index = indexOf(item);
        if (index == -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

        // TODO Complete this function.

        if (item.bounds != null && item.bounds.length > 0) return;
        updateItems();
        redraw();
    }

    /**
     * Shows the selection. If the selection is already showing in the receiver,
     * this method simply returns. Otherwise, the items are scrolled until the
     * selection is visible.
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @see CTabFolder#showItem(CTabItem)
     * 
     * @since 2.0
     */
    public void showSelection() {
        checkWidget();
        if (selectedIndex != -1) {
            showItem(getSelection());
        }
    }

    /**
     * This function is used internally to update the value of timeRangeStart
     * and timeRangeEnd.
     * 
     * @return True if the time range changed
     */
    private boolean updateDateTimeRange() {

        this.calendar.setTime(curDateSelection.getTime());
        int week = this.calendar.get(Calendar.WEEK_OF_YEAR);
        // First day of week already sets
        this.calendar.set(Calendar.MILLISECOND, 0);
        this.calendar.set(Calendar.SECOND, 0);
        this.calendar.set(Calendar.MINUTE, 0);
        this.calendar.set(Calendar.HOUR_OF_DAY, 0);
        this.calendar.set(Calendar.DAY_OF_WEEK, this.firstDayOfWeek);
        this.calendar.set(Calendar.WEEK_OF_YEAR, week);

        Date start = this.calendar.getTime();
        this.calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date end = this.calendar.getTime();

        // Check if the time range has changed
        boolean change = this.timeRangeStart == null || !this.timeRangeStart.equals(start) || this.timeRangeEnd == null || !this.timeRangeEnd.equals(end);
        this.timeRangeStart = start;
        this.timeRangeEnd = end;
        return change;

    }

    /**
     * Update the location of all items.
     */
    boolean updateItems() {
        boolean changed = layoutItems();
        if (changed && getToolTipText() != null) {
            Point pt = getDisplay().getCursorLocation();
            pt = toControl(pt);
        }
        return changed;
    }
}