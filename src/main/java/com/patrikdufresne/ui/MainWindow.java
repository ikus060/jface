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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class represent the main window of the application.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class MainWindow extends ApplicationWindow {

    // Constante pour nouvelle couleur du tabfolder background
    private static final String TABFOLDER_BACKGROUND_COLOR_2 = "TABFOLDER_BACKGROUND_COLOR_2";

    /**
     * The composite used to keep track of the active view.
     */
    private ViewBook book;

    /**
     * The preference store of this windows.
     */
    private IPreferenceStore prefStore;

    /**
     * Create a new main window.
     * 
     * @param parentShell
     */
    public MainWindow(Shell parentShell) {
        super(parentShell);
    }

    /**
     * Shows the specified view and give it focus.
     * 
     * @param id
     *            the view's id
     * @return True if the specified view is active
     */
    public boolean activateView(String id) {
        return getBook().activateView(id);
    }

    protected void addView(int index, IViewPart part) {
        initView(part);
        this.book.addView(index, part);
    }

    /**
     * This function should be called by sub-classes to add view part to this main window. Typical case, is calling this
     * function in {@link #addViews()}.
     * 
     * @param view
     *            the view part to be added
     */
    protected void addView(IViewPart view) {
        initView(view);
        this.book.addView(view);
    }

    /**
     * Sub-classes should implement this function to add view part to this window by calling the function addView(
     */
    protected void addViews() {
        // Nothing to do
    }

    /**
     * Closes this window, disposes its shell, and removes this window from its window manager (if it has one).
     * <p>
     * This method is extended to save the dialog bounds.
     * </p>
     * 
     * @return <code>true</code> if the window is (or was already) closed, and <code>false</code> if it is still open
     */
    @Override
    public boolean close() {
        // If already closed, there is nothing to do.
        // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=127505
        if (getShell() == null || getShell().isDisposed()) {
            return true;
        }
        saveDialogBounds(getShell());

        // Dispose the view parts
        IViewPart[] parts = this.book.getViews();
        this.book.dispose();
        this.book = null;
        for (IViewPart part : parts) {
            part.dispose();
        }

        return super.close();
    }

    /**
     * This implementation create a tab folder to hold the view part.
     */
    @Override
    protected Control createContents(Composite parent) {
        // Create a view book
        this.book = createViewBook(parent);
        // Add view to the book
        addViews();
        // Return the book as the main component of this windows.
        return this.book;
    }

    /**
     * Create a new instance of {@link ViewBook} and customize it's appearance.
     * 
     * @param parent
     */
    protected ViewBook createViewBook(Composite parent) {
        // Create the view book.
        ViewBook b = new ViewBook(parent, SWT.NONE);

        // Change it's appearance
        CTabFolder tabFolder = b.getTabFolder();
        tabFolder.setSimple(true);
        tabFolder.setUnselectedCloseVisible(false);

        // Creates a blend of black and widget background
        RGB widgetB1 = ColorUtil.blend(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).getRGB(), new RGB(0, 0, 0), 90);
        // Adds the blend color to the registry
        JFaceResources.getColorRegistry().put(TABFOLDER_BACKGROUND_COLOR_2, widgetB1);

        // Set background and selected background
        tabFolder.setBackground(new Color[] {
                Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND),
                JFaceResources.getColorRegistry().get(TABFOLDER_BACKGROUND_COLOR_2) }, new int[] { 100 }, true);
        tabFolder.setSelectionBackground(new Color[] {
                Display.getDefault().getSystemColor(SWT.COLOR_WHITE),
                Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND) }, new int[] { 90 }, true);

        return b;
    }

    /**
     * Returns the active view or null if none active.
     * 
     * @return the active view or null.
     */
    public IViewPart getActive() {
        return getBook().getActive();
    }

    /**
     * Return the view book used to display the view parts.
     * 
     * @return
     */
    protected ViewBook getBook() {
        return this.book;
    }

    /**
     * This implementation restore the window location from the preference store.
     */
    @Override
    protected Point getInitialLocation(Point initialSize) {
        IPreferenceStore store = getPreferenceStore();
        if (store != null) {
            Point result = ShellPreferences.getLocation(store, getClass().getName());
            if (result != null) {
                // The coordinates were stored relative to the parent shell.
                // Convert to display coordinates.
                Shell parent = getParentShell();
                if (parent != null) {
                    Point parentLocation = parent.getLocation();
                    result.x += parentLocation.x;
                    result.y += parentLocation.y;
                }
                return result;
            }
        }
        return super.getInitialLocation(initialSize);
    }

    /**
     * Returns the initial maximized state.
     * 
     * @return
     */
    protected boolean getInitialMaximized() {
        IPreferenceStore store = getPreferenceStore();
        if (store == null) {
            return false;
        }
        return ShellPreferences.getMaximized(store, getClass().getName());
    }

    /**
     * This implementation restore the window initial size from preference store.
     */
    @Override
    protected Point getInitialSize() {
        IPreferenceStore store = getPreferenceStore();
        if (store == null) {
            return super.getInitialSize();
        }
        Point result = ShellPreferences.getSize(store, getClass().getName());
        if (result != null) {
            return result;
        }
        return super.getInitialSize();
    }

    /**
     * Return the preference store to be used to remember this windows settings.
     * 
     * @return
     */
    public IPreferenceStore getPreferenceStore() {
        return this.prefStore;
    }

    /**
     * Returns a list of views available to be displayed.
     */
    public IViewPart[] getViews() {
        return getBook().getViews();
    }

    /**
     * This implementation set maximized.
     */

    @Override
    protected void initializeBounds() {
        super.initializeBounds();
        getShell().setMaximized(getInitialMaximized());
    }

    /**
     * This function is called for every view part added using {@link #addView(IViewPart)}.
     * 
     * @param part
     *            the view part to be init
     */
    protected void initView(IViewPart part) {
        // Create a new view site
        IViewSite site = new ViewSite(this, part, this.book.getToolBarProvider());
        // Init the part with the site
        part.init(site);
        // Check if the site is properly sets
        if (part.getSite() != site) {
            throw new RuntimeException("site not properly set"); //$NON-NLS-1$
        }
    }

    /**
     * This implementation return null. Sub-classes should implement this function to provide different services.
     * 
     * @param serviceClass
     *            the requested service class.
     * 
     * @return The service or null
     */
    public <T> T locateService(Class<T> serviceClass) {
        return null;
    }

    /**
     * This implementation of IRunnableContext#run(boolean, boolean, IRunnableWithProgress) blocks until the runnable
     * has been run, regardless of the value of <code>fork</code>. It is recommended that <code>fork</code> is set to
     * true in most cases. If <code>fork</code> is set to <code>false</code>, the runnable will run in the UI thread and
     * it is the runnable's responsibility to call <code>Display.readAndDispatch()</code> to ensure UI responsiveness.
     */
    @Override
    public void run(final boolean fork, boolean cancelable, final IRunnableWithProgress runnable) throws InvocationTargetException, InterruptedException {
        ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());
        progressDialog.run(fork, cancelable, runnable);
    }

    /**
     * Saves the bounds of the shell in the preference store. The bounds are recorded relative to the parent shell, if
     * there is one, or display coordinates if there is no parent shell.
     * 
     * @param shell
     *            The shell whose bounds are to be stored
     */
    protected void saveDialogBounds(Shell shell) {
        Point shellLocation = shell.getLocation();
        Point shellSize = shell.getSize();
        Shell parent = getParentShell();
        if (parent != null) {
            Point parentLocation = parent.getLocation();
            shellLocation.x -= parentLocation.x;
            shellLocation.y -= parentLocation.y;
        }
        IPreferenceStore store = getPreferenceStore();
        if (store != null) {
            ShellPreferences.setLocation(store, getClass().getName(), shellLocation);
            ShellPreferences.setSize(store, getClass().getName(), shellSize);
            ShellPreferences.setMaximized(store, getClass().getName(), shell.getMaximized());
        }
    }

    /**
     * Sets the preference store for this window.
     * 
     * @param prefStore
     */
    public void setPreferenceStore(IPreferenceStore prefStore) {
        this.prefStore = prefStore;
    }

    /**
     * This implementation always return false.
     */
    @Override
    protected boolean showTopSeperator() {
        return false;
    }

}