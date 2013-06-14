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

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.SubToolBarManager;

/**
 * This implementation of {@link IToolBarProvider} allow creating sub tool bar
 * manager into an existing tool bar.
 * 
 * @author Patrik Dufresne
 * 
 */
public class SubToolBarProvider implements IToolBarProvider {

    private IToolBarProvider parent;

    private SubToolBarManager subtoolbar;

    /**
     * Create a new sub tool bar provider.
     * 
     * @param parentthe
     *            parent toolbar provider.
     */
    public SubToolBarProvider(IToolBarProvider parent) {
        if (parent == null) {
            throw new IllegalArgumentException();
        }
        this.parent = parent;
    }

    /**
     * This implementation return a sub cool bar manager.
     */
    @Override
    public IToolBarManager getToolBarManager() {
        if (this.subtoolbar == null) {
            this.subtoolbar = new SubToolBarManager(this.parent.getToolBarManager());
            this.subtoolbar.setVisible(true);
        }
        return this.subtoolbar;
    }

}
