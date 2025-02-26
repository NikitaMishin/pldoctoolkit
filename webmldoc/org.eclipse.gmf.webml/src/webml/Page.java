/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package webml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Page</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link webml.Page#getElement <em>Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see webml.WebmlPackage#getPage()
 * @model
 * @generated
 */
public interface Page extends ContentUnit {
	/**
	 * Returns the value of the '<em><b>Element</b></em>' containment reference list.
	 * The list contents are of type {@link webml.Unit}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element</em>' containment reference list.
	 * @see webml.WebmlPackage#getPage_Element()
	 * @model containment="true"
	 *        annotation="gmf.compartment foo='bar' collapsible='false'"
	 * @generated
	 */
	EList<Unit> getElement();

} // Page
