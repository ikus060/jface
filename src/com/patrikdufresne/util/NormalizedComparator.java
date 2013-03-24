package com.patrikdufresne.util;

import java.text.Normalizer;
import java.util.Comparator;

/**
 * Comparator used to compare string without accent. i.e.: e == é == è
 * 
 * @author Patrik Dufresne
 * 
 */
public class NormalizedComparator implements Comparator<String> {

	private static final String EMPTY = ""; //$NON-NLS-1$

	private static final String PATTERN = "[^\\p{ASCII}]"; //$NON-NLS-1$

	@Override
	public int compare(String s1, String s2) {
		return Normalizer
				.normalize(s1, Normalizer.Form.NFD)
				.replaceAll(PATTERN, EMPTY)
				.compareTo(
						Normalizer.normalize(s2, Normalizer.Form.NFD)
								.replaceAll(PATTERN, EMPTY));
	}
}
