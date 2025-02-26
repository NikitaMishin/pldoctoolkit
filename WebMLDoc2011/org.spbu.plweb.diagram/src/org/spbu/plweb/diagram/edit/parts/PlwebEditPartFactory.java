package org.spbu.plweb.diagram.edit.parts;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.spbu.plweb.diagram.part.PlwebVisualIDRegistry;

/**
 * @generated
 */
public class PlwebEditPartFactory implements EditPartFactory {

	/**
	 * @generated
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof View) {
			View view = (View) model;
			switch (PlwebVisualIDRegistry.getVisualID(view)) {

			case DiagramRootEditPart.VISUAL_ID:
				return new DiagramRootEditPart(view);

			case RootEditPart.VISUAL_ID:
				return new RootEditPart(view);

			case SiteViewEditPart.VISUAL_ID:
				return new SiteViewEditPart(view);

			case SiteViewTitleEditPart.VISUAL_ID:
				return new SiteViewTitleEditPart(view);

			case AreaEditPart.VISUAL_ID:
				return new AreaEditPart(view);

			case AreaTitleEditPart.VISUAL_ID:
				return new AreaTitleEditPart(view);

			case PageEditPart.VISUAL_ID:
				return new PageEditPart(view);

			case PageTitleEditPart.VISUAL_ID:
				return new PageTitleEditPart(view);

			case GroupEditPart.VISUAL_ID:
				return new GroupEditPart(view);

			case NodeEditPart.VISUAL_ID:
				return new NodeEditPart(view);

			case NodeTitleEditPart.VISUAL_ID:
				return new NodeTitleEditPart(view);

			case DocTopicEditPart.VISUAL_ID:
				return new DocTopicEditPart(view);

			case DocTopicDocTopicName5EditPart.VISUAL_ID:
				return new DocTopicDocTopicName5EditPart(view);

			case DocTopic2EditPart.VISUAL_ID:
				return new DocTopic2EditPart(view);

			case DocTopicDocTopicNameEditPart.VISUAL_ID:
				return new DocTopicDocTopicNameEditPart(view);

			case DocTopic3EditPart.VISUAL_ID:
				return new DocTopic3EditPart(view);

			case DocTopicDocTopicName2EditPart.VISUAL_ID:
				return new DocTopicDocTopicName2EditPart(view);

			case DocTopic4EditPart.VISUAL_ID:
				return new DocTopic4EditPart(view);

			case DocTopicDocTopicName3EditPart.VISUAL_ID:
				return new DocTopicDocTopicName3EditPart(view);

			case DocTopic5EditPart.VISUAL_ID:
				return new DocTopic5EditPart(view);

			case DocTopicDocTopicName4EditPart.VISUAL_ID:
				return new DocTopicDocTopicName4EditPart(view);

			case RootTopicRootCompartmentEditPart.VISUAL_ID:
				return new RootTopicRootCompartmentEditPart(view);

			case SiteViewTopicSiteViewCompartmentEditPart.VISUAL_ID:
				return new SiteViewTopicSiteViewCompartmentEditPart(view);

			case AreaTopicAreaCompartmentEditPart.VISUAL_ID:
				return new AreaTopicAreaCompartmentEditPart(view);

			case PageTopicPageCompartmentEditPart.VISUAL_ID:
				return new PageTopicPageCompartmentEditPart(view);

			case NodeTopicNodeCompartmentEditPart.VISUAL_ID:
				return new NodeTopicNodeCompartmentEditPart(view);

			case SourceRefElementClassEditPart.VISUAL_ID:
				return new SourceRefElementClassEditPart(view);

			}
		}
		return createUnrecognizedEditPart(context, model);
	}

	/**
	 * @generated
	 */
	private EditPart createUnrecognizedEditPart(EditPart context, Object model) {
		// Handle creation of unrecognized child node EditParts here
		return null;
	}

	/**
	 * @generated
	 */
	public static CellEditorLocator getTextCellEditorLocator(
			ITextAwareEditPart source) {
		if (source.getFigure() instanceof WrappingLabel)
			return new TextCellEditorLocator((WrappingLabel) source.getFigure());
		else {
			return new LabelCellEditorLocator((Label) source.getFigure());
		}
	}

	/**
	 * @generated
	 */
	static private class TextCellEditorLocator implements CellEditorLocator {

		/**
		 * @generated
		 */
		private WrappingLabel wrapLabel;

		/**
		 * @generated
		 */
		public TextCellEditorLocator(WrappingLabel wrapLabel) {
			this.wrapLabel = wrapLabel;
		}

		/**
		 * @generated
		 */
		public WrappingLabel getWrapLabel() {
			return wrapLabel;
		}

		/**
		 * @generated
		 */
		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Rectangle rect = getWrapLabel().getTextBounds().getCopy();
			getWrapLabel().translateToAbsolute(rect);
			if (!text.getFont().isDisposed()) {
				if (getWrapLabel().isTextWrapOn()
						&& getWrapLabel().getText().length() > 0) {
					rect.setSize(new Dimension(text.computeSize(rect.width,
							SWT.DEFAULT)));
				} else {
					int avr = FigureUtilities.getFontMetrics(text.getFont())
							.getAverageCharWidth();
					rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT,
							SWT.DEFAULT)).expand(avr * 2, 0));
				}
			}
			if (!rect.equals(new Rectangle(text.getBounds()))) {
				text.setBounds(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}

	/**
	 * @generated
	 */
	private static class LabelCellEditorLocator implements CellEditorLocator {

		/**
		 * @generated
		 */
		private Label label;

		/**
		 * @generated
		 */
		public LabelCellEditorLocator(Label label) {
			this.label = label;
		}

		/**
		 * @generated
		 */
		public Label getLabel() {
			return label;
		}

		/**
		 * @generated
		 */
		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Rectangle rect = getLabel().getTextBounds().getCopy();
			getLabel().translateToAbsolute(rect);
			if (!text.getFont().isDisposed()) {
				int avr = FigureUtilities.getFontMetrics(text.getFont())
						.getAverageCharWidth();
				rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT,
						SWT.DEFAULT)).expand(avr * 2, 0));
			}
			if (!rect.equals(new Rectangle(text.getBounds()))) {
				text.setBounds(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}
}
