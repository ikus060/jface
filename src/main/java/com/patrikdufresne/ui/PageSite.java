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

import org.eclipse.swt.widgets.Shell;

/**
 * Concrete implementation of IViewSite for a PageBook.
 * 
 * @author Patrik Dufresne
 * 
 */
public class PageSite extends ViewSite implements IPageSite {

    private PageBookViewPart parentPart;

    /**
     * Create a new PageSite.
     * 
     * @param parentViewSite
     */
    public PageSite(PageBookViewPart parentPart, IViewPart part, IToolBarProvider parent) {
        super(parentPart.getSite().getMainWindow(), part, parent);
        this.parentPart = parentPart;
    }

    @Override
    public PageBookViewPart getParentPart() {
        return this.parentPart;
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        return getParentPart().getSite().getService(serviceClass);
    }

    @Override
    public Shell getShell() {
        return getParentPart().getSite().getShell();
    }

}
