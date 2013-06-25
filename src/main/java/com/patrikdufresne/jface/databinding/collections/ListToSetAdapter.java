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
package com.patrikdufresne.jface.databinding.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ObservableSet;

/**
 * This implementation of {@link ListToSetAdapter} extends the default features to allow the client to update the the
 * {@link IObservableSet}.
 * <p>
 * The wrapped list must not contain duplicate elements.
 * 
 * @author Patrik Dufresne
 * 
 */
public class ListToSetAdapter extends ObservableSet {

    /**
     * The adaptee.
     */
    protected final IObservableList list;

    private IListChangeListener listener = new IListChangeListener() {

        @Override
        public void handleListChange(ListChangeEvent event) {
            Set added = new HashSet();
            Set removed = new HashSet();
            ListDiffEntry[] differences = event.diff.getDifferences();
            for (int i = 0; i < differences.length; i++) {
                ListDiffEntry entry = differences[i];
                Object element = entry.getElement();
                if (entry.isAddition()) {
                    if (wrappedSet.add(element)) {
                        if (!removed.remove(element)) added.add(element);
                    }
                } else {
                    if (wrappedSet.remove(element)) {
                        removed.add(element);
                        added.remove(element);
                    }
                }
            }
            fireSetChange(Diffs.createSetDiff(added, removed));
        }
    };

    /**
     * Create a new instance of IObservateSet to adapte a list.
     * 
     * @param list
     */
    public ListToSetAdapter(IObservableList list) {
        super(list.getRealm(), new HashSet(), list.getElementType());
        this.list = list;
        this.wrappedSet.addAll(list);
        this.list.addListChangeListener(listener);
    }

    /**
     * This implementation delegate the add opperation to the adaptee.
     */
    @Override
    public boolean add(Object o) {
        // getterCalled();
        return this.list.add(o);
    }

    @Override
    public boolean addAll(Collection c) {
        // getterCalled();
        return this.list.addAll(c);
    }

    @Override
    public void clear() {
        // getterCalled();
        this.list.clear();
    }

    public synchronized void dispose() {
        super.dispose();
        if (list != null && listener != null) {
            list.removeListChangeListener(listener);
            listener = null;
        }
    }

    @Override
    public boolean remove(Object o) {
        // getterCalled();
        return this.list.remove(o);

    }

    @Override
    public boolean removeAll(Collection c) {
        // getterCalled();
        return this.list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection c) {
        // getterCalled();
        return this.list.retainAll(c);
    }

}
