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

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEffect;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * This adapter class provides a default drag under effect (eg. select and
 * scroll) when a drag occurs over a <code>Planner</code>.
 * 
 * <p>
 * Classes that wish to provide their own drag under effect for a
 * <code>Planner</code> can extend this class, override the
 * <code>PlannerDropTargetEffect.dragOver</code> method and override any other
 * applicable methods in <code>PlannerDropTargetEffect</code> to display their
 * own drag under effect.
 * </p>
 * 
 * Subclasses that override any methods of this class should call the
 * corresponding <code>super</code> method to get the default drag under effect
 * implementation.
 * 
 * <p>
 * The feedback value is either one of the FEEDBACK constants defined in class
 * <code>DND</code> which is applicable to instances of this class, or it must
 * be built by <em>bitwise OR</em>'ing together (that is, using the
 * <code>int</code> "|" operator) two or more of those <code>DND</code> effect
 * constants.
 * </p>
 * <p>
 * <dl>
 * <dt><b>Feedback:</b></dt>
 * <dd>FEEDBACK_SELECT, FEEDBACK_SCROLL</dd>
 * </dl>
 * </p>
 * 
 * @see DropTargetAdapter
 * @see DropTargetEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * 
 * @since 3.3
 */
public class PlannerDropTargetEffect extends DropTargetEffect {

    static final int SCROLL_HYSTERESIS = 100; // milli seconds
    static final int SCROLL_TOLERANCE = 20; // pixels

    int currentOffset = -1;
    long scrollBeginTime;
    int scrollX = -1, scrollY = -1;
    Listener paintListener;

    /**
     * Creates a new <code>PlannerDropTargetEffect</code> to handle the drag
     * under effect on the specified <code>Planner</code>.
     * 
     * @param planner
     *            the <code>Planner</code> over which the user positions the
     *            cursor to drop the data
     */
    public PlannerDropTargetEffect(Planner planner) {
        super(planner);
        // Add a paint listener
        paintListener = new Listener() {
            public void handleEvent(Event event) {
                // if (currentOffset != -1) {
                Planner text = (Planner) getControl();
                // Point position = text.getLocationAtOffset(currentOffset);
                // int height = text.getLineHeight(currentOffset);
                // event.gc.setBackground(event.display.getSystemColor
                // (SWT.COLOR_BLACK));
                // event.gc.fillRectangle(100, 100, 50, 50);
                // System.out.println("print");
                // }
            }
        };
        getControl().addListener(SWT.Paint, paintListener);
    }

    public void dragEnter(DropTargetEvent event) {
        // FIXME
        _debug("PlannerDropTargetEffect.dragEnter", event);
        currentOffset = -1;
        scrollBeginTime = 0;
        scrollX = -1;
        scrollY = -1;
        //		getControl().removeListener(SWT.Paint, paintListener);
        //		getControl().addListener(SWT.Paint, paintListener);
    }

    private void _debug(String fctName, DropTargetEvent event) {
        // e DND#DROP_NONE
        // * @see DND#DROP_MOVE
        // * @see DND#DROP_COPY
        // * @see DND#DROP_LINK
        // * @see DND#DROP_DEFAULT
        String detail = (event.detail == DND.DROP_NONE ? "" : (((event.detail & DND.DROP_MOVE) == 0 ? "" : "DROP_MOVE ")
                + ((event.detail & DND.DROP_COPY) == 0 ? "" : "DROP_COPY ")
                + ((event.detail & DND.DROP_LINK) == 0 ? "" : "DROP_LINK ") + ((event.detail & DND.DROP_DEFAULT) == 0 ? "" : "DROP_DEFAULT ")));
        String operations = (event.operations == DND.DROP_NONE ? "" : (((event.operations & DND.DROP_MOVE) == 0 ? "" : "DROP_MOVE ")
                + ((event.operations & DND.DROP_COPY) == 0 ? "" : "DROP_COPY ")
                + ((event.operations & DND.DROP_LINK) == 0 ? "" : "DROP_LINK ") + ((event.operations & DND.DROP_DEFAULT) == 0 ? "" : "DROP_DEFAULT ")));
        // * @see DND#FEEDBACK_SELECT
        // * @see DND#FEEDBACK_INSERT_BEFORE
        // * @see DND#FEEDBACK_INSERT_AFTER
        // * @see DND#FEEDBACK_SCROLL
        // * @see DND#FEEDBACK_EXPAND
        String feedback = (event.feedback == DND.FEEDBACK_NONE ? "" : (((event.feedback & DND.FEEDBACK_SELECT) == 0 ? "" : "FEEDBACK_SELECT ")
                + ((event.feedback & DND.FEEDBACK_INSERT_BEFORE) == 0 ? "" : "FEEDBACK_INSERT_BEFORE ")
                + ((event.feedback & DND.FEEDBACK_INSERT_AFTER) == 0 ? "" : "FEEDBACK_INSERT_AFTER ") + ((event.feedback & DND.FEEDBACK_SCROLL) == 0
                ? ""
                : "FEEDBACK_SCROLL "))
                + ((event.feedback & DND.FEEDBACK_EXPAND) == 0 ? "" : "FEEDBACK_EXPAND "));
        String msg = String.format(
                "%s, x: %d, y:%d, detail:%s, operations:%s, feedback: %s, item:%s, currentDataType:%s",
                fctName,
                event.x,
                event.y,
                detail,
                operations,
                feedback,
                event.item != null ? event.item.toString() : "null",
                event.currentDataType != null ? event.currentDataType.toString() : "null");
        System.out.println(msg);
    }

    public void dragLeave(DropTargetEvent event) {
        // FIXME
        _debug("PlannerDropTargetEffect.dragLeave", event);

        Planner text = (Planner) getControl();
        if (currentOffset != -1) {
            // refreshCaret(text, currentOffset, -1);
        }
        //		text.removeListener(SWT.Paint, paintListener);
        scrollBeginTime = 0;
        scrollX = -1;
        scrollY = -1;
    }

    public void dragOver(DropTargetEvent event) {
        // FIXME
        _debug("PlannerDropTargetEffect.dragOver", event);

        int effect = event.feedback;
        Planner text = (Planner) getControl();

        Point pt = text.getDisplay().map(null, text, event.x, event.y);
        if ((effect & DND.FEEDBACK_SCROLL) == 0) {
            scrollBeginTime = 0;
            scrollX = scrollY = -1;
        } else {

            if (scrollX != -1
                    && scrollY != -1
                    && scrollBeginTime != 0
                    && (pt.x >= scrollX && pt.x <= (scrollX + SCROLL_TOLERANCE) || pt.y >= scrollY && pt.y <= (scrollY + SCROLL_TOLERANCE))) {
                if (System.currentTimeMillis() >= scrollBeginTime) {

                    Rectangle area = text.getClientArea();
                    GC gc = new GC(text);
                    FontMetrics fm = gc.getFontMetrics();
                    gc.dispose();
                    int charWidth = fm.getAverageCharWidth();
                    int scrollAmount = 10 * charWidth;
                    if (pt.x < area.x + 3 * charWidth) {
                        System.out.println("coucou1");
                        int leftPixel = text.getHorizontalPixel();
                        text.setHorizontalPixel(leftPixel - scrollAmount);
                    }
                    if (pt.x > area.width - 3 * charWidth) {
                        System.out.println("coucou2 " + scrollAmount);
                        int leftPixel = text.getHorizontalPixel();
                        text.setHorizontalPixel(leftPixel + scrollAmount);
                    }

                    int lineHeight = fm.getHeight();
                    if (pt.y < area.y + lineHeight) {
                        int topPixel = text.getVerticalPixel();
                        text.setVerticalPixel(topPixel - lineHeight);
                    }
                    if (pt.y > area.height - lineHeight) {
                        int topPixel = text.getVerticalPixel();
                        text.setVerticalPixel(topPixel + lineHeight);
                    }
                    scrollBeginTime = 0;
                    scrollX = scrollY = -1;
                }
            } else {
                scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
                scrollX = pt.x;
                scrollY = pt.y;
            }
            // }
        }

        if ((effect & DND.FEEDBACK_SELECT) != 0) {
            System.out.println("select");
            // int[] trailing = new int[1];
            // int newOffset = text.getOffsetAtPoint(pt.x, pt.y, trailing,
            // false);
            // newOffset += trailing[0];
            // if (newOffset != currentOffset) {
            // refreshCaret(text, currentOffset, newOffset);
            // currentOffset = newOffset;
            // }
        }
    }

    void refreshCaret(Planner planner, int oldOffset, int newOffset) {
        if (oldOffset != newOffset) {
            if (oldOffset != -1) {
                // Point oldPos = text.getLocationAtOffset(oldOffset);
                // int oldHeight = text.getLineHeight(oldOffset);
                // text.redraw (oldPos.x, oldPos.y, CARET_WIDTH, oldHeight,
                // false);
            }
            if (newOffset != -1) {
                // Point newPos = text.getLocationAtOffset(newOffset);
                // int newHeight = text.getLineHeight(newOffset);
                // text.redraw (newPos.x, newPos.y, CARET_WIDTH, newHeight,
                // false);
            }
        }
    }

    public void dropAccept(DropTargetEvent event) {
        // FIXME
        _debug("PlannerDropTargetEffect.dropAccept", event);
        if (currentOffset != -1) {
            Planner text = (Planner) getControl();
            text.setSelection(currentOffset);
            currentOffset = -1;
        }
    }
}
