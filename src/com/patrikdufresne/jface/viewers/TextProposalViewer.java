package com.patrikdufresne.jface.viewers;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

// TODO Delete this viewer.
public class TextProposalViewer extends ContentViewer {

	private class ContentProposal implements IContentProposal {

		private Object element;

		public ContentProposal(Object element) {
			Assert.isNotNull(element);
			this.element = element;
		}

		@Override
		public String getContent() {
			return getLabelProviderText(this.element);
		}

		@Override
		public int getCursorPosition() {
			return getLabelProviderText(this.element).length();
		}

		@Override
		public String getDescription() {
			return null;
		}

		Object getElement() {
			return this.element;
		}

		@Override
		public String getLabel() {
			return getLabelProviderText(this.element);
		}
	}

	/**
	 * Asserts that the given array of elements is itself non- <code>null</code>
	 * and contains no <code>null</code> elements.
	 * 
	 * @param elements
	 *            the array to check
	 */
	protected static void assertElementsNotNull(Object[] elements) {
		Assert.isNotNull(elements);
		for (int i = 0, n = elements.length; i < n; ++i) {
			Assert.isNotNull(elements[i]);
		}
	}

	private ContentProposalAdapter contentProposal;

	private IContentProposalListener contentProviderListener = new IContentProposalListener() {
		@Override
		public void proposalAccepted(IContentProposal proposal) {
			ContentProposal elementProposal = (ContentProposal) proposal;
			updateSelection(new StructuredSelection(
					elementProposal.getElement()));
		}
	};

	private IContentProposalProvider contentProviderProposal = new IContentProposalProvider() {
		@Override
		public IContentProposal[] getProposals(String contents, int position) {
			return TextProposalViewer.this.getProposals(contents, position);
		}
	};

	private Listener listener = new Listener() {
		@Override
		public void handleEvent(Event event) {
			switch (event.type) {
			case SWT.Modify:
				updateSelection(new StructuredSelection());
			}
		}
	};

	private Object selection;

	private ViewerComparator sorter;

	private Text text;

	/**
	 * Returns the filtered array of children of the given element. The
	 * resulting array must not be modified, as it may come directly from the
	 * model's internal state.
	 * 
	 * @param parent
	 *            the parent element
	 * @return a filtered array of child elements
	 */
	/*
	 * protected Object[] getFilteredChildren(Object parent) { Object[] result =
	 * getRawChildren(parent); if (filters != null) { for (Iterator iter =
	 * filters.iterator(); iter.hasNext();) { ViewerFilter f = (ViewerFilter)
	 * iter.next(); result = f.filter(this, parent, result); } } return result;
	 * }
	 */

	public TextProposalViewer(Composite parent) {
		this(parent, SWT.BORDER);
	}

	public TextProposalViewer(Composite parent, int style) {
		this(new Text(parent, style));
	}

	/**
	 * Determines whether a change to the given property of the given element
	 * would require refiltering and/or resorting.
	 * <p>
	 * This method is internal to the framework; subclassers should not call
	 * this method.
	 * </p>
	 * 
	 * @param object
	 *            the element
	 * @param property
	 *            the property
	 * @return <code>true</code> if refiltering is required, and
	 *         <code>false</code> otherwise
	 */
	/*
	 * protected boolean needsRefilter(Object element, String property) { if
	 * (sorter != null && sorter.isSorterProperty(element, property)) { return
	 * true; }
	 * 
	 * if (filters != null) { for (int i = 0, n = filters.size(); i < n; ++i) {
	 * ViewerFilter filter = (ViewerFilter) filters.get(i); if
	 * (filter.isFilterProperty(element, property)) { return true; } } } return
	 * false; }
	 */

	public TextProposalViewer(Text text) {
		this.text = text;
		hookControl(text);
	}

	/**
	 * Return this viewer's comparator used to sort elements.
	 * 
	 * @return a viewer comparator, or <code>null</code> if none
	 * 
	 */
	public ViewerComparator getComparator() {
		return sorter;
	}

	public ContentProposalAdapter getContentProposalAdapter() {
		return this.contentProposal;
	}

	@Override
	public Text getControl() {
		return this.text;
	}

	/**
	 * Returns the filtered array of children of the given element. The
	 * resulting array must not be modified, as it may come directly from the
	 * model's internal state.
	 * 
	 * @param parent
	 *            the parent element
	 * @return a filtered array of child elements
	 */
	protected Object[] getFilteredChildren(Object parent, ViewerFilter filter) {
		Object[] result = getRawChildren(parent);
		if (filter != null) {
			Object[] filteredResult = filter.filter(this, parent, result);
			result = filteredResult;
		}
		return result;
	}

	/**
	 * Returns whether this viewer has any filters.
	 * 
	 * @return boolean
	 */
	/*
	 * protected boolean hasFilters() { return filters != null && filters.size()
	 * > 0; }
	 */

	protected Image getLabelProviderImage(Object element) {
		return ((ILabelProvider) getLabelProvider()).getImage(element);
	}

	protected String getLabelProviderText(Object element) {
		return ((ILabelProvider) getLabelProvider()).getText(element);
	}

	protected IContentProposal[] getProposals(String contents, int position) {
		if (!(getLabelProvider() instanceof ILabelProvider)) {
			return new IContentProposal[0];
		}
		// Create a regex pattern for filtering
		String[] strings = contents.split(" "); //$NON-NLS-1$
		final Pattern patterns[] = new Pattern[strings.length];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = normalizeString(strings[i]);
			patterns[i] = Pattern.compile(
					".*" + strings[i] + ".*", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// Create the filter
		ViewerFilter filter = new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				if (patterns.length == 0) {
					return true;
				}
				String string = getLabelProviderText(element);
				string = normalizeString(string);
				int i = 0;
				while (i < patterns.length
						&& patterns[i].matcher(string).matches()) {
					i++;
				}
				if (i < patterns.length)
					return false;
				return true;
			}
		};

		// Retrieve a list of elements from the filtered content provider
		Object[] elements = getSortedChildren(getInput(), filter);
		int count = Math.min(50, elements.length);
		IContentProposal[] proposals = new IContentProposal[count];
		for (int i = 0; i < count; i++) {
			proposals[i] = makeContentProposal(elements[i]);
		}

		return proposals;
	}

	/**
	 * Returns the children of the given parent without sorting and filtering
	 * them. The resulting array must not be modified, as it may come directly
	 * from the model's internal state.
	 * <p>
	 * Returns an empty array if the given parent is <code>null</code>.
	 * </p>
	 * 
	 * @param parent
	 *            the parent element
	 * @return the child elements
	 */
	protected Object[] getRawChildren(Object parent) {
		Object[] result = null;
		if (parent != null) {
			IStructuredContentProvider cp = (IStructuredContentProvider) getContentProvider();
			if (cp != null) {
				result = cp.getElements(parent);
				assertElementsNotNull(result);
			}
		}
		return (result != null) ? result : new Object[0];
	}

	@Override
	public ISelection getSelection() {
		if (this.selection == null) {
			return new StructuredSelection();
		}
		return new StructuredSelection(this.selection);
	}

	/**
	 * Returns the sorted and filtered set of children of the given element. The
	 * resulting array must not be modified, as it may come directly from the
	 * model's internal state.
	 * 
	 * @param parent
	 *            the parent element
	 * @return a sorted and filtered array of child elements
	 */
	protected Object[] getSortedChildren(Object parent, ViewerFilter filter) {
		Object[] result = getFilteredChildren(parent, filter);
		if (this.sorter != null) {
			// be sure we're not modifying the original array from the model
			result = result.clone();
			this.sorter.sort(this, result);
		}
		return result;
	}

	public Text getText() {
		return this.text;
	}

	@Override
	protected void hookControl(Control control) {
		super.hookControl(control);

		// Add Content proposal
		this.contentProposal = new ContentProposalAdapter(getControl(),
				new TextContentAdapter(), this.contentProviderProposal, null,
				null);
		this.contentProposal
				.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		this.contentProposal
				.addContentProposalListener(this.contentProviderListener);

		// Add listener
		control.addListener(SWT.Modify, this.listener);

	}

	protected IContentProposal makeContentProposal(final Object element) {
		return new ContentProposal(element);
	}

	/**
	 * Remove weird character.
	 * 
	 * @param string
	 *            the input string
	 * @return the string without weird character
	 */
	protected String normalizeString(String string) {
		String temp = Normalizer.normalize(string, Normalizer.Form.NFD);
		return temp.replaceAll("[^\\p{ASCII}]", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void refresh() {
		// TODO
	}

	/**
	 * Sets this viewer's comparator to be used for sorting elements, and
	 * triggers refiltering and resorting of this viewer's element.
	 * <code>null</code> turns sorting off.
	 * 
	 * @param comparator
	 *            a viewer comparator, or <code>null</code> if none
	 * 
	 */
	public void setComparator(ViewerComparator comparator) {
		if (this.sorter != comparator) {
			this.sorter = comparator;
			refresh();
		}
	}

	/**
	 * This implementation sets the selection of the viewer.
	 */
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			if (element != null) {
				Object[] children = getRawChildren(getInput());
				for (Object child : children) {
					if (element.equals(child)) {
						this.text.setText(getLabelProviderText(element));
						updateSelection(new StructuredSelection(element));
						return;
					}
				}
			} else {
				this.text.setText(""); //$NON-NLS-1$
				updateSelection(new StructuredSelection());
			}
		}
	}

	protected void updateSelection(IStructuredSelection selection) {
		if (this.selection == null && selection.getFirstElement() == null) {
			return; // Nothing to change
		}
		this.selection = selection.getFirstElement();
		fireSelectionChanged(new SelectionChangedEvent(this, selection));
	}

}
