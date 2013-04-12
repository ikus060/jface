/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.resource;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;

/**
 * A <code>OverlayImageDescriptor</code> is an image descriptor that can be used
 * to overlay decoration images on to 6 positions of a base image.
 * <p>
 * The 6 position are define using {@link SWT#LEFT}, {@link SWT#CENTER},
 * {@link SWT#RIGHT}, {@link SWT#END}, {@link SWT#HOME}
 * 
 * @since 3.3
 * @see IDecoration
 */
public class OverlayImageDescriptor extends CompositeImageDescriptor {
	/**
	 * The base image.
	 */
	private ImageDescriptor baseImage;
	/**
	 * The overlay image
	 */
	private ImageDescriptor overlayImage;
	/**
	 * The horizontal alignment.
	 */
	private int horizontalAlignment;
	/**
	 * The vertical alignment.
	 */
	private int verticalAlignment;
	/**
	 * Size of the base image.
	 */
	private Point size;

	/**
	 * 
	 * @param baseImage
	 * @param overlayImage
	 * @param horizontalAlignment
	 *            how the overlay image will be positioned horizontally within
	 *            the base image, one of: SWT.BEGINNING (or SWT.LEFT),
	 *            SWT.CENTER, SWT.END (or SWT.RIGHT), or SWT.FILL
	 * @param verticalAlignment
	 *            how the overlay image will be positioned vertically within the
	 *            base image, one of: SWT.BEGINNING (or SWT.TOP), SWT.CENTER,
	 *            SWT.END (or SWT.BOTTOM), or SWT.FILL
	 */
	public OverlayImageDescriptor(ImageDescriptor baseImage,
			ImageDescriptor overlayImage, int horizontalAlignment,
			int verticalAlignment) {
		this.baseImage = baseImage;
		this.overlayImage = overlayImage;
		this.size = new Point(this.baseImage.getImageData().height,
				this.baseImage.getImageData().width);
		this.horizontalAlignment = horizontalAlignment;
		this.verticalAlignment = verticalAlignment;

	}

	@Override
	protected void drawCompositeImage(int width, int height) {

		// Draw the base image
		drawImage(this.baseImage.getImageData(), 0, 0);

		// Draw the overlay image
		Point p = computeOverlayPosition(this.overlayImage,
				this.horizontalAlignment, this.verticalAlignment);
		drawImage(this.overlayImage.getImageData(), p.x, p.y);

	}

	protected Point computeOverlayPosition(ImageDescriptor overlay, int h, int v) {
		int x, y = 0;
		switch (h) {
		case SWT.CENTER:
			x = (this.size.x - overlay.getImageData().width) / 2;
			break;
		case SWT.END:
		case SWT.RIGHT:
			x = this.size.x - overlay.getImageData().width;
			break;
		case SWT.BEGINNING:
		case SWT.LEFT:
		default:
			x = 0;
			break;
		}
		switch (v) {
		case SWT.CENTER:
			y = (this.size.y - overlay.getImageData().height) / 2;
			break;
		case SWT.END:
		case SWT.BOTTOM:
			y = this.size.y - overlay.getImageData().height;
			break;
		case SWT.BEGINNING:
		case SWT.TOP:
		default:
			y = 0;
			break;
		}

		return new Point(x, y);
	}

	@Override
	protected Point getSize() {
		return this.size;
	}

}
