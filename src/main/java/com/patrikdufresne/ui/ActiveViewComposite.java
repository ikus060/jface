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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.util.Policy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * This composite widget is used to keep track of the active view part.
 * 
 * @author Patrik Dufresne
 * 
 */
public class ActiveViewComposite extends Composite implements IToolBarProvider {

    /**
     * The active view part.
     */
    private IViewPart active;

    /**
     * Check for widget disposal.
     */
    private Listener listener = new Listener() {
        @Override
        public void handleEvent(Event event) {
            deactivateView();
        }
    };

    /**
     * The tool bar manager.
     */
    private ToolBarManager toolbarManager;

    /**
     * Create a new composite.
     * 
     * @param parent
     *            the parent
     * @param style
     *            the style of this control
     */
    public ActiveViewComposite(Composite parent, int style) {
        super(parent, style);
        GridLayout layout = new GridLayout(1, true);
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        addListener(SWT.Dispose, this.listener);
    }

    private GridLayout createLayout() {
        GridLayout layout = new GridLayout(1, true);
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        return layout;
    }

    /**
     * Shows the specified view and give it focus.
     * 
     * @param view
     *            the view part to be shown
     * @return True if the specified view is active
     */
    public boolean activateView(IViewPart view) {
        // Check if the specified view is already active
        if (view == this.active) {
            return true;
        }

        // Deactive the current active view if any
        deactivateView();

        // Active the view part. Most likely, activating the view part will al
        // so populate the cool bar.
        if (view != null) {
            Composite comp = new Composite(this, SWT.NONE);
            comp.setLayout(createLayout());
            comp.setLayoutData(new GridData(GridData.FILL_BOTH));
            // Try to active the view
            try {
                view.activate(comp);
            } catch (RuntimeException e) {
                Policy.getStatusHandler().show(new Status(IStatus.ERROR, Policy.JFACE, e.getMessage(), e), null);
            }
            // Layout the toolbar manager
            if (this.toolbarManager != null) {
                if (this.toolbarManager.getSize() > 0) {
                    this.toolbarManager.update(true);
                    this.toolbarManager.getControl().setVisible(true);
                    this.toolbarManager.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
                } else {
                    this.toolbarManager.getControl().setVisible(false);
                    this.toolbarManager.getControl().setLayoutData(new GridData(0, 0));
                }
            }
            // Keep track of the active view
            this.active = view;
        }

        this.layout();

        return true;
    }

    /**
     * Create the tool bar manager. This function should be called only once
     * during the life cycle of this class. After calling this function, the
     * provided tool bar manager should have create the toolbar widget.
     * 
     * @return the tool bar manager
     */
    protected ToolBarManager createToolBarManager() {
        ToolBarManager manager = new ToolBarManager(SWT.FLAT | SWT.RIGHT);

        final ToolBar bar = manager.createControl(this);

        // Add resize listener to tool bar to update the size of tool items with
        // control.
        bar.addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(Event e) {

                Point computedSize = bar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                Point barSize = bar.getSize();
                int ditributeWidth = barSize.x - computedSize.x;
                ToolItem[] items = bar.getItems();
                ToolItem[] resizeItems = new ToolItem[items.length];
                int count = 0;
                for (ToolItem item : items) {
                    Control control;
                    if ((control = item.getControl()) != null && !control.isDisposed() && control.getLayoutData() instanceof RowData) {
                        ditributeWidth += item.getWidth();
                        resizeItems[count] = item;
                        count++;
                    }
                }

                if (count > 0) {
                    int width = Math.max(0, ditributeWidth / count);
                    for (int i = 0; i < count; i++) {
                        resizeItems[i].setWidth(Math.max(resizeItems[i].getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).x, width));
                    }
                }

            }
        });

        return manager;
    }

    /**
     * Dispose control from previous view and deactive the active view is any.
     */
    void deactivateView() {
        // Dispose the composites
        Control children[] = this.getChildren();
        for (int i = 0; i < children.length; i++) {
            if (this.toolbarManager == null || children[i] != this.toolbarManager.getControl()) {
                children[i].dispose();
            }
        }
        // Dispose active view
        if (this.active != null) {
            try {
                this.active.deactivate();
                this.active = null;
            } catch (RuntimeException e) {
                Policy.getStatusHandler().show(new Status(IStatus.ERROR, Policy.JFACE, e.getMessage(), e), null);
            }
        }
    }

    /**
     * Returns the active view or null if none active.
     * 
     * @return the active view or null.
     */
    public IViewPart getActive() {
        return this.active;
    }

    @Override
    public IToolBarManager getToolBarManager() {
        if (this.toolbarManager == null) {
            this.toolbarManager = createToolBarManager();
            Control control = this.toolbarManager.getControl();
            // Check if toolbar is created.
            if (control == null) {
                throw new RuntimeException("ToolBarManager didn't create a toolbar"); //$NON-NLS-1$
            }
            // Check the toolbar parent.
            if (control.getParent() != this) {
                throw new RuntimeException("ToolBarManager didn't create a toolbar with this composite as the parent"); //$NON-NLS-1$
            }
            control.moveAbove(null);
        }
        return this.toolbarManager;
    }

    /**
     * Check if the specified view is active.
     * 
     * @param view
     *            the view to check if visible
     * @return True if the specified view is active.
     */
    public boolean isActive(IViewPart view) {
        return view == this.active;
    }

}
