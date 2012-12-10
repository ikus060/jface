package com.patrikdufresne.ui;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

//TODO Add comment to this class
public class ColorUtil {
	/**
	 * Constant to access the color registry.
	 */
	private static final String ACTIVE_TAB_END = "ColorUtil.ActiveTabEnd"; //$NON-NLS-1$
	/**
	 * Constant to access the color registry.
	 */
	private static final String ACTIVE_TAB_START = "ColorUtil.ActiveTabStart"; //$NON-NLS-1$
	/**
	 * Constant to access the color registry.
	 */
	private static final String ACTIVE_TAB_TEXT = "ColorUtil.ActiveTabText"; //$NON-NLS-1$

	/**
	 * This unction may be used to adapt the color of a CTabFolder.
	 * 
	 * @param tabFolder
	 */
	public static void adapt(CTabFolder tabFolder) {
		// Use the color registry to store the font.
		if (!JFaceResources.getColorRegistry().hasValueFor(ACTIVE_TAB_START)) {
			JFaceResources.getColorRegistry().put(ACTIVE_TAB_START,
					getActiveTabStartColor());
			JFaceResources.getColorRegistry().put(ACTIVE_TAB_END,
					getActiveTabEndColor());
			JFaceResources.getColorRegistry().put(ACTIVE_TAB_TEXT,
					getActiveTabTextColor());
		}

		tabFolder.setSelectionBackground(new Color[] {
				JFaceResources.getColorRegistry().get(ACTIVE_TAB_START),
				JFaceResources.getColorRegistry().get(ACTIVE_TAB_END) },
				new int[] { 100 }, true);
		tabFolder.setSelectionForeground(JFaceResources.getColorRegistry().get(
				ACTIVE_TAB_TEXT));

	}

	private static int blend(int v1, int v2, int ratio) {
		int b = (ratio * v1 + (100 - ratio) * v2) / 100;
		return Math.min(255, b);
	}

	/**
	 * Blends the two color values according to the provided ratio.
	 * 
	 * @param c1
	 *            first color
	 * @param c2
	 *            second color
	 * @param ratio
	 *            percentage of the first color in the blend (0-100)
	 * @return the RGB value of the blended color
	 * 
	 * @since 3.3
	 */
	private static RGB blend(RGB c1, RGB c2, int ratio) {
		int r = blend(c1.red, c2.red, ratio);
		int g = blend(c1.green, c2.green, ratio);
		int b = blend(c1.blue, c2.blue, ratio);
		return new RGB(r, g, b);
	}

	/**
	 * Return an RGB array of the Tango palette.
	 * 
	 * @return the colors
	 */
	public static RGB[] createTangoPalette() {
		return new RGB[] { new RGB(252, 233, 79), /* Butter 1 */
		new RGB(237, 212, 0), /* Butter 2 */
		new RGB(196, 160, 0), /* Butter 3 */
		new RGB(138, 226, 52), /* Chameleon 1 */
		new RGB(115, 210, 22), /* Chameleon 2 */
		new RGB(78, 154, 6), /* Chameleon 3 */
		new RGB(252, 175, 62), /* Orange 1 */
		new RGB(245, 121, 0), /* Orange 2 */
		new RGB(206, 92, 0), /* Orange 3 */
		new RGB(114, 159, 207), /* Sky Blue 1 */
		new RGB(52, 101, 164), /* Sky Blue 2 */
		new RGB(32, 74, 135), /* Sky Blue 3 */
		new RGB(173, 127, 168), /* Plum 1 */
		new RGB(117, 80, 123), /* Plum 2 */
		new RGB(92, 53, 102), /* Plum 3 */
				// new RGB(233, 185, 110), /* Chocolate 1 */
				// new RGB(193, 125, 17), /* Chocolate 2 */
				// new RGB(143, 89, 2), /* Chocolate 3 */
				new RGB(239, 41, 41), /* Scarlet Red 1 */
				new RGB(204, 0, 0), /* Scarlet Red 2 */
				new RGB(164, 0, 0), /* Scarlet Red 3 */
				new RGB(238, 238, 236), /* Aluminium 1 */
				new RGB(211, 215, 207), /* Aluminium 2 */
				new RGB(186, 189, 182), /* Aluminium 3 */
				new RGB(136, 138, 133), /* Aluminium 4 */
				new RGB(85, 87, 83), /* Aluminium 5 */
				new RGB(46, 52, 54) /* Aluminium 6 */
		};
	}

	private static RGB getActiveTabEndColor() {
		return Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION)
				.getRGB();
	}

	private static RGB getActiveTabStartColor() {
		if (Display.getCurrent().getDepth() < 15)
			return getActiveTabEndColor();

		RGB white = new RGB(255, 255, 255);
		Color c1 = Display.getCurrent()
				.getSystemColor(SWT.COLOR_LIST_SELECTION);
		return blend(white, c1.getRGB(), 75);
	}

	private static RGB getActiveTabTextColor() {
		return Display.getCurrent()
				.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT).getRGB();
	}
}
