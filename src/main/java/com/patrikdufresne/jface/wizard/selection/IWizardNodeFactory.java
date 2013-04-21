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
