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
