package com.patrikdufresne.jface.wizard.selection;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;

/**
 * Implementation of the a {@link IWizardNode}. Sub-Class need to implement the
 * {@link #createWizard()} function.
 * <p>
 * This class is an adaptation of
 * <code>org.eclipse.pde.internal.ui.wizards.WizardNode</code>.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class WizardNode implements IWizardNode {
	/**
	 * The wizard or null if not created.
	 */
	private IWizard wizard;

	/**
	 * This implementation return true if the wizard have been created.
	 */
	@Override
	public boolean isContentCreated() {
		return this.wizard != null;
	}

	/**
	 * Return the existing wizard instance or create it.
	 */
	@Override
	public IWizard getWizard() {
		if (this.wizard == null) {
			this.wizard = createWizard();
		}
		return this.wizard;
	}

	/**
	 * Return default
	 */
	@Override
	public Point getExtent() {
		return new Point(SWT.DEFAULT, SWT.DEFAULT);
	}

	/**
	 * Dispose the wizard.
	 */
	@Override
	public void dispose() {
		if (this.wizard != null) {
			this.wizard.dispose();
			this.wizard = null;
		}
	}

	/**
	 * Sub-classes must implement this function to create a new wizard instance.
	 * 
	 * @return the wizard. Can't be null.
	 */
	public abstract IWizard createWizard();
}
