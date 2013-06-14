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
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IStaleListener;
import org.eclipse.core.databinding.observable.StaleEvent;
import org.eclipse.core.databinding.observable.set.AbstractObservableSet;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.observable.set.SetDiff;

/**
 * Filter an existing observable set.
 * 
 * @author Patrik Dufresne
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractFilteredObservableSet extends AbstractObservableSet {
    /**
     * Inner class that implements interfaces that we don't want to expose as
     * public API. Each interface could have been implemented using a separate
     * anonymous class, but we combine them here to reduce the memory overhead
     * and number of classes.
     * 
     * <p>
     * The IChangeListener is attached to every dependency.
     * </p>
     * 
     * <p>
     * IManagerObserver is attache to managers.
     * </p>
     * 
     */
    private class PrivateInterface implements IChangeListener, IStaleListener, ISetChangeListener {

        /**
         * Public constructor.
         */
        public PrivateInterface() {

        }

        /**
         * Notify this class about pattern change.
         */
        @Override
        public void handleChange(ChangeEvent event) {
            makeDirty();
        }

        /**
         * Notify this class about change in the observed set.
         */
        @Override
        public void handleSetChange(SetChangeEvent event) {
            Set<Object> additions = new HashSet<Object>();
            Set<Object> removals = new HashSet<Object>();
            for (Object element : event.diff.getAdditions()) {
                if (doSelect(element)) {
                    if (cachedSet.add(element)) {
                        additions.add(element);
                    }
                }
            }
            for (Object element : event.diff.getRemovals()) {
                if (cachedSet.remove(element)) {
                    removals.add(element);
                }
            }
            // Fire change
            if (additions.size() != 0 || removals.size() != 0) {
                fireSetChange(Diffs.createSetDiff(additions, removals));
            }
        }

        @Override
        public void handleStale(StaleEvent event) {
            if (!AbstractFilteredObservableSet.this.dirty) makeStale();
        }
    }

    /** Cached set */
    Set cachedSet = new HashSet();

    /** True if dirty */
    boolean dirty = true;

    /** The element type of this observable */
    private Object elementType;

    private PrivateInterface privateInterface = new PrivateInterface();

    /**
     * The observable set to be filter
     */
    private IObservableSet innerSet;

    /**
     * Return the adapted set.
     * @return
     */
    protected IObservableSet getInnerSet() {
        return this.innerSet;
    }

    private boolean stale = false;

    /**
     * Create an observable filter for the set specified.
     * 
     * @param innerSet
     *            the observable set to filter
     */
    public AbstractFilteredObservableSet(IObservableSet innerSet) {
        if (innerSet == null) {
            throw new IllegalArgumentException();
        }
        this.innerSet = innerSet;
    }

    /**
     * This implementation throw an exception.
     */
    @Override
    public boolean add(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * This implementation throw an exception.
     */
    @Override
    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void addChangeListener(IChangeListener listener) {
        super.addChangeListener(listener);
        // If somebody is listening, we need to make sure we attach our own
        // listeners
        computeSetForListeners();
    }

    @Override
    public synchronized void addSetChangeListener(ISetChangeListener listener) {
        super.addSetChangeListener(listener);
        // If somebody is listening, we need to make sure we attach our own
        // listeners
        computeSetForListeners();
    }

    /**
     * This implementation throw an exception.
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    private void computeSetForListeners() {
        // Some clients just add a listener and expect to get notified even if
        // they never called getValue(), so we have to call getValue() ourselves
        // here to be sure. Need to be careful about realms though, this method
        // can be called outside of our realm.
        // See also bug 198211. If a client calls this outside of our realm,
        // they may receive change notifications before the runnable below has
        // been executed. It is their job to figure out what to do with those
        // notifications.
        getRealm().exec(new Runnable() {
            @Override
            public void run() {
                if (AbstractFilteredObservableSet.this.dirty) {
                    // We are not currently listening.
                    // But someone is listening for changes. Call getValue()
                    // to make sure we start listening to the observables we
                    // depend on.
                    getSet();
                }
            }
        });
    }

    /**
     * This implementation remove listener.
     */
    @Override
    public synchronized void dispose() {
        try {
            stopListening();
            lastListenerRemoved();
            this.innerSet = null;
            this.privateInterface = null;
        } finally {
            super.dispose();
        }
    }

    /**
     * Query the database.
     * <p>
     * Sub classes may override this function to query the database using
     * something else then list().
     * 
     * @return a collection
     */
    protected Iterator doCompute() {
        final Iterator finalIterator = this.innerSet.iterator();
        // Create a filtered iterator view on the observable set iterator.
        return new Iterator() {
            private Object nextObject;
            private boolean nextObjectSet = false;

            @Override
            public boolean hasNext() {
                if (this.nextObjectSet) {
                    return true;
                }
                return setNextObject();
            }

            @Override
            public Object next() {
                if (!this.nextObjectSet) {
                    if (!setNextObject()) {
                        throw new NoSuchElementException();
                    }
                }
                this.nextObjectSet = false;
                return this.nextObject;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private boolean setNextObject() {
                while (finalIterator.hasNext()) {
                    Object object = finalIterator.next();
                    if (doSelect(object)) {
                        this.nextObject = object;
                        this.nextObjectSet = true;
                        return true;
                    }
                }
                return false;
            }
        };
    }

    final Set doGetSet() {

        if (this.dirty) {

            startListening();

            try {
                this.cachedSet = new HashSet();
                Iterator iter = doCompute();
                while (iter.hasNext()) {
                    this.cachedSet.add(iter.next());
                }
            } finally {
                this.dirty = false;
            }

        }

        return this.cachedSet;
    }

    /**
     * Check if the element should be selected. Default implementation always
     * return true.
     * <p>
     * Subclasses should override this function if it's override
     * {@link #doCompute()}.
     * 
     * @param element
     *            the element to check
     * @return True if the element should be selected
     */
    protected boolean doSelect(Object element) {
        return true;
    }

    @Override
    protected void fireSetChange(SetDiff diff) {
        super.fireSetChange(diff);
    }

    /**
     * This implementation returns the manager object class.
     */
    @Override
    public Object getElementType() {
        return this.elementType;
    }

    final Set getSet() {
        getterCalled();
        return doGetSet();
    }

    /**
     * This implementation always return null since all the primary function are
     * overrided.
     * 
     * @return
     */
    @Override
    protected Set getWrappedSet() {
        return doGetSet();
    }

    @Override
    public boolean isStale() {
        // recalculate set if dirty, to ensure staleness is correct.
        getSet();
        return this.stale;
    }

    protected void makeDirty() {
        if (!this.dirty) {
            this.dirty = true;

            makeStale();

            // The computed set is dirty and need to be recompute, then there is
            // no need to listen to dependencies again. The next call to
            // doGetSet will startListener again.
            stopListening();

            // copy the old set
            final Set oldSet = new HashSet(this.cachedSet);
            // Fire the "dirty" event. This implementation recomputes the new
            // set lazily.
            fireSetChange(new SetDiff() {
                SetDiff delegate;

                @Override
                public Set getAdditions() {
                    return getDelegate().getAdditions();
                }

                private SetDiff getDelegate() {
                    if (this.delegate == null) this.delegate = Diffs.computeSetDiff(oldSet, getSet());
                    return this.delegate;
                }

                @Override
                public Set getRemovals() {
                    return getDelegate().getRemovals();
                }
            });
        }
    }

    void makeStale() {
        if (!this.stale) {
            this.stale = true;
            fireStale();
        }
    }

    /**
     * This implementation throw an exception.
     */
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * This implementation throw an exception.
     */
    @Override
    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    /**
     * This implementation throw an exception.
     */
    @Override
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * Add listener to dependencies
     */
    protected void startListening() {
        if (this.innerSet != null) {
            this.innerSet.addSetChangeListener(this.privateInterface);
            this.innerSet.addStaleListener(this.privateInterface);
        }
    }

    /**
     * Remove listener from dependencies.
     */
    protected void stopListening() {
        if (this.innerSet != null) {
            this.innerSet.removeSetChangeListener(this.privateInterface);
            this.innerSet.removeStaleListener(this.privateInterface);
        }
    }

    @Override
    public String toString() {
        getterCalled();
        return "ObservableSetWithDependencies [" + getWrappedSet().toString() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
