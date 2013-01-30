package com.patrikdufresne.jface.databinding.core;

import org.eclipse.core.databinding.observable.list.IObservableList;

public interface IBindList {

	IBindTargetValue target(IObservableList observable);

}
