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
package com.patrikdufresne.jface.wizard;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.wizard.WizardPage;

/**
 * This wizard page provide a default implementation including a data binding context.
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

    protected WizardPageSupport wizardPageSupport;

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
     * This function should be called without the createControls function to bind the widgets.
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

    /**
     * Return a reference to the databinding context.
     * 
     * @return
     */
    protected DataBindingContext getDbc() {
        return this.dbc;
    }

    /**
     * Return a reference on the observable manager.
     * 
     * @return
     */
    protected ObservablesManager getOm() {
        return this.om;
    }

}
