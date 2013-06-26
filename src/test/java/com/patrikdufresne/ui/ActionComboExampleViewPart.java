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

import java.util.Arrays;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.patrikdufresne.jface.action.ActionCombo;
import com.patrikdufresne.jface.databinding.util.JFaceProperties;

public class ActionComboExampleViewPart extends AbstractViewPart {

    private ActionCombo actionDropDown;

    ActionComboExampleViewPart(String id) {
        super(id);
        setTitle("My Title");
        setTitleToolTip("My Tooltip text");
        this.setTitleImage(JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_INFO));
    }

    Label label;

    @Override
    public void activate(Composite parent) {
        label = new Label(parent, SWT.BORDER);
        label.setText("item1");
        label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        IToolBarManager toolbar = getSite().getToolBarManager();

        this.actionDropDown = new ActionCombo();
        this.actionDropDown.setDefaultMessage("Select an item");
        ActionContributionItem aci = new ActionContributionItem(this.actionDropDown);
        aci.setMode(ActionContributionItem.MODE_FORCE_TEXT);
        toolbar.add(aci);
        bind();
    }

    @Override
    protected void bindValues() {
        super.bindValues();
        WritableList input = new WritableList(Arrays.asList("item1", "item2", "item3", "item4"), String.class);

        // Bind items.
        getDbc().bindList(JFaceProperties.list(ActionCombo.class, ActionCombo.ITEMS, ActionCombo.ITEMS).observe(this.actionDropDown), input);

        // Bind the selection
        getDbc().bindValue(
                JFaceProperties.value(ActionCombo.class, ActionCombo.SELECTION, ActionCombo.SELECTION).observe(this.actionDropDown),
                WidgetProperties.text().observe(label));

    }
}