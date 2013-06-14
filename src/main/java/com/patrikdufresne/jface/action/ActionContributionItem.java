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
package com.patrikdufresne.jface.action;

import org.eclipse.jface.action.IAction;

/**
 * A contribution item which delegates to an action.
 * <p>
 * This implementation allow to sets the mode in the constructor.
 * 
 * @author Patrik Dufresne
 * 
 */
public class ActionContributionItem extends org.eclipse.jface.action.ActionContributionItem {

    /**
     * create a new contribution item.
     * 
     * @param action
     *            the action.
     */
    public ActionContributionItem(IAction action) {
        this(action, 0);
    }

    /**
     * Create a new contribution item and sets the mode according to
     * <code>forceText</code>.
     * 
     * @param action
     *            the action
     * @param forceText
     *            True to set the mode to MODE_FORCE_TEXT
     */
    public ActionContributionItem(IAction action, boolean forceText) {
        this(action, forceText ? MODE_FORCE_TEXT : 0);
    }

    /**
     * Create a new contribution item and sets the <code>mode</code>.
     * 
     * @param action
     *            the action.
     * @param mode
     *            the presentation mode settings (bitwise-or of the MODE_*
     *            constants)
     */
    public ActionContributionItem(IAction action, int mode) {
        super(action);
        setMode(mode);
    }

}
