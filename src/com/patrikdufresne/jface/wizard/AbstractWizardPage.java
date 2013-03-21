package com.patrikdufresne.jface.wizard;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.wizard.WizardPage;

/**
 * This wizard page provide a default implementation including a data binding
 * context.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class AbstractWizardPage extends WizardPage {

	private DataBindingContext dbc;

	/**
	 * Observable manager.
	 */
	private ObservablesManager om;

	/**
	 * Create a new wizard page.
	 * 
	 * @param pageName
	 *            the page name.
	 */
	protected AbstractWizardPage(String pageName) {
		super(pageName);
	}

	/**
	 * This function should be called without the createControls function to
	 * bind the widgets.
	 */
	protected void bind() {
		this.dbc = new DataBindingContext();
		this.om = new ObservablesManager();
		this.om.runAndCollect(new Runnable() {
			@Override
			public void run() {
				bindValues();
			}
		});
		this.om.addObservablesFromContext(this.dbc, true, true);
		this.wizardPageSupport = WizardPageSupport.create(this, getDbc());
	}

	protected WizardPageSupport wizardPageSupport;

	/**
	 * Sub-classes should implement this function to bind the widgets.
	 */
	protected abstract void bindValues();

	@Override
	public void dispose() {
		try {
			if (this.wizardPageSupport != null) {
				this.wizardPageSupport.dispose();
				this.wizardPageSupport = null;
			}
			// Dispose the widget before disposing the observables, otherwise
			// the widget will still have reference to the observables.
			if (getControl() != null) {
				getControl().dispose();
			}
			// Dispose the observables.
			if (this.om != null) {
				this.om.dispose();
				this.om = null;
			}
			if (this.dbc != null) {
				this.dbc.dispose();
				this.dbc = null;
			}
		} finally {
			super.dispose();
		}
	}

	protected DataBindingContext getDbc() {
		return this.dbc;
	}

}
