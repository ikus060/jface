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
package com.patrikdufresne.jface.databinding.viewers;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.internal.databinding.ConverterValueProperty;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableSetTreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * A simple TreeViewer to demonstrate usage
 * 
 * @author Karl St-Cyr
 */
public class ColumnSupportTreeViewerExample {
    public class MyModel {
        public int counter;

        public MyModel(int counter) {
            this.counter = counter;
        }

        @Override
        public String toString() {
            return "Item " + this.counter;
        }
    }

    public static final Object ROOT = new Object();

    /**
     * @param args
     */
    public static void main(String[] args) {
        final Display display = new Display();
        Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
            @Override
            public void run() {
                Shell shell = new Shell(display);
                shell.setLayout(new FillLayout());
                new ColumnSupportTreeViewerExample(shell);
                shell.open();
                while (!shell.isDisposed()) {
                    if (!display.readAndDispatch()) display.sleep();
                }
            }
        });
        display.dispose();
    }

    public ColumnSupportTreeViewerExample(final Composite comp) {
        final TreeViewer v = new TreeViewer(comp, SWT.BORDER);
        ObservableSetTreeContentProvider olcp = new ObservableSetTreeContentProvider(new IObservableFactory() {
            @Override
            public IObservable createObservable(Object target) {
                if (ROOT.equals(target)) {
                    return createModel();
                }
                return null;
            }
        }, null);
        v.setContentProvider(olcp);
        v.getTree().setHeaderVisible(true);
        v.setInput(ROOT);

        ColumnSupport.create(v, "Column 1", olcp.getKnownElements(), new ConverterValueProperty(new Converter(MyModel.class, String.class) {
            @Override
            public Object convert(Object fromObject) {
                return fromObject;
            }
        })).setPixelLayoutData(100).setResizable(true).setMoveable(false);
        ColumnSupport.create(v, "Column 2", olcp.getKnownElements(), new ConverterValueProperty(new Converter(MyModel.class, String.class) {
            @Override
            public Object convert(Object fromObject) {
                return fromObject;
            }
        })).setWeightLayoutData(1, 10).setResizable(true).setMoveable(false);
        ColumnSupport.create(v, "Column 3", olcp.getKnownElements(), new ConverterValueProperty(new Converter(MyModel.class, String.class) {
            @Override
            public Object convert(Object fromObject) {
                return fromObject;
            }
        })).setWeightLayoutData(1, 10).setResizable(false).setMoveable(false);
        v.getTree().setLinesVisible(true);
    }

    private WritableSet createModel() {
        WritableSet set = new WritableSet();
        for (int i = 0; i < 10; i++) {
            set.add(new MyModel(i));
        }
        return set;
    }
}