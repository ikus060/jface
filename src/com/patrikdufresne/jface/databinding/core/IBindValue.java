package com.patrikdufresne.jface.databinding.core;

import org.eclipse.core.databinding.observable.value.IObservableValue;

public interface IBindValue {

	IBindTargetValue target(IObservableValue observable);

}
