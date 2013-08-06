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

import java.util.Arrays;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.internal.databinding.ConverterValueProperty;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * A simple TableViewer to demonstrate usage
 * 
 * @author Karl St-Cyr
 */
public class ColumnSupportTableViewerExample {
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
                new ColumnSupportTableViewerExample(shell);
                shell.open();
                while (!shell.isDisposed()) {
                    if (!display.readAndDispatch()) display.sleep();
                }
            }
        });
        display.dispose();
    }

    public ColumnSupportTableViewerExample(final Composite comp) {
        final TableViewer v = new TableViewer(new Table(comp, SWT.BORDER));
        ObservableListContentProvider olcp = new ObservableListContentProvider();
        v.setContentProvider(olcp);
        v.getTable().setHeaderVisible(true);
        MyModel[] model = createModel();
        IObservableList list = Observables.staticObservableList(Arrays.asList(model));
        v.setInput(list);
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
        v.getTable().setLinesVisible(true);
    }

    private MyModel[] createModel() {
        MyModel[] elements = new MyModel[10];
        for (int i = 0; i < 10; i++) {
            elements[i] = new MyModel(i);
        }
        return elements;
    }
}