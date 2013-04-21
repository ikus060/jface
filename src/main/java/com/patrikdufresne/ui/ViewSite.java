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
import org.eclipse.swt.widgets.Shell;

/**
 * Concrete implementation of a {@link IViewSite}.
 * 
 * @author Patrik Dufresne
 * @see IViewSite
 * @see MainWindow
 */
public class ViewSite implements IViewSite {

    private IViewPart part;

    private IToolBarProvider toolbarProvider;

    private MainWindow window;

    /**
     * Create a new view site.
     */
    public ViewSite(MainWindow window, IViewPart part, IToolBarProvider parent) {
        if (window == null || part == null) {
            throw new NullPointerException();
        }
        this.window = window;
        this.part = part;

        // Create a sub tool bar manager to avoid manipulating contribution item
        // from other sources.
        this.toolbarProvider = new SubToolBarProvider(parent);
    }

    /**
     * View site implementation of dispose.
     * <p>
     * Release the toolbar manager.
     */
    @Override
    public void dispose() {
        this.toolbarProvider = null;
        this.part = null;
        this.window = null;
    }

    /**
     * This implementation returns the main windows by the parent view-site.
     */
    @Override
    public MainWindow getMainWindow() {
        return this.window;
    }

    /**
     * This implementation return the view part.
     */
    @Override
    public IViewPart getPart() {
        return this.part;
    }

    /**
     * This implementation search for the specify service class. The operation
     * is delegate to the {@link MainWindow#locateService(Class)}.
     */
    @Override
    public <T> T getService(Class<T> serviceClass) {
        return getMainWindow().locateService(serviceClass);
    }

    /**
     * This implementation return the shell provided by the parent view-site.
     */
    @Override
    public Shell getShell() {
        return getMainWindow().getShell();
    }

    /**
     * Return the view tool bar.
     * <p>
     * This implementation use the SubToolBarProvider to return a tool bar
     * manager.
     */
    @Override
    public IToolBarManager getToolBarManager() {
        return this.toolbarProvider.getToolBarManager();
    }

}