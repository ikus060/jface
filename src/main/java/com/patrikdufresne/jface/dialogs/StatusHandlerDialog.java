/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.util.StatusHandler;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * SafeRunnableDialog is a dialog that can show the results of multiple safe
 * runnable errors.
 * 
 */
public class StatusHandlerDialog extends DetailMessageDialog {

	private static final String EMPTY = ""; //$NON-NLS-1$

	/**
	 * Singleton instance of the status handler.
	 */
	private static StatusHandler statusHandler;

	/**
	 * Create a new instance of a status handler.
	 * 
	 * @return
	 */
	private static StatusHandler createStatusHandler() {
		return new StatusHandler() {

			StatusHandlerDialog dialog;

			@Override
			public void show(final IStatus status, String title) {

				// Display the error with the logger
				Policy.getLog().log(status);

				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						if (dialog == null || dialog.getShell().isDisposed()) {
							dialog = new StatusHandlerDialog(status);
							dialog.create();
							dialog.getShell().addDisposeListener(
									new DisposeListener() {
										@Override
										public void widgetDisposed(
												DisposeEvent e) {
											dialog = null;
										}
									});
							dialog.open();
						} else {
							dialog.addStatus(status);
							dialog.refresh();
						}
					}
				};
				if (Display.getCurrent() != null) {
					runnable.run();
				} else {
					Display.getDefault().asyncExec(runnable);
				}
			}
		};
	}

	/**
	 * Return the singleton status handler using this dialog to display the
	 * error.
	 * 
	 * @return an instance of a status handler
	 */
	public static StatusHandler getStatusHandler() {

		if (statusHandler == null) {
			statusHandler = createStatusHandler();
		}
		return statusHandler;
	}

	protected List<IStatus> statuses = new ArrayList<IStatus>();

	/**
	 * View on the statuses.
	 */
	private Viewer statusListViewer;

	/**
	 * Create a new instance of the receiver on a status.
	 * 
	 * @param status
	 *            The status to display.
	 */
	public StatusHandlerDialog(IStatus status) {

		super(null, JFaceResources.getString("error"), null, JFaceResources //$NON-NLS-1$
				.getString("SafeRunnable.errorMessage"), EMPTY, EMPTY, ERROR, //$NON-NLS-1$
				new String[] { IDialogConstants.OK_LABEL }, 0, null, false);

		setShellStyle(SWT.DIALOG_TRIM | SWT.MODELESS | SWT.RESIZE | SWT.MIN
				| SWT.MAX | getDefaultOrientation());
		setBlockOnOpen(false);

		this.statuses.add(status);
		setStatus(status);
	}

	/**
	 * Sets the current status to be displayed.
	 * 
	 * @param status
	 */
	protected void setStatus(IStatus status) {
		// Update the controls
		setDetailMessage(createStatusMessage(status));
		setDetails(createStatusDetails(status));
	}

	/**
	 * This function is used to create a long detailed message.
	 * 
	 * @param status
	 * @return
	 */
	protected String createStatusDetails(IStatus status) {
		Throwable t = status.getException();
		StringBuilder buf = new StringBuilder();
		while (t != null) {
			buf.append(t.toString());
			buf.append("\r\n"); //$NON-NLS-1$
			StackTraceElement[] trace = t.getStackTrace();
			for (int i = 0; i < trace.length; i++) {
				buf.append("\tat "); //$NON-NLS-1$
				buf.append(trace[i]);
				buf.append("\r\n"); //$NON-NLS-1$
			}
			t = t.getCause();
		}
		return buf.toString();
	}

	/**
	 * This function is used to create a message from a status object. This
	 * implementation return the status message field and append the exception
	 * message field if any.
	 * 
	 * @param status
	 *            the status object
	 * 
	 * @return
	 */
	protected String createStatusMessage(IStatus status) {
		StringBuilder buf = new StringBuilder();
		buf.append(status.getMessage());

		Throwable t = status.getException();
		if (t != null && t.getLocalizedMessage() != null
				&& !t.getLocalizedMessage().isEmpty()) {
			buf.append("\r\n"); //$NON-NLS-1$
			buf.append(t.getLocalizedMessage());
		}
		return buf.toString();
	}

	/**
	 * Add the status to the receiver.
	 * 
	 * @param status
	 */
	public void addStatus(IStatus status) {
		this.statuses.add(status);
		refresh();
	}

	@Override
	protected Control createCustomArea(Composite parent) {
		this.statusListViewer = createStatusListViewer(parent);
		Control control = this.statusListViewer.getControl();
		control.setVisible(false);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 0;
		data.widthHint = 0;
		control.setLayoutData(data);
		return control;
	}

	/**
	 * Create an area that allow the user to select one of multiple jobs that
	 * have reported errors
	 * 
	 * @param parent
	 *            - the parent of the area
	 */
	protected Viewer createStatusListViewer(Composite parent) {
		// Display a list of jobs that have reported errors
		TableViewer viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((IStatus) cell.getElement()).getMessage());
			}
		});
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelectionChange(event);
			}
		});
		applyDialogFont(parent);
		viewer.setInput(this.statuses);
		return viewer;
	}

	/**
	 * Get the single selection. Return null if the selection is not just one
	 * element.
	 * 
	 * @return IStatus or <code>null</code>.
	 */
	protected IStatus getSingleSelection() {
		ISelection rawSelection = this.statusListViewer.getSelection();
		if (rawSelection != null
				&& rawSelection instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) rawSelection;
			if (selection.size() == 1) {
				return (IStatus) selection.getFirstElement();
			}
		}
		return null;
	}

	/**
	 * The selection in the multiple job list has changed. Update widgets.
	 */
	protected void handleSelectionChange(SelectionChangedEvent event) {
		IStatus newSelection = getSingleSelection();
		setStatus(newSelection);
	}

	/*
	 * Return whether there are multiple errors to be displayed
	 */
	protected boolean isMultipleStatusDialog() {
		return this.statuses.size() > 1;
	}

	/**
	 * Method which should be invoked when new errors become available for
	 * display
	 */
	protected void refresh() {
		if (isMultipleStatusDialog()) {
			// The job list doesn't exist so create it.
			setMessage(JFaceResources
					.getString("SafeRunnableDialog_MultipleErrorsMessage")); //$NON-NLS-1$
			getShell()
					.setText(
							JFaceResources
									.getString("SafeRunnableDialog_MultipleErrorsTitle")); //$NON-NLS-1$
			this.statusListViewer.getControl().setVisible(true);
			GridData data = (GridData) this.statusListViewer.getControl()
					.getLayoutData();
			data.heightHint = SWT.DEFAULT;
			data.widthHint = SWT.DEFAULT;
			refreshStatusList();
		}
	}

	/**
	 * Refresh the contents of the viewer.
	 */
	void refreshStatusList() {
		if (this.statusListViewer != null
				&& !this.statusListViewer.getControl().isDisposed()) {
			this.statusListViewer.refresh();
			Point newSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
			getShell().setSize(newSize);
		}
	}

}
