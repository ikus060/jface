package com.patrikdufresne.ui;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * This class represent the main window of the application.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class MainWindow extends ApplicationWindow {

	/**
	 * Define the height in pixel of the tab folder.
	 */
	protected static final int TAB_FOLDER_HEIGHT = 32;

	/**
	 * The composite used to keep track of the active view.
	 */
	private ViewBook book;

	/**
	 * The preference store of this windows.
	 */
	private IPreferenceStore prefStore;

	private ProgressMonitorDialog progressDialog;

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
	 * This function should be called by sub-classes to add view part to this
	 * main window. Typical case, is calling this function in
	 * {@link #addViews()}.
	 * 
	 * @param view
	 *            the view part to be added
	 */
	protected void addView(IViewPart view) {
		initView(view);
		this.book.addView(view);
	}

	/**
	 * Sub-classes should implement this function to add view part to this
	 * window by calling the function addView(
	 */
	protected void addViews() {
		// Nothing to do
	}

	/**
	 * Closes this window, disposes its shell, and removes this window from its
	 * window manager (if it has one).
	 * <p>
	 * This method is extended to save the dialog bounds.
	 * </p>
	 * 
	 * @return <code>true</code> if the window is (or was already) closed, and
	 *         <code>false</code> if it is still open
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
	 * This implementation add a shell title and icon.
	 */
	@Override
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		shell.setMaximized(getInitialMaximized());
	}

	/**
	 * This implementation create a tab folder to hold the view part.
	 */
	@Override
	protected Control createContents(Composite parent) {

		// Add views to book
		this.book = new ViewBook(parent, SWT.BORDER);
		addViews();

		// Sets the TabFolder style
		CTabFolder tabFolder = this.book.getTabFolder();
		tabFolder.setSimple(false);
		tabFolder.setTabHeight(TAB_FOLDER_HEIGHT);
		tabFolder.setUnselectedCloseVisible(false);
		ColorUtil.adapt(tabFolder);

		return this.book;
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
	 * This implementation restore the window location from the preference
	 * store.
	 */
	@Override
	protected Point getInitialLocation(Point initialSize) {
		IPreferenceStore store = getPreferenceStore();
		if (store != null) {
			Point result = ShellPreferences.getLocation(store, getClass()
					.getName());
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
	 * This implementation restore the window initial size from preference
	 * store.
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
	 * This implementation return a progress dialog as a runnable context.
	 */
	public IRunnableContext getRunnableContext() {
		if (this.progressDialog == null) {
			// Create the progress dialog (the runnable context)
			this.progressDialog = new ProgressMonitorDialog(getShell());
		}
		return this.progressDialog;
	}

	/**
	 * Returns a list of views available to be displayed.
	 */
	public IViewPart[] getViews() {
		return getBook().getViews();
	}

	/**
	 * This function is called for every view part added using
	 * {@link #addView(IViewPart)}.
	 * 
	 * @param part
	 *            the view part to be init
	 */
	protected void initView(IViewPart part) {
		// Create a new view site
		IViewSite site = new ViewSite(this, part, this.book.getToolBarProvider() );
		// Init the part with the site
		part.init(site);
		// Check if the site is properly sets
		if (part.getSite() != site) {
			throw new RuntimeException("site not properly set"); //$NON-NLS-1$
		}
	}

	/**
	 * This implementation return null. Sub-classes should implement this
	 * function to provide different services.
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
	 * Saves the bounds of the shell in the preference store. The bounds are
	 * recorded relative to the parent shell, if there is one, or display
	 * coordinates if there is no parent shell.
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
			ShellPreferences.setLocation(store, getClass().getName(),
					shellLocation);
			ShellPreferences.setSize(store, getClass().getName(), shellSize);
			ShellPreferences.setMaximized(store, getClass().getName(),
					shell.getMaximized());
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
