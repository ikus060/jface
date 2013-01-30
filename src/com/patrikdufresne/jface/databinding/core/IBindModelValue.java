package com.patrikdufresne.jface.databinding.core;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;

public interface IBindModelValue {

	IBindModelValue afterConvert(IValidator validator);

	IBindModelValue afterGet(IValidator validator);

	IBindModelValue beforeSet(IValidator validator);

	IBindModelValue convert(IConverter converter);

	Binding model(IObservableValue model);

	IBindModelValue strategy(UpdateValueStrategy modelToTarget);

	IBindModelValue policyConvert();

	IBindModelValue policyNever();

	IBindModelValue policyOnRequest();

	IBindModelValue policyUpdate();

}
