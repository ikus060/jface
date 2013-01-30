/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.core;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateSetStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;

/**
 * Utility class to create one liner data binding with function chaining.
 * 
 * @author Patrik Dufresne
 * 
 */
public class Bind implements IBindValue, IBindModelValue, IBindTargetValue,
		IBindSet, IBindModelSet, IBindTargetSet, IBindList, IBindModelList,
		IBindTargetList {

	private static final int CURRENT_MODEL = 1;
	private static final int CURRENT_TARGET = 0;
	private static final int POLICY_CONVERT = 3;

	private static final int POLICY_NEVER = 1;
	private static final int POLICY_ON_REQUEST = 2;

	private static final int POLICY_UPDATE = 4;

	private static final int TYPE_LIST = 1;

	private static final int TYPE_SET = 2;

	private static final int TYPE_VALUE = 0;

	public static Bind list(DataBindingContext dbc) {
		if (dbc == null) {
			throw new IllegalArgumentException();
		}
		return new Bind(dbc, TYPE_LIST);
	}

	public static Bind set(DataBindingContext dbc) {
		if (dbc == null) {
			throw new IllegalArgumentException();
		}
		return new Bind(dbc, TYPE_SET);
	}

	public static Bind value(DataBindingContext dbc) {
		if (dbc == null) {
			throw new IllegalArgumentException();
		}
		return new Bind(dbc, TYPE_VALUE);
	}

	int current = CURRENT_TARGET;

	/**
	 * The databinding context to be used.
	 */
	DataBindingContext dbc;

	IObservable model;

	/**
	 * The update strategy.
	 */
	Object modelToTarget;

	IObservable target;

	/**
	 * The update strategy.
	 */
	Object targetToModel;

	final int type;

	protected Bind(DataBindingContext dbc, int type) {
		this.dbc = dbc;
		this.type = type;
	}

	@Override
	public Bind afterConvert(IValidator validator) {
		if (this.type != TYPE_VALUE) {
			throw new IllegalArgumentException();
		}
		((UpdateValueStrategy) getOrCreateStrategy())
				.setAfterConvertValidator(validator);
		return this;
	}

	@Override
	public Bind afterGet(IValidator validator) {
		if (this.type != TYPE_VALUE) {
			throw new IllegalArgumentException();
		}
		((UpdateValueStrategy) getOrCreateStrategy())
				.setAfterGetValidator(validator);
		return this;
	}

	@Override
	public Bind beforeSet(IValidator validator) {
		if (this.type != TYPE_VALUE) {
			throw new IllegalArgumentException();
		}
		((UpdateValueStrategy) getOrCreateStrategy())
				.setBeforeSetValidator(validator);
		return this;
	}

	@Override
	public Bind convert(IConverter converter) {
		switch (this.type) {
		case TYPE_LIST:
			((UpdateListStrategy) getOrCreateStrategy())
					.setConverter(converter);
		case TYPE_SET:
			((UpdateSetStrategy) getOrCreateStrategy()).setConverter(converter);
		case TYPE_VALUE:
			((UpdateValueStrategy) getOrCreateStrategy())
					.setConverter(converter);
			break;
		default:
			throw new IllegalArgumentException();
		}
		return this;
	}

	protected Object createUpdateStrategy(boolean provideDefaults,
			int updatePolicy) {
		int policy;
		switch (this.type) {
		case TYPE_VALUE:
			policy = UpdateValueStrategy.POLICY_UPDATE;
			if (updatePolicy == POLICY_NEVER)
				policy = UpdateValueStrategy.POLICY_NEVER;
			if (updatePolicy == POLICY_ON_REQUEST)
				policy = UpdateValueStrategy.POLICY_ON_REQUEST;
			if (updatePolicy == POLICY_UPDATE)
				policy = UpdateValueStrategy.POLICY_CONVERT;
			return new UpdateValueStrategy(provideDefaults, policy);
		case TYPE_LIST:
			policy = UpdateListStrategy.POLICY_UPDATE;
			if (updatePolicy == POLICY_NEVER)
				policy = UpdateListStrategy.POLICY_NEVER;
			if (updatePolicy == POLICY_ON_REQUEST)
				policy = UpdateListStrategy.POLICY_ON_REQUEST;
			return new UpdateListStrategy(provideDefaults, policy);
		case TYPE_SET:
			policy = UpdateSetStrategy.POLICY_UPDATE;
			if (updatePolicy == POLICY_NEVER)
				policy = UpdateSetStrategy.POLICY_NEVER;
			if (updatePolicy == POLICY_ON_REQUEST)
				policy = UpdateSetStrategy.POLICY_ON_REQUEST;
			return new UpdateSetStrategy(provideDefaults, policy);
		}
		throw new IllegalArgumentException();
	}

	protected Object getOrCreateStrategy() {
		return getOrCreateStrategy(true, POLICY_CONVERT);

	}

	protected Object getOrCreateStrategy(boolean provideDefaults, int style) {
		if (this.current == CURRENT_TARGET) {
			return targetToModel != null ? targetToModel
					: (targetToModel = createUpdateStrategy(provideDefaults,
							style));
		}
		return modelToTarget != null ? modelToTarget
				: (modelToTarget = createUpdateStrategy(provideDefaults, style));
	}

	@Override
	public Binding model(IObservableList model) {
		if (this.type != TYPE_LIST) {
			throw new IllegalArgumentException();
		}
		return this.dbc.bindList((IObservableList) target, model,
				(UpdateListStrategy) targetToModel,
				(UpdateListStrategy) modelToTarget);
	}

	@Override
	public Binding model(IObservableSet model) {
		if (this.type != TYPE_LIST) {
			throw new IllegalArgumentException();
		}
		return this.dbc.bindSet((IObservableSet) target, model,
				(UpdateSetStrategy) targetToModel,
				(UpdateSetStrategy) modelToTarget);
	}

	@Override
	public Binding model(IObservableValue model) {
		if (this.type != TYPE_LIST) {
			throw new IllegalArgumentException();
		}
		return this.dbc.bindValue((IObservableValue) target, model,
				(UpdateValueStrategy) targetToModel,
				(UpdateValueStrategy) modelToTarget);
	}

	@Override
	public Bind policyConvert() {
		getOrCreateStrategy(true, POLICY_CONVERT);
		return this;
	}

	@Override
	public Bind policyNever() {
		getOrCreateStrategy(true, POLICY_NEVER);
		return this;
	}

	@Override
	public Bind policyOnRequest() {
		getOrCreateStrategy(true, POLICY_ON_REQUEST);
		return this;
	}

	@Override
	public Bind policyUpdate() {
		if (this.type != TYPE_VALUE) {
			throw new IllegalArgumentException();
		}
		getOrCreateStrategy(true, POLICY_CONVERT);
		return this;
	}

	protected void setStrategy(Object updateStrategy) {
		if (this.current == CURRENT_TARGET) {
			if (targetToModel != null) {
				throw new IllegalArgumentException();
			}
			this.targetToModel = updateStrategy;
		}

		if (this.modelToTarget != null) {
			throw new IllegalArgumentException();
		}
		this.modelToTarget = updateStrategy;
	}

	@Override
	public Bind strategy(UpdateListStrategy modelToTarget) {
		if (this.type != TYPE_LIST) {
			throw new IllegalArgumentException();
		}
		setStrategy(modelToTarget);
		return this;
	}

	@Override
	public Bind strategy(UpdateSetStrategy modelToTarget) {
		if (this.type != TYPE_SET) {
			throw new IllegalArgumentException();
		}
		setStrategy(modelToTarget);
		return this;
	}

	@Override
	public Bind strategy(UpdateValueStrategy modelToTarget) {
		if (this.type != TYPE_VALUE) {
			throw new IllegalArgumentException();
		}
		setStrategy(modelToTarget);
		return this;
	}

	@Override
	public Bind target(IObservableList observable) {
		if (observable == null || this.type != TYPE_LIST || this.target != null) {
			throw new IllegalArgumentException();
		}
		this.target = observable;
		return this;
	}

	@Override
	public Bind target(IObservableSet observable) {
		if (observable == null || this.type != TYPE_SET || this.target != null) {
			throw new IllegalArgumentException();
		}
		this.target = observable;
		return this;
	}

	@Override
	public Bind target(IObservableValue observable) {
		if (observable == null || this.type != TYPE_VALUE
				|| this.target != null) {
			throw new IllegalArgumentException();
		}
		this.target = observable;
		return this;
	}

	@Override
	public Bind with() {
		if (this.current != CURRENT_TARGET) {
			throw new IllegalArgumentException();
		}
		this.current = CURRENT_MODEL;
		return this;
	}

	@Override
	public Binding withModel(IObservableList model) {
		return with().model(model);
	}

	@Override
	public Binding withModel(IObservableSet model) {
		return with().model(model);
	}

	@Override
	public Binding withModel(IObservableValue model) {
		return with().model(model);
	}

}
