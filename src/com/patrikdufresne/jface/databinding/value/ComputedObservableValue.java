package com.patrikdufresne.jface.databinding.value;

import java.util.Arrays;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
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
	private class PrivateInterface implements Runnable, IChangeListener,
			IStaleListener {
		public void handleChange(ChangeEvent event) {
			makeDirty();
		}

		public void handleStale(StaleEvent event) {
			if (!dirty && !stale) {
				stale = true;
				fireStale();
			}
		}

		public void run() {
			cachedValue = calculate();
		}
	}

	private Object cachedValue = null;

	/**
	 * Array of observables this computed value depends on. This field has a
	 * value of <code>null</code> if we are not currently listening.
	 */
	private IObservable[] dependencies = null;

	private boolean dirty = true;

	private PrivateInterface privateInterface = new PrivateInterface();

	private boolean stale = false;

	private Object valueType;

	public ComputedObservableValue(IObservableValue... dependencies) {
		this(Realm.getDefault(), null, dependencies);
	}

	/**
	 * @param valueType
	 *            can be <code>null</code>
	 */
	public ComputedObservableValue(Object valueType,
			IObservableValue... dependencies) {
		this(Realm.getDefault(), valueType, dependencies);
	}

	/**
	 * @param realm
	 * 
	 */
	public ComputedObservableValue(Realm realm,
			IObservableValue... dependencies) {
		this(realm, null, dependencies);
	}

	/**
	 * @param realm
	 * @param valueType
	 */
	public ComputedObservableValue(Realm realm, Object valueType,
			IObservableValue... dependencies) {
		super(realm);
		this.valueType = valueType;
		this.dependencies = dependencies;
	}

	public synchronized void addChangeListener(IChangeListener listener) {
		super.addChangeListener(listener);
		// If somebody is listening, we need to make sure we attach our own
		// listeners
		computeValueForListeners();
	}

	public synchronized void addValueChangeListener(
			IValueChangeListener listener) {
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

	public synchronized void dispose() {
		super.dispose();
		stopListening();
	}

	/**
	 * This implementation execute the {@link #calculate()} function only if
	 * this observable is identify as dirty. Otherwise, the cached value is
	 * returned.
	 */
	protected final Object doGetValue() {
		if (dirty) {

			cachedValue = calculate();

			dirty = false;
			
			startListening();

			// This line will do the following:
			// - Run the calculate method
			// - While doing so, add any observable that is touched to the
			// dependencies list
			// IObservable[] newDependencies = ObservableTracker.runAndMonitor(
			// privateInterface, privateInterface, null);

			// stale = false;
			// for (int i = 0; i < dependencies.length; i++) {
			// IObservable observable = dependencies[i];
			// // Add a change listener to the new dependency.
			// if (observable.isStale()) {
			// stale = true;
			// }
			// // else {
			// // observable.addStaleListener(privateInterface);
			// // }
			// }

			// dependencies = newDependencies;

		}

		return cachedValue;
	}

	public Object getValueType() {
		return valueType;
	}

	// this method exists here so that we can call it from the runnable below.
	/**
	 * @since 1.1
	 */
	protected boolean hasListeners() {
		return super.hasListeners();
	}

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

				public Object getNewValue() {
					return getValue();
				}

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
