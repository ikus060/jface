package com.patrikdufresne.jface.databinding.core;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateSetStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.set.IObservableSet;

public interface IBindTargetSet {

	IBindTargetSet convert(IConverter converter);

	IBindTargetSet strategy(UpdateSetStrategy modelToTarget);

	IBindTargetSet policyNever();

	IBindTargetSet policyOnRequest();

	IBindTargetSet policyUpdate();

	IBindModelSet with();

	Binding withModel(IObservableSet model);

}