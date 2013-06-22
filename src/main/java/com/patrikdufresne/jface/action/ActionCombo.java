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

import java.util.Arrays;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * This action work in a similar way to a Combo widget.
 * 
 * @author Patrik Dufresne
 * 
 */
public class ActionCombo extends Action {

    public static final String ITEMS = "items";

    public static final String SELECTION = "selection";

    public static final String DEFAULT_MESSAGE = "defaultMessage";

    /**
     * The default message.
     */
    String defaultMessage = "";

    /**
     * List the available items.
     */
    private Object[] items;

    /**
     * A menu creator to display the available elements.
     */
    private IMenuCreator menuCreator = new IMenuCreator() {

        private Menu menu;

        @Override
        public void dispose() {
            if (this.menu != null) {
                this.menu.dispose();
                this.menu = null;
            }
        }

        @Override
        public Menu getMenu(Control parent) {
            if (this.menu != null) this.menu.dispose();
            this.menu = new Menu(parent);
            return createMenu(this.menu);
        }

        @Override
        public Menu getMenu(Menu parent) {
            if (this.menu != null) this.menu.dispose();
            this.menu = new Menu(parent);
            return createMenu(this.menu);
        }
    };

    /**
     * Listen to user selection.
     */
    private Listener privateListener = new Listener() {

        @Override
        public void handleEvent(Event event) {
            MenuItem menuItem = (MenuItem) event.widget;
            Object element = menuItem.getData();
            setSelection(element);
        }
    };

    /**
     * The element being selected.
     */
    private Object selection;

    /**
     * Create a new action drop down.
     */
    public ActionCombo() {
        super("", IAction.AS_DROP_DOWN_MENU);
        setMenuCreator(this.menuCreator);
    }

    /**
     * Called every time the menu need to be displayed.
     * 
     * @param menu
     *            the menu to be populate
     * @return the menu
     */
    protected Menu createMenu(Menu menu) {
        for (Object element : this.items) {
            createMenuItem(menu, element);
        }
        return menu;
    }

    /**
     * Create a new menu item to represent the element.
     * 
     * @param menu
     * @param element
     */
    protected void createMenuItem(Menu menu, Object element) {
        MenuItem item = new MenuItem(menu, SWT.RADIO);
        item.setText(getElementLabel(element));
        // Check if selected
        if (element.equals(this.selection)) {
            item.setSelection(true);
        }
        item.setData(element);
        item.addListener(SWT.Selection, this.privateListener);
    }

    /**
     * Return the default message being displayed when their are no selection.
     * 
     * @return the default message.
     */
    public String getDefaultMessage() {
        return this.defaultMessage;
    }

    /**
     * Return the label of the given element using the label property.
     * 
     * @param element
     *            the element.
     * @return the label.
     */
    protected String getElementLabel(Object element) {
        return element == null ? "" : element.toString();
    }

    /**
     * Returns the available items.
     * 
     * @return
     */
    public Object[] getItems() {
        return this.items;
    }

    /**
     * Return the element being selected.
     * 
     * @return the selection or null.
     */
    public Object getSelection() {
        return this.selection;
    }

    @Override
    public void run() {
        // Does nothing.
    }

    /**
     * Sets the default message to be displayed when their are no item selected.
     * 
     * @param message
     *            the new default message.
     */
    public void setDefaultMessage(String message) {
        if (message == null) {
            message = "";
        }
        firePropertyChange(DEFAULT_MESSAGE, this.defaultMessage, this.defaultMessage = message);
        // If the selection is unset, update the action text.
        if (this.selection == null) {
            super.setText(this.defaultMessage);
        }
    }

    /**
     * This implementation does nothing.
     */
    @Override
    public void setText(String text) {
        // Do nothing.
    }

    /**
     * Sets the available item.
     * 
     * @param items
     */
    public void setItems(Object[] items) {
        firePropertyChange(ITEMS, this.items, this.items = Arrays.copyOf(items, items.length));
    }

    /**
     * Sets the element being selected.
     * 
     * @param element
     *            the element or null.
     */
    public void setSelection(Object element) {
        // TODO Check if the element is inside the elements list.
        firePropertyChange(SELECTION, this.selection, this.selection = element);
        if (this.selection != null) {
            super.setText(getElementLabel(this.selection));
        } else {
            super.setText(this.defaultMessage);
        }
    }

}
