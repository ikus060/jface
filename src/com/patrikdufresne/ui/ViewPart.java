package com.patrikdufresne.ui;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Image;

/**
 * This class is an incomplete implementation of the IViewPart interface that
 * provide the management and storage of the view site. It's also provide
 * default value for the image descriptor and the title tool tip.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class ViewPart extends EventManager implements IViewPart {
	/**
	 * Title property.
	 */
	public static final String TITLE = "title"; //$NON-NLS-1$

	/**
	 * Title image property.
	 */
	public static final String TITLE_IMAGE = "titleImage"; //$NON-NLS-1$
	/**
	 * Title tooltip.
	 */
	public static final String TITLE_TOOLTIP = "titleToolTip"; //$NON-NLS-1$
	/**
	 * The view id.
	 */
	private String id;
	/**
	 * The view site for this part.
	 */
	private IViewSite site;
	/**
	 * The view's title.
	 */
	private String title;
	/**
	 * The view's image.
	 */
	private Image titleImage;
	/**
	 * The view's tooltip.
	 */
	private String toolTip;

	/**
	 * Create a new view-part.
	 * 
	 * @param id
	 *            the part id
	 */
	public ViewPart(String id) {
		this.id = id;
	}

	/**
	 * Add a property change listener to this view part.
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(final IPropertyChangeListener listener) {
		addListenerObject(listener);
	}

	/**
	 * View part implementation of deactivate.
	 * <p>
	 * Release the contribution items from the toolbar's view site.
	 */
	@Override
	public void deactivate() {
		// Clear the content of the roolbar
		getSite().getToolBarManager().removeAll();
	}

	/**
	 * View part implementation of dispose.
	 * <p>
	 * <ul>
	 * <li>Release view site.</li>
	 * <li>Release title image.</li>
	 * <li>Clear listeners.</li>
	 * </ul>
	 */
	@Override
	public void dispose() {
		this.id = null;
		if (this.site != null) {
			this.site.dispose();
			this.site = null;
		}
		this.title = null;
		if (this.titleImage != null) {
			this.titleImage.dispose();
			this.titleImage = null;
		}
		this.toolTip = null;
		clearListeners();
	}

	/**
	 * Notifies any property change listeners that a property has changed. Only
	 * listeners registered at the time this method is called are notified.
	 * 
	 * @param event
	 *            the property change event
	 * 
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	protected final void firePropertyChange(final PropertyChangeEvent event) {
		final Object[] list = getListeners();
		for (int i = 0; i < list.length; ++i) {
			((IPropertyChangeListener) list[i]).propertyChange(event);
		}
	}

	/**
	 * Notifies any property change listeners that a property has changed. Only
	 * listeners registered at the time this method is called are notified. This
	 * method avoids creating an event object if there are no listeners
	 * registered, but calls
	 * <code>firePropertyChange(PropertyChangeEvent)</code> if there are.
	 * 
	 * @param propertyName
	 *            the name of the property that has changed
	 * @param oldValue
	 *            the old value of the property, or <code>null</code> if none
	 * @param newValue
	 *            the new value of the property, or <code>null</code> if none
	 * 
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	protected final void firePropertyChange(final String propertyName,
			final Object oldValue, final Object newValue) {
		if (isListenerAttached()) {
			firePropertyChange(new PropertyChangeEvent(this, propertyName,
					oldValue, newValue));
		}
	}

	/**
	 * The part id.
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/**
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public IViewSite getSite() {
		return this.site;
	}

	/**
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public String getTitle() {
		return this.title != null ? this.title : ""; //$NON-NLS-1$
	}

	/**
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public Image getTitleImage() {
		return this.titleImage;
	}

	/**
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public String getTitleToolTip() {
		return this.toolTip != null ? this.toolTip : ""; //$NON-NLS-1$
	}

	/**
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public void init(IViewSite newsite) {
		this.site = newsite;
	}

	/**
	 * Remove the property change listener from this view part.
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(
			final IPropertyChangeListener listener) {
		removeListenerObject(listener);
	}

	/**
	 * Sets or clears the view's title. Clients should call this method instead
	 * of overriding the getTitle.
	 * 
	 * @param titleImage
	 *            the title, or <code>null</code> to clear
	 */
	public void setTitle(String title) {
		firePropertyChange(TITLE, this.title, this.title = title);
	}

	/**
	 * Sets or clears the view's title image. Clients should call this method
	 * instead of overriding the getTitleImage.
	 * 
	 * @param titleImage
	 *            the title image, or <code>null</code> to clear
	 */
	public void setTitleImage(Image titleImage) {
		firePropertyChange(TITLE_IMAGE, this.titleImage,
				this.titleImage = titleImage);
	}

	/**
	 * Sets or clears the view's toolTip. Clients should call this method
	 * instead of overriding the getTitle.
	 * 
	 * @param toolTip
	 *            the new tool tip text, or <code>null</code> to clear
	 */
	public void setTitleToolTip(String toolTip) {
		firePropertyChange(TITLE_TOOLTIP, this.toolTip, this.toolTip = toolTip);
	}

}
