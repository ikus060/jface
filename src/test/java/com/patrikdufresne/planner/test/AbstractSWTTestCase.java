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
package com.patrikdufresne.planner.test;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;

public class AbstractSWTTestCase {

    private Shell shell;

    public AbstractSWTTestCase() {
        super();
    }

    @After
    public void disposeShell() throws Exception {
        if (shell != null && !shell.isDisposed()) {
            shell.dispose();
        }
    }

    /**
     * Returns a Shell to be used in a test.
     * 
     * @return shell
     */
    protected Shell getShell() {
        if (shell == null || shell.isDisposed()) {
            shell = new Shell();
            shell.setLayout(new FillLayout());
        }

        return shell;
    }

}