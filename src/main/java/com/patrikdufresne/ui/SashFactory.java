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
package com.patrikdufresne.ui;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;

/**
 * Utility class used to create sash form control for different usages.
 * 
 * @author Patrik Dufresne
 * 
 */
public class SashFactory {

    /**
     * This class is an adaptation of the ResizeListener class from TrayDialog
     * to avoid resizing the left pane.
     * 
     * @author Patrik Dufresne
     * 
     */
    private static class ResizeListener extends ControlAdapter {

        // accumulating
        private final Composite composite;

        int compositeWidth;
        private final GridData data;

        // to tray when resizing
        private int remainder = 0; // Used to prevent rounding errors from

        private int tray_ratio = 0; // Percentage of extra width devoted

        public ResizeListener(GridData data, Composite shell, int tray_ratio) {
            this.data = data;
            this.composite = shell;
            this.compositeWidth = shell.getSize().x;
            this.tray_ratio = tray_ratio;
        }

        @Override
        public void controlResized(ControlEvent event) {
            int newWidth = this.composite.getSize().x;
            if (newWidth != this.compositeWidth) {
                int shellWidthIncrease = newWidth - this.compositeWidth;
                int trayWidthIncreaseTimes100 = (shellWidthIncrease * this.tray_ratio) + this.remainder;
                int trayWidthIncrease = trayWidthIncreaseTimes100 / 100;
                this.remainder = trayWidthIncreaseTimes100 - (100 * trayWidthIncrease);
                this.data.widthHint = this.data.widthHint + trayWidthIncrease;
                this.compositeWidth = newWidth;
                if (!this.composite.isDisposed()) {
                    this.composite.layout();
                }
            }
        }
    }

    /**
     * Minimum width value (value 10).
     */
    private static final int MIN_WIDTH = 10;

    /**
     * Create a sash control with all the required component to allow the user
     * to drag the sash and resize the panes.
     * <p>
     * The given composite <code>parent</code> should have exactly two children.
     * The first children will be used as the left pane. The layout on the
     * parent will be changed to a grid layout.
     * 
     * @param parent
     *            the composite parent
     * @param separator
     *            True to create a separator representing the sash.
     */
    public static void createLeftPane(final Composite parent, boolean separator) {
        createLeftPane(parent, separator, null, null);
    }

    /**
     * Create a sash control with all the required component to allow the user
     * to drag the sash and resize the panes.
     * <p>
     * The given composite <code>parent</code> should have exactly two children.
     * The first children will be used as the left pane. The layout on the
     * parent will be changed to a grid layout.
     * <p>
     * If provided, the width of the pane will be stored and restored from the
     * preference store.
     * 
     * @param parent
     *            the composite parent
     * @param separator
     *            True to create a separator representing the sash.
     * @param prefStore
     *            The preference store used to store and restore the pane width
     * @param prefKey
     *            the preference key
     */
    public static void createLeftPane(final Composite parent, boolean separator, final IPreferenceStore prefStore, final String prefKey) {
        createPane(parent, separator, prefStore, prefKey, SWT.LEFT, null);
    }

    /**
     * Create a sash control with all the required component to allow the user
     * to drag the sash and resize the panes.
     * <p>
     * The given composite <code>parent</code> should have exactly two children.
     * The first children will be used as the left pane. The layout on the
     * parent will be changed to a grid layout.
     * <p>
     * If provided, the width of the pane will be stored and restored from the
     * preference store.
     * 
     * @param parent
     *            the composite parent
     * @param separator
     *            True to create a separator representing the sash.
     * @param prefStore
     *            The preference store used to store and restore the pane width
     * @param prefKey
     *            the preference key
     * @param defaultWidth
     *            the default width if the preference is not set or null
     */
    public static void createLeftPane(final Composite parent, boolean separator, final IPreferenceStore prefStore, final String prefKey, int defaultWidth) {
        createPane(parent, separator, prefStore, prefKey, SWT.LEFT, Integer.valueOf(defaultWidth));
    }

    /**
     * Create a sash control with all the required component to allow the user
     * to drag the sash and resize the panes.
     * <p>
     * The given composite <code>parent</code> should have exactly two children.
     * The last children will be used as the right pane. The layout on the
     * parent will be changed to a grid layout.
     * <p>
     * If provided, the width of the pane will be stored and restored from the
     * preference store.
     * 
     * @param parent
     *            the composite parent
     * 
     * @param separator
     *            True to create a separator representing the sash.
     * @param prefStore
     *            The preference store used to store and restore the pane width
     * @param prefKey
     *            the preference key
     */
    private static void createPane(
            final Composite parent,
            boolean separator,
            final IPreferenceStore prefStore,
            final String prefKey,
            final int side,
            Integer defaultWidth) {

        // Check if the parent only has two children
        if (parent.getChildren().length != 2) {
            throw new IllegalArgumentException("Wrong number of children."); //$NON-NLS-1$
        }

        // Sets the parents layout.
        GridLayout grid = new GridLayout();
        grid.makeColumnsEqualWidth = false;
        grid.horizontalSpacing = 0;
        grid.marginWidth = 0;
        grid.marginHeight = 0;
        grid.numColumns = separator ? 4 : 3;
        parent.setLayout(grid);

        // Set the layout of the main control
        Control main = side == SWT.RIGHT ? parent.getChildren()[0] : parent.getChildren()[1];
        main.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Get reference to the pane.
        Control trayControl = side == SWT.RIGHT ? parent.getChildren()[1] : parent.getChildren()[0];

        // Create the ash control
        final Sash sash = new Sash(parent, SWT.VERTICAL);
        sash.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        if (side == SWT.RIGHT) {
            sash.moveBelow(main);
        } else {
            sash.moveAbove(main);
        }

        // Create the separator if required.
        final Label labelSeparator = separator ? new Label(parent, SWT.SEPARATOR | SWT.VERTICAL) : null;
        if (labelSeparator != null) {
            labelSeparator.setLayoutData(new GridData(GridData.FILL_VERTICAL));
            if (side == SWT.RIGHT) {
                labelSeparator.moveBelow(sash);
            } else {
                labelSeparator.moveAbove(sash);
            }
        }

        // Sets the layout data to the pane.
        Rectangle clientArea = parent.getClientArea();
        final GridData data = new GridData(GridData.FILL_VERTICAL);
        if (defaultWidth != null && defaultWidth.intValue() > MIN_WIDTH) {
            data.widthHint = defaultWidth.intValue();
        }
        if (prefStore != null && prefKey != null && prefStore.contains(prefKey) && prefStore.getInt(prefKey) > MIN_WIDTH) {
            data.widthHint = prefStore.getInt(prefKey);
        }
        data.widthHint = Math.max(data.widthHint, trayControl.computeSize(SWT.DEFAULT, clientArea.height).x);
        trayControl.setLayoutData(data);

        // Add a selection listener to allow the user to resize the sash.
        sash.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (event.detail != SWT.DRAG) {
                    int newWidth;
                    if (side == SWT.RIGHT) {
                        Rectangle clientArea = parent.getClientArea();
                        newWidth = clientArea.width - event.x - (sash.getSize().x + (labelSeparator != null ? labelSeparator.getSize().x : 0));
                    } else {
                        newWidth = event.x;
                    }
                    if (newWidth != data.widthHint) {
                        data.widthHint = newWidth;
                        parent.layout();
                    }
                }
            }
        });

        // Add a resize listener to update the pane size according to the new
        // size.
        parent.addControlListener(new ResizeListener(data, parent, 0));

        // Add a dispose listener to store the widthHint
        if (prefStore != null && prefKey != null) {
            sash.addListener(SWT.Dispose, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    prefStore.setValue(prefKey, data.widthHint);
                }
            });
        }
    }

    /**
     * Create a sash control with all the required component to allow the user
     * to drag the sash and resize the panes.
     * <p>
     * The given composite <code>parent</code> should have exactly two children.
     * The last children will be used as the right pane. The layout on the
     * parent will be changed to a grid layout.
     * 
     * @param parent
     *            the composite parent
     * @param separator
     *            True to create a separator representing the sash.
     */
    public static void createRightPane(final Composite parent, boolean separator) {
        createRightPane(parent, separator, null, null);
    }

    /**
     * Create a sash control with all the required component to allow the user
     * to drag the sash and resize the panes.
     * <p>
     * The given composite <code>parent</code> should have exactly two children.
     * The last children will be used as the right pane. The layout on the
     * parent will be changed to a grid layout.
     * <p>
     * If provided, the width of the pane will be stored and restored from the
     * preference store.
     * 
     * @param parent
     *            the composite parent
     * 
     * @param separator
     *            True to create a separator representing the sash.
     * @param prefStore
     *            The preference store used to store and restore the pane width
     * @param prefKey
     *            the preference key
     */
    public static void createRightPane(final Composite parent, boolean separator, final IPreferenceStore prefStore, final String prefKey) {
        createPane(parent, separator, prefStore, prefKey, SWT.RIGHT, null);
    }

    /**
     * Create a sash control with all the required component to allow the user
     * to drag the sash and resize the panes.
     * <p>
     * The given composite <code>parent</code> should have exactly two children.
     * The last children will be used as the right pane. The layout on the
     * parent will be changed to a grid layout.
     * <p>
     * If provided, the width of the pane will be stored and restored from the
     * preference store.
     * 
     * @param parent
     *            the composite parent
     * 
     * @param separator
     *            True to create a separator representing the sash.
     * @param prefStore
     *            The preference store used to store and restore the pane width
     * @param prefKey
     *            the preference key
     * @param defaultWidth
     *            the default width if the preference is not set or null
     */
    public static void createRightPane(final Composite parent, boolean separator, final IPreferenceStore prefStore, final String prefKey, int defaultWidth) {
        createPane(parent, separator, prefStore, prefKey, SWT.RIGHT, Integer.valueOf(defaultWidth));
    }

    /**
     * Private constructor to avoid creating instance of a utility class.
     */
    private SashFactory() {

    }
}
