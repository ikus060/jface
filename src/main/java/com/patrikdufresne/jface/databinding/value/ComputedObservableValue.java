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
package com.patrikdufresne.jface.databinding.value;

import java.util.Arrays;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.DisposeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IDisposeListener;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.IStaleListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.StaleEvent;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueDiff;

public abstract class ComputedObservableValue extends AbstractObservableValue {

    /**
     * Inner class that implements interfaces that we don't want to expose as
     * public API. Each interface could have been implemented using a separate
     * anonymous class, but we combine them here to reduce the memory overhead
     * and number of classes.
     * 
     * <p>
     * The Runnable calls computeValue and stores the result in cachedValue.
     * </p>
     * 
     * <p>
     * The IChangeListener stores each observable in the dependencies list. This
     * is registered as the listener when calling ObservableTracker, to detect
     * every observable that is used by computeValue.
     * </p>
     * 
     * <p>
     * The IChangeListener is attached to every dependency.
     * </p>
     * 
     */
    private class PrivateInterface implements Runnable, IChangeListener, IStaleListener, IDisposeListener {
        @Override
        public void handleChange(ChangeEvent event) {
            makeDirty();
        }

        @Override
        public void handleStale(StaleEvent event) {
            if (!dirty && !stale) {
                stale = true;
                fireStale();
            }
        }

        @Override
        public void run() {
            cachedValue = calculate();
        }

        /**
         * This implementation will dipose this observable.
         */
        @Override
        public void handleDispose(DisposeEvent event) {
            dispose();
        }
    }

    private Object cachedValue = null;

    /**
     * Array of observables this computed value depends on. This field has a
     * value of <code>null</code> if we are not currently listening.
     */
    private IObservable[] dependencies = null;

    private boolean dirty = true;

    private boolean disposeOnDependencyDispose;

    private PrivateInterface privateInterface = new PrivateInterface();

    private boolean stale = false;

    private Object valueType;

    /**
     * Create a new computed observable value with default realm, without value
     * type and the given dependency list.
     * 
     * @param disposeOnDependencyDispose
     *            True to dispose this observable whenever one of it's
     *            dependency is disposed.
     * @param dependencies
     *            list of dependency.
     */
    public ComputedObservableValue(boolean disposeOnDependencyDispose, IObservableValue... dependencies) {
        this(Realm.getDefault(), null, disposeOnDependencyDispose, dependencies);
    }

    /**
     * Create a new computed observable value with default realm, without value
     * type and the given dependency list.
     * 
     * @param dependencies
     *            list of dependency.
     */
    public ComputedObservableValue(IObservableValue... dependencies) {
        this(Realm.getDefault(), null, dependencies);
    }

    /**
     * Create a new computed observable value with default realm and the given
     * dependencies dependencies.
     * 
     * @param valueType
     *            this observable value type or null
     * @param disposeOnDependencyDispose
     *            True to dispose this observable whenever one of it's
     *            dependency is disposed.
     * @param dependencies
     *            list of dependency.
     */
    public ComputedObservableValue(Object valueType, boolean disposeOnDependencyDispose, IObservableValue... dependencies) {
        this(Realm.getDefault(), valueType, disposeOnDependencyDispose, dependencies);
    }

    /**
     * Create a new computed observable value with default realm and the given
     * dependencies dependencies.
     * 
     * @param valueType
     *            this observable value type or null
     * @param dependencies
     *            list of dependency.
     */
    public ComputedObservableValue(Object valueType, IObservableValue... dependencies) {
        this(Realm.getDefault(), valueType, dependencies);
    }

    /**
     * Create a new computed observable value with the given realm and
     * dependencies.
     * 
     * @param realm
     *            the realm of this observable
     * @param disposeOnDependencyDispose
     *            True to dispose this observable whenever one of it's
     *            dependency is disposed.
     * @param dependencies
     *            list of dependency.
     */
    public ComputedObservableValue(Realm realm, boolean disposeOnDependencyDispose, IObservableValue... dependencies) {
        this(realm, null, disposeOnDependencyDispose, dependencies);
    }

    /**
     * Create a new computed observable value with the given realm and
     * dependencies.
     * 
     * @param realm
     *            the realm of this observable
     * 
     * @param dependencies
     *            list of dependency.
     */
    public ComputedObservableValue(Realm realm, IObservableValue... dependencies) {
        this(realm, null, dependencies);
    }

    /**
     * Create a new computed observable value.
     * 
     * @param realm
     *            the realm of this observable
     * @param valueType
     *            this observable value type
     * @param disposeOnDependencyDispose
     *            True to dispose this observable whenever one of it's
     *            dependency is disposed.
     * @param dependencies
     *            list of dependency.
     */
    public ComputedObservableValue(Realm realm, Object valueType, boolean disposeOnDependencyDispose, IObservableValue... dependencies) {
        super(realm);
        this.valueType = valueType;
        this.disposeOnDependencyDispose = disposeOnDependencyDispose;
        this.dependencies = dependencies;
        if (this.disposeOnDependencyDispose) {
            for (IObservableValue dependency : dependencies) {
                dependency.addDisposeListener(this.privateInterface);
            }
        }
    }

    /**
     * Create a new computed observable value.
     * 
     * @param realm
     *            the realm of this observable
     * @param valueType
     *            this observable value type
     * @param dependencies
     *            list of dependency.
     */
    public ComputedObservableValue(Realm realm, Object valueType, IObservableValue... dependencies) {
        this(realm, valueType, false, dependencies);
    }

    @Override
    public synchronized void addChangeListener(IChangeListener listener) {
        super.addChangeListener(listener);
        // If somebody is listening, we need to make sure we attach our own
        // listeners
        computeValueForListeners();
    }

    @Override
    public synchronized void addValueChangeListener(IValueChangeListener listener) {
        super.addValueChangeListener(listener);
        // If somebody is listening, we need to make sure we attach our own
        // listeners
        computeValueForListeners();
    }

    /**
     * Subclasses must override this method to provide the object's value. Any
     * dependencies used to calculate the value must be {@link IObservable}, and
     * implementers must use one of the interface methods tagged TrackedGetter
     * for ComputedValue to recognize it as a dependency.
     * 
     * @return the object's value
     */
    protected abstract Object calculate();

    /**
     * Some clients just add a listener and expect to get notified even if they
     * never called getValue(), so we have to call getValue() ourselves here to
     * be sure. Need to be careful about realms though, this method can be
     * called outside of our realm. See also bug 198211. If a client calls this
     * outside of our realm, they may receive change notifications before the
     * runnable below has been executed. It is their job to figure out what to
     * do with those notifications.
     */
    private void computeValueForListeners() {
        getRealm().exec(new Runnable() {
            @Override
            public void run() {
                if (dependencies == null) {
                    // We are not currently listening.
                    if (hasListeners()) {
                        // But someone is listening for changes. Call getValue()
                        // to make sure we start listening to the observables we
                        // depend on.
                        getValue();
                    }
                }
            }
        });
    }

    @Override
    public synchronized void dispose() {
        super.dispose();
        stopListening();
    }

    /**
     * This implementation execute the {@link #calculate()} function only if
     * this observable is identify as dirty. Otherwise, the cached value is
     * returned.
     */
    @Override
    protected final Object doGetValue() {
        if (dirty) {

            cachedValue = calculate();

            dirty = false;

            startListening();

        }

        return cachedValue;
    }

    @Override
    public Object getValueType() {
        return valueType;
    }

    // this method exists here so that we can call it from the runnable below.
    /**
     * @since 1.1
     */
    @Override
    protected boolean hasListeners() {
        return super.hasListeners();
    }

    @Override
    public boolean isStale() {
        // we need to recompute, otherwise staleness wouldn't mean anything
        getValue();
        return stale;
    }

    protected final void makeDirty() {
        if (!dirty) {
            dirty = true;

            stopListening();

            // copy the old value
            final Object oldValue = cachedValue;
            // Fire the "dirty" event. This implementation recomputes the new
            // value lazily.
            fireValueChange(new ValueDiff() {

                @Override
                public Object getNewValue() {
                    return getValue();
                }

                @Override
                public Object getOldValue() {
                    return oldValue;
                }
            });
        }
    }

    /**
     * This function is used to start listening on dependencies.
     */
    protected void startListening() {
        // Stop listening for dependency changes.
        if (dependencies != null) {
            for (int i = 0; i < dependencies.length; i++) {
                IObservable observable = dependencies[i];
                observable.addChangeListener(privateInterface);
                observable.addStaleListener(privateInterface);
            }
            dependencies = null;
        }
    }

    /**
     * This function is used to stop listening on dependencies.
     */
    protected void stopListening() {
        // Stop listening for dependency changes.
        if (dependencies != null) {
            for (int i = 0; i < dependencies.length; i++) {
                IObservable observable = dependencies[i];
                observable.removeChangeListener(privateInterface);
                observable.removeStaleListener(privateInterface);
            }
            dependencies = null;
        }
    }

}
