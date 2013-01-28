package com.patrikdufresne.jface.databinding.conversion;

import static org.junit.Assert.*;

import org.junit.Test;

public class AcronymConverterTest {

	@Test
	public void testConvert() {

		AcronymConverter converter = AcronymConverter.getInstance();
		
		assertEquals("JS", converter.convert("Jours de semaine"));
		
		assertEquals("JFS", converter.convert("Jours de fin de semaine"));
		
		assertEquals("JFS", converter.convert("1. Jours de fin de semaine"));
		
		assertEquals("NSDJS", converter.convert("9. Nuit semaine Dim - Jeu - Salubrité"));
		
		assertEquals("JRFS", converter.convert("6. Jours remplacement fin de semaine"));
		
	}

}
