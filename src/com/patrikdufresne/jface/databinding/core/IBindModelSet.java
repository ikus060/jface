package com.patrikdufresne.jface.databinding.core;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateSetStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.set.IObservableSet;

public interface IBindModelSet {

	IBindModelSet convert(IConverter converter);

	Binding model(IObservableSet model);

	IBindModelSet strategy(UpdateSetStrategy modelToTarget);

	IBindModelSet policyNever();

	IBindModelSet policyOnRequest();

	IBindModelSet policyUpdate();

}
