package com.patrikdufresne.jface.wizard.selection;

import org.eclipse.jface.wizard.IWizardNode;

/**
 * This interface is used with WizardListSelectionPage to represent one selected
 * item.
 * 
 * @author patapouf
 * 
 */
public interface IWizardNodeFactory {
	/**
	 * Returns a one line description of the Wizard created by this factory.
	 * 
	 * @return a string
	 */
	public String getDescription();

	/**
	 * Returns a short name describing the Wizard node create by this factory.
	 * 
	 * @return a string
	 */
	public String getName();

	/**
	 * Returns a one word category.
	 * 
	 * @return
	 */
	public String getCategory();

	/**
	 * Returns an <code>IWizardNode</code> representing the specified workbench
	 * wizard which has been selected by the user.
	 * 
	 * @return org.eclipse.jface.wizards.IWizardNode
	 */
	public IWizardNode createWizardNode();

}
