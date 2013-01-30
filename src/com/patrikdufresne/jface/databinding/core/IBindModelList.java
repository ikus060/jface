package com.patrikdufresne.jface.databinding.core;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.list.IObservableList;

public interface IBindModelList {

	IBindModelList convert(IConverter converter);

	Binding model(IObservableList model);

	IBindModelList strategy(UpdateListStrategy modelToTarget);

	IBindModelList policyNever();

	IBindModelList policyOnRequest();

	IBindModelList policyUpdate();

}
