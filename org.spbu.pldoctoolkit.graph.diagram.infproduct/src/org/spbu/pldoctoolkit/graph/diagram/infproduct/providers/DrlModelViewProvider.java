package org.spbu.pldoctoolkit.graph.diagram.infproduct.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.edit.parts.DocumentationCoreEditPart;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.edit.parts.InfElemRef2EditPart;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.edit.parts.InfElemRefEditPart;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.edit.parts.InfElemRefGroupEditPart;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.edit.parts.InfElementEditPart;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.edit.parts.InfElementNameEditPart;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.edit.parts.InfProductEditPart;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.edit.parts.InfProductNameEditPart;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.part.DrlModelVisualIDRegistry;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.view.factories.DocumentationCoreViewFactory;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.view.factories.GenericDocumentPartGroupsViewFactory;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.view.factories.InfElemRef2ViewFactory;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.view.factories.InfElemRefGroupViewFactory;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.view.factories.InfElemRefViewFactory;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.view.factories.InfElementNameViewFactory;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.view.factories.InfElementViewFactory;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.view.factories.InfProductNameViewFactory;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.view.factories.InfProductViewFactory;

/**
 * @generated
 */
public class DrlModelViewProvider extends AbstractViewProvider {

	/**
	 * @generated
	 */
	protected Class getDiagramViewClass(IAdaptable semanticAdapter,
			String diagramKind) {
		EObject semanticElement = getSemanticElement(semanticAdapter);
		if (DocumentationCoreEditPart.MODEL_ID.equals(diagramKind)
				&& DrlModelVisualIDRegistry.getDiagramVisualID(semanticElement) != -1) {
			return DocumentationCoreViewFactory.class;
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected Class getNodeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		if (containerView == null) {
			return null;
		}
		IElementType elementType = getSemanticElementType(semanticAdapter);
		if (elementType != null
				&& !DrlModelElementTypes.isKnownElementType(elementType)) {
			return null;
		}
		EClass semanticType = getSemanticEClass(semanticAdapter);
		EObject semanticElement = getSemanticElement(semanticAdapter);
		int nodeVID = DrlModelVisualIDRegistry.getNodeVisualID(containerView,
				semanticElement, semanticType, semanticHint);
		switch (nodeVID) {
		case InfElementEditPart.VISUAL_ID:
			return InfElementViewFactory.class;
		case InfElementNameEditPart.VISUAL_ID:
			return InfElementNameViewFactory.class;
		case InfProductEditPart.VISUAL_ID:
			return InfProductViewFactory.class;
		case InfProductNameEditPart.VISUAL_ID:
			return InfProductNameViewFactory.class;
		case InfElemRefGroupEditPart.VISUAL_ID:
			return InfElemRefGroupViewFactory.class;
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected Class getEdgeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		IElementType elementType = getSemanticElementType(semanticAdapter);
		if (elementType != null
				&& !DrlModelElementTypes.isKnownElementType(elementType)) {
			return null;
		}
		if (DrlModelElementTypes.GenericDocumentPartGroups_3002
				.equals(elementType)) {
			return GenericDocumentPartGroupsViewFactory.class;
		}
		EClass semanticType = getSemanticEClass(semanticAdapter);
		if (semanticType == null) {
			return null;
		}
		EObject semanticElement = getSemanticElement(semanticAdapter);
		int linkVID = DrlModelVisualIDRegistry.getLinkWithClassVisualID(
				semanticElement, semanticType);
		switch (linkVID) {
		case InfElemRefEditPart.VISUAL_ID:
			return InfElemRefViewFactory.class;
		case InfElemRef2EditPart.VISUAL_ID:
			return InfElemRef2ViewFactory.class;
		}
		return getUnrecognizedConnectorViewClass(semanticAdapter,
				containerView, semanticHint);
	}

	/**
	 * @generated
	 */
	private IElementType getSemanticElementType(IAdaptable semanticAdapter) {
		if (semanticAdapter == null) {
			return null;
		}
		return (IElementType) semanticAdapter.getAdapter(IElementType.class);
	}

	/**
	 * @generated
	 */
	private Class getUnrecognizedConnectorViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		// Handle unrecognized child node classes here
		return null;
	}

}
