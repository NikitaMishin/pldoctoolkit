package plweb.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.View;

import plweb.PlwebPackage;
import plweb.diagram.edit.parts.AreaTitleEditPart;
import plweb.diagram.edit.parts.NodeTitleEditPart;
import plweb.diagram.edit.parts.PageTitleEditPart;
import plweb.diagram.parsers.MessageFormatParser;
import plweb.diagram.part.PlwebVisualIDRegistry;

/**
 * @generated
 */
public class PlwebParserProvider extends AbstractProvider implements
		IParserProvider {

	/**
	 * @generated
	 */
	private IParser areaTitle_5001Parser;

	/**
	 * @generated
	 */
	private IParser getAreaTitle_5001Parser() {
		if (areaTitle_5001Parser == null) {
			EAttribute[] features = new EAttribute[] { PlwebPackage.eINSTANCE
					.getElement_Title() };
			MessageFormatParser parser = new MessageFormatParser(features);
			areaTitle_5001Parser = parser;
		}
		return areaTitle_5001Parser;
	}

	/**
	 * @generated
	 */
	private IParser pageTitle_5002Parser;

	/**
	 * @generated
	 */
	private IParser getPageTitle_5002Parser() {
		if (pageTitle_5002Parser == null) {
			EAttribute[] features = new EAttribute[] { PlwebPackage.eINSTANCE
					.getElement_Title() };
			MessageFormatParser parser = new MessageFormatParser(features);
			pageTitle_5002Parser = parser;
		}
		return pageTitle_5002Parser;
	}

	/**
	 * @generated
	 */
	private IParser nodeTitle_5003Parser;

	/**
	 * @generated
	 */
	private IParser getNodeTitle_5003Parser() {
		if (nodeTitle_5003Parser == null) {
			EAttribute[] features = new EAttribute[] { PlwebPackage.eINSTANCE
					.getElement_Title() };
			MessageFormatParser parser = new MessageFormatParser(features);
			nodeTitle_5003Parser = parser;
		}
		return nodeTitle_5003Parser;
	}

	/**
	 * @generated
	 */
	protected IParser getParser(int visualID) {
		switch (visualID) {
		case AreaTitleEditPart.VISUAL_ID:
			return getAreaTitle_5001Parser();
		case PageTitleEditPart.VISUAL_ID:
			return getPageTitle_5002Parser();
		case NodeTitleEditPart.VISUAL_ID:
			return getNodeTitle_5003Parser();
		}
		return null;
	}

	/**
	 * Utility method that consults ParserService
	 * @generated
	 */
	public static IParser getParser(IElementType type, EObject object,
			String parserHint) {
		return ParserService.getInstance().getParser(
				new HintAdapter(type, object, parserHint));
	}

	/**
	 * @generated
	 */
	public IParser getParser(IAdaptable hint) {
		String vid = (String) hint.getAdapter(String.class);
		if (vid != null) {
			return getParser(PlwebVisualIDRegistry.getVisualID(vid));
		}
		View view = (View) hint.getAdapter(View.class);
		if (view != null) {
			return getParser(PlwebVisualIDRegistry.getVisualID(view));
		}
		return null;
	}

	/**
	 * @generated
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GetParserOperation) {
			IAdaptable hint = ((GetParserOperation) operation).getHint();
			if (PlwebElementTypes.getElement(hint) == null) {
				return false;
			}
			return getParser(hint) != null;
		}
		return false;
	}

	/**
	 * @generated
	 */
	private static class HintAdapter extends ParserHintAdapter {

		/**
		 * @generated
		 */
		private final IElementType elementType;

		/**
		 * @generated
		 */
		public HintAdapter(IElementType type, EObject object, String parserHint) {
			super(object, parserHint);
			assert type != null;
			elementType = type;
		}

		/**
		 * @generated
		 */
		public Object getAdapter(Class adapter) {
			if (IElementType.class.equals(adapter)) {
				return elementType;
			}
			return super.getAdapter(adapter);
		}
	}

}
