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

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.patrikdufresne.jface.databinding.util.JFaceProperties;

/**
 * A view book is a special widget used to hold a list of view availables to be
 * displayed in the composite area.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CLOSE, TOP, BOTTOM, FLAT, BORDER, SINGLE, MULTI</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles TOP and BOTTOM may be specified.
 * 
 * @author Patrik Dufresne
 */
public class ViewBook extends Composite {

    /**
     * Key used to store a reference to the view.
     */
    private static final String DATA_VIEW = "view"; //$NON-NLS-1$

    /**
     * The composite used to display the views.
     */
    private ActiveViewComposite comp;

    /**
     * Listen to tab folder event.
     */
    private Listener listener = new Listener() {
        @Override
        public void handleEvent(Event event) {
            handleTabItemSelection(event);
        }
    };

    /**
     * Listen to view events.
     */
    private IPropertyChangeListener changeListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            // Check if the soruce is view.
            if (!(event.getSource() instanceof ViewPart)) {
                return;
            }
            ViewPart view = (ViewPart) event.getSource();
            // Get the reference to the view and the tab.
            int i = indexOf(view.getId());
            if (i == -1) {
                return;
            }
            CTabItem item = tabFolder.getItem(i);
            if (ViewPart.TITLE.equals(event.getProperty())) {
                item.setText(view.getTitle());
            } else if (ViewPart.TITLE_IMAGE.equals(event.getProperty())) {
                item.setImage(view.getTitleImage());
            } else if (ViewPart.TITLE_TOOLTIP.equals(event.getProperty())) {
                item.setToolTipText(view.getTitleToolTip());
            }
        }
    };

    /**
     * The tab folder use to display the available views.
     */
    protected CTabFolder tabFolder;

    /**
     * Create a new active view tab-folder. *
     * <p>
     * The style value is either one of the style constants defined in class SWT
     * which is applicable to instances of this class, or must be built by
     * bitwise OR'ing together (that is, using the int "|" operator) two or more
     * of those SWT style constants. The class description lists the style
     * constants that are applicable to the class. Style bits are also inherited
     * from superclasses.
     * 
     * @param parent
     *            the composite parent
     * @param style
     *            the style.
     * @see SWT#TOP
     * @see SWT#BOTTOM
     * @see SWT#FLAT
     * @see SWT#BORDER
     * @see SWT#SINGLE
     * @see SWT#MULTI
     */
    public ViewBook(Composite parent, int style) {
        super(parent, SWT.NONE);
        setLayout(new FillLayout());
        // Create the tab folder
        this.tabFolder = createTabFolder(this, style);
        configureTabFolder(this.tabFolder);
        // Create the composite holding the views
        this.comp = new ActiveViewComposite(this.tabFolder, SWT.NONE);
    }

    /**
     * Shows the specified view and give it focus.
     * 
     * @param view
     *            the view part to be shown
     * @return True if the specified view is active
     */
    public boolean activateView(IViewPart view) {
        return activateView(view.getId());
    }

    /**
     * Shows the specified view and give it focus.
     * 
     * @param id
     *            the view's id
     * @return True if the specified view is active
     */
    public boolean activateView(String id) {
        // Check if the view exists
        int index = indexOf(id);
        if (index == -1) {
            throw new IllegalArgumentException("view doesn't exists"); //$NON-NLS-1$
        }
        // Select the tab folder
        this.tabFolder.setSelection(index);

        // Get the view from the index
        IViewPart view = getView(index);

        // Activate the view
        return this.comp.activateView(view);
    }

    /**
     * Insert the given view part at the specified position in the list of
     * available views to be displayed by this composite.
     * 
     * @param index
     *            index at which the specified view is to be inserted
     * @param view
     *            the view to be inserted
     */
    public void addView(int index, IViewPart view) {
        if (view == null) {
            throw new NullPointerException();
        }

        // Check if the view is already in the list.
        if (indexOf(view.getId()) != -1) {
            return;
        }
        // Create a Tab-item
        CTabItem item = createTabItem(this.tabFolder, view, index);
        item.setControl(this.comp);
        // Attach listener
        if (view instanceof ViewPart) {
            ((ViewPart) view).addPropertyChangeListener(this.changeListener);
        }

        // Select the item if it'S the first.
        if (this.tabFolder.getItemCount() == 1) {
            this.tabFolder.setSelection(item);
            this.comp.activateView(view);
        }
        this.comp.setVisible(true);
    }

    /**
     * Appends the specified view to the end of this list of available views to
     * be displayed by this composite.
     * <p>
     * The view part must already be init with a view site.
     * 
     * @param view
     *            the view to be add
     */
    public void addView(IViewPart view) {
        addView(this.tabFolder.getItemCount(), view);
    }

    /**
     * Configures the given tab-folder in preparation to be displayed.
     * <p>
     * The default implementation of this function sets the tabFolder layout and
     * attach a listener.
     * <p>
     * Subclasses may extend or reimplement.
     * 
     * @param tabFolder
     */
    protected void configureTabFolder(CTabFolder tabFolder) {
        this.tabFolder.setLayout(new FillLayout());
        this.tabFolder.addListener(SWT.Selection, this.listener);
        this.tabFolder.setSelection(0);
    }

    /**
     * Used to create the tab folder widget. Either a TabFolder of a CTabFolder.
     * 
     * @param parent
     */
    protected CTabFolder createTabFolder(Composite parent, int style) {
        return new CTabFolder(parent, style);
    }

    /**
     * Create a CTabItem for the view.
     * 
     * @param view
     *            the associated view
     * @return the CTabItem
     */
    protected CTabItem createTabItem(CTabFolder parent, IViewPart view, int index) {
        CTabItem item = new CTabItem(parent, SWT.NONE, index);
        item.setText(view.getTitle());
        item.setToolTipText(view.getTitleToolTip());
        item.setImage(view.getTitleImage());
        item.setData(DATA_VIEW, view);
        item.setShowClose(false);
        return item;
    }

    /**
     * Returns the active view or null if none active.
     * 
     * @return the active view or null.
     */
    public IViewPart getActive() {
        return this.comp.getActive();
    }

    /**
     * Returns the embeded tab folder.
     * 
     * @return the tab folder widget.
     */
    public CTabFolder getTabFolder() {
        return this.tabFolder;
    }

    /**
     * Return cool bar manager from the active view composite.
     * 
     * @return
     */
    public IToolBarProvider getToolBarProvider() {
        return this.comp;
    }

    /**
     * Returns the view for the given index.
     */
    private IViewPart getView(int index) {
        return (IViewPart) this.tabFolder.getItem(index).getData(DATA_VIEW);
    }

    /**
     * Returns a list of views available to be displayed.
     */
    public IViewPart[] getViews() {
        CTabItem[] items = this.tabFolder.getItems();
        IViewPart[] a = new IViewPart[items.length];
        for (int i = 0; i < items.length; i++) {
            a[i] = (IViewPart) items[i].getData(DATA_VIEW);
        }
        return a;
    }

    /**
     * This function is used to notify this class about the user selecting a tab
     * item.
     */
    protected void handleTabItemSelection(Event event) {
        if (!(event.item instanceof CTabItem)) return;
        if (!(event.item.getData(DATA_VIEW) instanceof IViewPart)) return;

        ((CTabItem) event.item).setControl(this.comp);

        // Active the view associated to this item
        IViewPart view = (IViewPart) event.item.getData(DATA_VIEW);
        this.comp.activateView(view);
    }

    /**
     * Returns the index of of the given view'S id.
     * 
     * @param id
     *            the view's id
     * @return the index of the view or -1 if not found
     */
    private int indexOf(String id) {
        int index = 0;
        CTabItem[] items = this.tabFolder.getItems();
        while (index < items.length && !(getView(index).getId().equals(id))) {
            index++;
        }
        if (index < items.length) {
            return index;
        }
        return -1;
    }

    /**
     * Check if the specified view is active.
     * 
     * @param view
     *            the view to check if visible
     * @return True if the specified view is active.
     */
    public boolean isActive(IViewPart view) {
        return this.comp.isActive(view);
    }

    /**
     * Remove a single instance of the specified view from the list of available
     * views to be displayed by this composite.
     * 
     * @param view
     *            the view to be removed
     * @return True if the view was contained in the list
     */
    public boolean removeView(IViewPart view) {
        return removeView(view.getId());
    }

    /**
     * Remove a single instance of a view defined by the specified
     * <code>id</code>.
     * 
     * @param id
     *            the view id
     * @return True if the view was contained in the list
     */
    public boolean removeView(String id) {
        // Search the view in the list
        int index = indexOf(id);
        if (index == -1) {
            return false;
        }
        // If the view is currently active, deactivate it
        IViewPart view = getView(index);
        if (this.comp.getActive() == view) {
            this.comp.activateView(null);
        }
        // Detach listener
        if (view instanceof ViewPart) {
            ((ViewPart) view).addPropertyChangeListener(this.changeListener);
        }

        // Remove the item from the list
        this.tabFolder.getItem(index).dispose();

        return true;
    }

}
