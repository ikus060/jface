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
import org.eclipse.jface.window.IShellProvider;

/**
 * A view site is the primary interface for the view part to interact with the
 * application components. For each view part instance there are a view site.
 * 
 * @author Patrik Dufresne
 * 
 */
public interface IViewSite extends IShellProvider, IToolBarProvider {

    /**
     * Dispose resource allocated by this view site.
     */
    public void dispose();

    /**
     * Returns the main window.
     * 
     * @return
     */
    MainWindow getMainWindow();

    /**
     * Return the associated view part.
     * 
     * @return the view part
     */
    IViewPart getPart();

    /**
     * Used to retrieve a service for this view site.
     * 
     * @param serviceClass
     *            the service class
     * @return the service
     */
    public <T> T getService(Class<T> serviceClass);

    /**
     * Return the view toolbar.
     * 
     * @return
     */
    @Override
    IToolBarManager getToolBarManager();

}