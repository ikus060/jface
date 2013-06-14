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

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardNode;

/**
 * Abstract implementation of {@link IWizardNodeFactory}.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class AbstractWizardNodeFactory implements IWizardNodeFactory {
    /**
     * Category
     */
    private String category;

    /**
     * Wizard description.
     */
    private String description;

    /**
     * Wizard short name.
     */
    private String name;

    /**
     * Create a new wizard factory.
     * 
     * @param name
     *            The localized name of the wizard.
     * @param description
     *            The locazlied description of the wizard.
     */
    public AbstractWizardNodeFactory(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }

    /**
     * Subclasses must implement this function to create the wizard.
     * 
     * @return a wizard
     */
    protected abstract IWizard createWizard();

    /**
     * This implementation create a new {@link WizardNode}.
     * 
     * @see net.ekwos.gymkhana.ui.wizardselection.IWizardNodeFactory#createWizardNode()
     */
    @Override
    public IWizardNode createWizardNode() {
        return new WizardNode() {
            @Override
            public IWizard createWizard() {
                return AbstractWizardNodeFactory.this.createWizard();
            }
        };
    }

    /**
     * Return the wizard category or null for general
     * 
     * @return
     */
    public String getCategory() {
        return category;
    }

    /**
     * This implementation return te wizard description.
     * 
     * @see net.ekwos.gymkhana.ui.wizardselection.IWizardNodeFactory#getDescription()
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * This implementation return the wizard name.
     * 
     * @see net.ekwos.gymkhana.ui.wizardselection.IWizardNodeFactory#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Sets the category.
     * 
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Sets the description.
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the name.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

}
