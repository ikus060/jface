package com.patrikdufresne.ui;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * A View Part is a visual component within the Main Window. It's used to
 * present and manipulate data.
 * <p>
 * This interface may be implemented directly. For Convenience, a base
 * implementation is defined {@link ViewPart}.
 * </p>
 * <p>
 * The lifecycle of a view part is as follows:
 * </p>
 * <ul>
 * <li>When a part extention is created:</li>
 * <ul>
 * <li>The part is created.</li>
 * <li>A View Site is created.</li>
 * <li> {@link IViewPart#init(IViewSite)} is called.</li>
 * </ul>
 * <li>When a part become visible, {@link IViewPart#activate(Composite) } is
 * called.</li> </ul>
 * <ul>
 * <li>When a part is closed:</li>
 * <ul>
 * <li>remove part from presentation; part controls are disposed as part of the
 * SWT widget tree</li>
 * <li>call {@link IViewPart#deactivate()}</li>
 * </ul>
 * <li>When a part is release.</li>
 * <ul>
 * <li>call {@link IViewPart#dispose()}</li>
 * </ul>
 * </ul>
 * 
 * @author Patrik Dufresne
 * 
 */
public interface IViewPart {

	/**
	 * Active the view part. At this point, the view part was initialized with a
	 * view site.
	 * <p>
	 * For implementors this is a multi-step process:
	 * <ol>
	 * <li>Create one or more controls within the given parent.</li>
	 * <li>Set the parent layout as needed.</li>
	 * <li>Register actions with the site's toolbar.</li>
	 * </ol>
	 * 
	 * @param parent
	 *            the parent control
	 */
	void activate(Composite parent);

	/**
	 * Release allocated resource when the part was activate.
	 * <p>
	 * At this point the part controls have been disposed as part of an SWT
	 * composite.
	 * 
	 */
	void deactivate();

	/**
	 * Release all resource.
	 * <p>
	 * This is the last method called. At this point the view part as been
	 * deactivated (if it was ever activate).
	 * <p>
	 * Within this method a part may release any resources, fonts, images, etc.
	 * held by this part.
	 */
	void dispose();

	/**
	 * Returns the part id.
	 * 
	 * @return the id
	 */
	String getId();

	/**
	 * Returns the site for this view.
	 * 
	 * @return the view site; this value may be null if the view has not yet
	 *         been initialized
	 */
	IViewSite getSite();

	/**
	 * Returns the title of this part.
	 * 
	 * @return the part title
	 */
	String getTitle();

	/**
	 * Returns the title image of this part. The title image is usually used to
	 * populate the title bar of this part's visual container. Since this image
	 * is managed by the part itself, callers must not dispose the returned
	 * image.
	 * 
	 * @return the title image
	 */
	Image getTitleImage();

	/**
	 * Returns the title tool tip text of this workbench part. An empty string
	 * result indicates no tool tip. The tool tip text is used to populate the
	 * title bar of this part's visual container.
	 * 
	 * @return the part title tool tip
	 */
	String getTitleToolTip();

	/**
	 * Initializes this view with the given view site.
	 * <p>
	 * This method is automatically called shortly after the part is
	 * instantiated. It marks the start of the views's lifecycle.
	 * 
	 * @param site
	 *            the view site
	 */
	void init(IViewSite site);

}
