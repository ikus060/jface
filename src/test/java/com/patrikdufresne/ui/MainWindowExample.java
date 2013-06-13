/*
 * Copyright (c) 2013, Bell Canada All rights reserved.
 * Bell Canada PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Snippet to display {@link MainWindow} usage.
 * 
 * @author Patrik Dufresne
 * 
 */
public class MainWindowExample extends MainWindow {

    public MainWindowExample() {
        super(null);
    }

    @Override
    public void addViews() {
        addView(new ViewPart("PART_ID_1") {

            {
                setTitle("My Title");
                setTitleToolTip("My Tooltip text");
                this.setTitleImage(JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_INFO));
            }

            @Override
            public void activate(Composite parent) {
                Label label = new Label(parent, SWT.BORDER);
                label.setText("COUCOU");
            }

        });
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        MainWindowExample main = new MainWindowExample();
        main.setBlockOnOpen(true);
        main.open();
    }

}
