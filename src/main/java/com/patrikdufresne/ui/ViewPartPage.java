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

/**
 * Instance of this class represent one page to be displayed within a
 * PageBookView.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class ViewPartPage extends ViewPart implements IViewPartPage {

    /**
     * Default constructor
     * 
     * @param id
     *            the part id
     */
    public ViewPartPage(String id) {
        super(id);
    }

    /**
     * This implementation type cast the IViewSite into a IPageSite.
     */
    @Override
    public IPageSite getSite() {
        return (IPageSite) super.getSite();
    }

    /**
     * This implementation check if the given site is an instance of a
     * IPageSite. This allows the part/page to get access to it's parent part.
     */
    @Override
    public void init(IViewSite newsite) {
        if (!(newsite instanceof IPageSite)) {
            throw new ClassCastException();
        }
        super.init(newsite);
    }

}
