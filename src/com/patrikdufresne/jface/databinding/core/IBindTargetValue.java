package com.patrikdufresne.jface.databinding.core;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;

public interface IBindTargetValue {

	IBindTargetValue afterConvert(IValidator validator);

	IBindTargetValue afterGet(IValidator validator);

	IBindTargetValue beforeSet(IValidator validator);

	IBindTargetValue convert(IConverter converter);

	IBindTargetValue strategy(UpdateValueStrategy modelToTarget);

	IBindTargetValue policyNever();

	IBindTargetValue policyOnRequest();

	IBindTargetValue policyUpdate();

	IBindModelValue with();

	Binding withModel(IObservableValue model);

}