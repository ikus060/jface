/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.conversion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.databinding.conversion.Converter;

/**
 * This converter allows to create an acronym from a string. The algorithm use
 * the first latter of any work in the string. 
 * 
 * @author Patrik Dufresne
 * 
 */
public class AcronymConverter extends Converter {

	/**
	 * Singleton instance of this class.
	 */
	private static AcronymConverter instance;

	/**
	 * Return a singleton instance of this class.
	 * 
	 * @return
	 */
	public static AcronymConverter getInstance() {
		if (instance == null) {
			instance = new AcronymConverter();
		}
		return instance;
	}

	private static final String EMPTY = ""; //$NON-NLS-1$
	private static final String SPACE = " "; //$NON-NLS-1$

	/**
	 * Create a new converter. it's recommanded to use {@link #getInstance()}
	 * function to get an instance of this class.
	 */
	public AcronymConverter() {
		super(String.class, String.class);
	}

	/**
	 * This implementation create an acronym from the input string.
	 */
	@Override
	public Object convert(Object fromObject) {
		String value = (String) fromObject;
		// value = Pattern.compile("[éèêë]").matcher(value).replaceAll("e");
		// value = Pattern.compile("[àâ]").matcher(value).replaceAll("a");
		// value = Pattern.compile("[àâ]").matcher(value).replaceAll("a");
		// value = Pattern.compile("[ùûü]").matcher(value).replaceAll("u");
		// value = Pattern.compile("[ôöò]").matcher(value).replaceAll("o");
		value = Pattern.compile("[\\.\\-]", Pattern.CASE_INSENSITIVE)
				.matcher(value).replaceAll(SPACE);
		value = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE)
				.matcher(value).replaceAll(EMPTY);
		value = Pattern.compile("( de | la )", Pattern.CASE_INSENSITIVE)
				.matcher(value).replaceAll(SPACE);
		value = value.trim();
		StringBuilder sb = new StringBuilder();
		sb.append(value.substring(0, 1).toUpperCase());
		Matcher m = Pattern.compile("\\s+([a-z0-9])", Pattern.CASE_INSENSITIVE).matcher(value);
		while (m.find()) {
			sb.append(m.group(1).toUpperCase());
		}
		return sb.toString();
	}

}
