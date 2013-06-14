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

import org.eclipse.jface.action.AbstractGroupMarker;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.Policy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * An implementation of an <code>IMenuManager</code> that inherits its UI (text
 * + icon + hints) from a given action.
 * <p>
 * This class is an adaptation of
 * org.eclipse.gmf.runtime.common.ui.action.ActionMenuManager.
 * 
 * <p>
 * When filled in a toolbar, the menu is rendered as a tool item with two parts:
 * a button, whose icon comes from the supplied action handler, and a drop-down
 * menu arrow. When the arrow is pressed, the drop-down menu is shown. When the
 * button is pressed, the associated action is executed. The manager can have an
 * optional style to retarget the last executed action. In this case the tool
 * item UI reflects the last executed sub-action from the menu.
 * <p>
 * When filled in a menu, this menu shows up as a normal cascading menu with its
 * GUI inherited from the supplied action.
 * 
 * @author Patrik Dufresne
 */
public class ActionMenuManager extends MenuManager {

    /**
     * An action that provides a menu and fills it from the contribution items
     * of the enclosing menu manager.
     */
    public class MenuCreatorAction extends Action implements IMenuCreator {
        // the action
        private IAction actionHandler;
        // the menu widget
        private Menu menu;

        // menu item selection listener: listens to selection events
        private Listener menuItemListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (SWT.Selection == event.type && !event.widget.isDisposed()) {
                    ActionContributionItem item = (ActionContributionItem) event.widget.getData();
                    if (ActionMenuManager.this.retargetLastAction) {
                        setActionHandler(item.getAction());
                        setDefaultAction(item.getAction());
                    }
                    subActionSelected(item.getAction());
                }
            }
        };

        /**
         * Creates a new menu creator action
         * 
         * @param actionHandler
         *            the action handler or null
         */
        public MenuCreatorAction(IAction actionHandler) {
            super(actionHandler != null ? actionHandler.getText() : null);
            setEnabled(false); // initially untill a menu item is added
            setActionHandler(actionHandler);
            setMenuCreator(this);
        }

        /**
         * Create the drop-down/pop-up menu.
         * 
         * @param mnu
         *            <code>Menu</code> for which to create the drop-down/pop-up
         *            menu
         * @return <code>Menu</code> the drop-down/pop-up menu
         */
        protected Menu createMenu(Menu mnu) {
            IContributionItem[] items = getRealItems();
            IContributionItem lastGroupMarker = null;
            for (int i = 0; i < items.length; i++) {
                IContributionItem item = items[i];
                if (item instanceof AbstractGroupMarker) {
                    if (i == 0 || i == items.length - 1 || items[i + 1] instanceof AbstractGroupMarker || mnu.getItemCount() < 1 || !item.isVisible()) {
                        continue;
                    }
                    // Do not add last group marker until we know that there
                    // will be items following it.
                    lastGroupMarker = item;

                } else {
                    if (!item.isVisible()) {
                        continue;
                    }
                    try {
                        if (lastGroupMarker != null) {
                            lastGroupMarker.fill(menu, -1);
                            lastGroupMarker = null;
                        }
                        item.fill(menu, -1);
                    } catch (Exception e) {
                        // "The contribution item (" + item.getId() +
                        // ") failed to fill within the menu"
                        Policy.logException(e);
                    }
                }
            }
            MenuItem menuItems[] = mnu.getItems();
            for (int i = 0; i < menuItems.length; i++) {
                if (menuItems[i].getStyle() == SWT.SEPARATOR) continue;
                menuItems[i].addListener(SWT.Selection, menuItemListener);
            }
            return mnu;
        }

        public void dispose() {
            if (menu != null) {
                menu.dispose();
                menu = null;
            }
            //ActionMenuManager.this.dispose();
        }

        protected IAction getActionHandler() {
            return this.actionHandler;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets
         * .Control)
         */
        public Menu getMenu(Control parent) {
            if (menu != null) menu.dispose();

            menu = new Menu(parent);
            return createMenu(menu);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets
         * .Menu)
         */
        public Menu getMenu(Menu parent) {
            if (menu != null) menu.dispose();
            menu = new Menu(parent);
            return createMenu(menu);
        }

        /**
         * Only run the action handler if it is enabled
         * 
         */
        public void run() {
            if (getActionHandler() != null && getActionHandler().isEnabled()) super.run();
            else if (getDefaultAction().isEnabled()) {
                setActionHandler(getDefaultAction());
                super.run();
            }
        }

        /**
         * Only run the action handler if it is enabled
         * 
         */
        public void runWithEvent(Event event) {
            if (getActionHandler() != null && getActionHandler().isEnabled()) getActionHandler().runWithEvent(event);
            else if (getDefaultAction() != null && getDefaultAction().isEnabled()) {
                setActionHandler(getDefaultAction());
                getActionHandler().runWithEvent(event);
            } else if (event.widget instanceof ToolItem) {
                Menu m = getMenu(((ToolItem) event.widget).getParent());
                if (m != null) {
                    // position the menu below the drop down item
                    // Point point =
                    // ((ToolItem)event.widget).getParent().toDisplay(
                    // new Point(event.x, event.y));
                    // m.setLocation(point.x, point.y); // waiting
                    // for SWT
                    // 0.42
                    m.setVisible(true);
                    return; // we don't fire the action
                }
            }
        }

        /**
         * Ignores the action handler's "enable" event since "enablement" is
         * determined by the sub-action(s)
         * 
         */
        protected void setActionHandler(IAction handler) {
            boolean enabled = MenuCreatorAction.this.isEnabled();
            this.actionHandler = handler;
            MenuCreatorAction.this.setEnabled(enabled);
        }

    }

    /** the associated menu action */
    protected final MenuCreatorAction action;

    /** the delegate action contribution item */
    private final ActionContributionItem actionContributionItem;

    /** the associated menu action */
    protected IAction defaultAction = null;

    /** an option to retarget the last action */
    boolean retargetLastAction;

    /**
     * Creates a new instance of <code>ActionMenuManager</code> with a given
     * action handler. The manager does not retarget the last selected action
     * from the menu
     * 
     * @param id
     *            The menu manager id
     * @param defaultActionHandler
     *            The default action handler when the user click on the button
     *            part of the item
     */
    public ActionMenuManager(String id, IAction defaultActionHandler) {
        this(id, defaultActionHandler, false);
    }

    /**
     * Creates a new instance of <code>ActionMenuManager</code> with a given
     * action handler and an option to retarget the last executed menu action.
     * 
     * @param id
     *            The menu manager id
     * @param defaultActionHandler
     *            The default action handler when the user click on the button
     *            part of the item
     * @param retargetLastAction
     *            whether to retarget the last action or not
     */
    public ActionMenuManager(String id, IAction defaultActionHandler, boolean retargetLastAction) {
        this(defaultActionHandler.getText(), defaultActionHandler.getImageDescriptor(), id, defaultActionHandler, retargetLastAction);
    }

    /**
     * Create a new instance of <code>ActionMenuManager</code>.
     * 
     * @param text
     *            the menu text
     * @param image
     *            the menu image or null
     * @param id
     *            the menu id
     * @param defaultActionHandler
     *            the default action or null
     * @param retargetLastAction
     *            whether to retarget the last action or not
     */
    protected ActionMenuManager(String text, ImageDescriptor image, String id, IAction defaultActionHandler, boolean retargetLastAction) {
        super(text, image, id);
        this.action = new MenuCreatorAction(defaultActionHandler);
        this.action.setImageDescriptor(image);
        this.defaultAction = defaultActionHandler;
        this.actionContributionItem = new ActionContributionItem(this.action);
        this.retargetLastAction = retargetLastAction;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IContributionItem#dispose()
     */
    @Override
    public void dispose() {
        if (this.actionContributionItem != null) {
            this.actionContributionItem.dispose();
        }
        super.dispose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public void fill(Composite parent) {
        // this is only relevant in toolbars
        this.retargetLastAction = false;
        this.actionContributionItem.fill(parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets
     * .Menu, int)
     */
    @Override
    public void fill(Menu parent, int index) {
        // this is only relevant in toolbars
        this.retargetLastAction = false;
        this.actionContributionItem.fill(parent, index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets
     * .ToolBar, int)
     */
    @Override
    public void fill(ToolBar parent, int index) {
        this.actionContributionItem.fill(parent, index);
    }

    public IAction getDefaultAction() {
        return this.defaultAction;
    }

    /**
     * Returns the contribution items of this manager. If an item is wrapper in
     * a SubContributionItem instance it extracts the real item instance
     * 
     * @return An array of real items of this contribution manager
     */
    protected IContributionItem[] getRealItems() {
        IContributionItem[] items = getItems();
        IContributionItem[] realItems = new IContributionItem[items.length];
        for (int i = 0; i < items.length; i++) {
            if (items[i] instanceof SubContributionItem) {
                realItems[i] = ((SubContributionItem) items[i]).getInnerItem();
            } else {
                realItems[i] = items[i];
            }
        }
        return realItems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IContributionManager#isDirty()
     */
    @Override
    public boolean isDirty() {
        return this.actionContributionItem.isDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IContributionItem#isDynamic()
     */
    @Override
    public boolean isDynamic() {
        return this.actionContributionItem.isDynamic();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IContributionItem#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return this.actionContributionItem.isEnabled();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IContributionItem#isGroupMarker()
     */
    @Override
    public boolean isGroupMarker() {
        return this.actionContributionItem.isGroupMarker();
    }

    /**
     * Returns whether the option to retarget last action was requested
     * 
     * @return <code>true</code> if retargetLastAction is enabled,
     *         <code>false</code> otherwise
     */
    protected boolean isRetargetLastAction() {
        return this.retargetLastAction;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IContributionItem#isSeparator()
     */
    @Override
    public boolean isSeparator() {
        return this.actionContributionItem.isSeparator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IContributionItem#isVisible()
     */
    @Override
    public boolean isVisible() {
        IContributionItem[] items = getRealItems();
        for (int i = 0; i < items.length; i++) {
            IContributionItem item = items[i];
            if (!(item instanceof AbstractGroupMarker) && item.isVisible()) {
                return this.actionContributionItem.isVisible();
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.action.ContributionManager#itemAdded(org.eclipse.jface
     * .action.IContributionItem)
     */
    @Override
    protected void itemAdded(IContributionItem item) {
        super.itemAdded(item);
        if (item instanceof SubContributionItem) item = ((SubContributionItem) item).getInnerItem();
        if (!item.isGroupMarker()) this.action.setEnabled(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.action.ContributionManager#itemRemoved(org.eclipse.
     * jface.action.IContributionItem)
     */
    @Override
    protected void itemRemoved(IContributionItem item) {
        super.itemRemoved(item);
        if (item instanceof SubContributionItem) item = ((SubContributionItem) item).getInnerItem();
        if (!item.isGroupMarker()) {
            this.action.setEnabled(false);
            IContributionItem[] items = getItems();
            for (int i = 0; i < items.length; i++)
                if (!items[i].isGroupMarker()) {
                    this.action.setEnabled(true);
                    break;
                }
        }
    }

    protected void setDefaultAction(IAction defaultAction) {
        this.defaultAction = defaultAction;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.action.IContributionItem#setParent(org.eclipse.jface
     * .action.IContributionManager)
     */
    @Override
    public void setParent(IContributionManager parent) {
        this.actionContributionItem.setParent(parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IContributionItem#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        this.actionContributionItem.setVisible(visible);
    }

    /**
     * Handle subaction selection
     * 
     * @param subActionHandler
     *            The selected sub action handler
     */
    protected void subActionSelected(IAction subActionHandler) {
        /* method not implemented */
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IContributionItem#update()
     */
    @Override
    public void update() {
        this.actionContributionItem.update();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IContributionManager#update(boolean)
     */
    @Override
    public void update(boolean force) {
        update();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IContributionItem#update(java.lang.String)
     */
    @Override
    public void update(String id) {
        this.actionContributionItem.update(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IMenuManager#updateAll(boolean)
     */
    @Override
    public void updateAll(boolean force) {
        update(force);

        IContributionItem[] items = getRealItems();
        for (int i = 0; i < items.length; ++i) {
            IContributionItem ci = items[i];
            if (ci instanceof IMenuManager) {
                IMenuManager mm = (IMenuManager) ci;
                if (mm.isVisible()) {
                    mm.updateAll(force);
                }
            }
        }
    }

}