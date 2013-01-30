package com.patrikdufresne.jface.databinding.core;

import org.eclipse.core.databinding.observable.set.IObservableSet;

public interface IBindSet {

	IBindTargetSet target(IObservableSet observable);

}
