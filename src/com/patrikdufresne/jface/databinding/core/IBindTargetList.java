package com.patrikdufresne.jface.databinding.core;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.list.IObservableList;

public interface IBindTargetList {

	IBindTargetList convert(IConverter converter);

	IBindTargetList strategy(UpdateListStrategy modelToTarget);

	IBindTargetList policyNever();

	IBindTargetList policyOnRequest();

	IBindTargetList policyUpdate();

	IBindModelList with();

	Binding withModel(IObservableList model);

}