package org.spbu.pldoctoolkit.registry;

import static org.spbu.pldoctoolkit.registry.RegisteredLocation.CORE_CONTEXT;
import static org.spbu.pldoctoolkit.registry.RegisteredLocation.DICTIONARY;
import static org.spbu.pldoctoolkit.registry.RegisteredLocation.PRODUCT;
import static org.spbu.pldoctoolkit.registry.RegisteredLocation.FINAL_INF_PRODUCT;
import static org.spbu.pldoctoolkit.registry.RegisteredLocation.INF_ELEMENT;
import static org.spbu.pldoctoolkit.registry.RegisteredLocation.INF_PRODUCT;
import static org.spbu.pldoctoolkit.registry.RegisteredLocation.PRODUCT_CONTEXT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.saxon.Configuration;
import net.sf.saxon.dom.DocumentBuilderImpl;
import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.om.NodeInfo;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Status;
import org.spbu.pldoctoolkit.PLDocToolkitPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static org.spbu.pldoctoolkit.registry.RegisteredLocation.*;

class ProjectRegistryImpl implements ProjectRegistry {
	private static final String DRLRESOLVE_PREFIX = "drlresolve://";
	
	private static final String DOCUMENTATION_CORE = "DocumentationCore";
	private static final String PRODUCT_DOCUMENTATION = "ProductDocumentation";
	private static final String PRODUCT_LINE = "ProductLine";
	
	private static final String ID_ATTRIBUTE = "id";
	private static final String NAME_ATTRIBUTE = "name";
	private static final String PRODUCTID_ATTRIBUTE = "productid";

	private final Map<String, RegisteredLocation> locationMap = new HashMap<String, RegisteredLocation>();
	private DocumentBuilderImpl documentBuilder;

	private Document getXMLDocument(IFile file) throws ParserConfigurationException, SAXException, IOException {
		final String SAXON_FACTORY_CLASS_NAME = "net.sf.saxon.dom.DocumentBuilderFactoryImpl"; 
		
		if (documentBuilder == null) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(
					SAXON_FACTORY_CLASS_NAME, this.getClass().getClassLoader());
			factory.setNamespaceAware(true);
			documentBuilder = (DocumentBuilderImpl) factory.newDocumentBuilder();
		}
		
		Configuration config = new Configuration();
		config.setLineNumbering(true);
		documentBuilder.setConfiguration(config);
		
		return documentBuilder.parse(file.getLocationURI().toString());
	}

	// === Public API ===
	
	public RegisteredLocation getRegisteredLocation(String uri) {
		if (!uri.startsWith(DRLRESOLVE_PREFIX))
			return null;
		String path = uri.substring(DRLRESOLVE_PREFIX.length());
		String[] elements = path.split("/");
		if (elements.length != 3)
			return null;
		if (CORE_CONTEXT.equals(elements[0]))
			return locationMap.get(path);
		RegisteredLocation loc = locationMap.get(path);
		if (loc != null)
			return loc;
		return locationMap.get(CORE_CONTEXT + "/" + elements[1] + "/" + elements[2]);
	}
	
	public String getContext(IFile file) {
		for (RegisteredLocation loc: locationMap.values()) {
			if (loc.getFile().equals(file))
				return loc.getContext();
		}
		return null;
	}
	
	public List<RegisteredLocation> findForId(String id) {
		// TODO: optimize?
		List<RegisteredLocation> result = new ArrayList<RegisteredLocation>();
		for (RegisteredLocation loc: locationMap.values())
			if (loc.getId().equals(id))
				result.add(loc);
		return result;
	}

	public List<RegisteredLocation> findForType(String type) {
		// TODO: optimize?
		List<RegisteredLocation> result = new ArrayList<RegisteredLocation>();
		for (RegisteredLocation loc: locationMap.values())
			if (loc.getType().equals(type))
				result.add(loc);
		return result;
	}

	public List<RegisteredLocation> findForFile(IFile file) {
		// TODO: optimize?
		List<RegisteredLocation> result = new ArrayList<RegisteredLocation>();
		for (RegisteredLocation loc: locationMap.values())
			if (loc.getFile().equals(file))
				result.add(loc);
		return result;
	}
	
	public List<RegisteredLocation> findAll() {
		return new ArrayList<RegisteredLocation>(locationMap.values());
	}
	
	// === End of public API ===
	
	private void register(String context, Node node, IFile file) {
		final NodeInfo saxonNodeInfo = ((NodeOverNodeInfo) node).getUnderlyingNodeInfo();
		
		Node idAttribute = node.getAttributes().getNamedItem(ID_ATTRIBUTE);
		Node nameAttribute = node.getAttributes().getNamedItem(NAME_ATTRIBUTE);
		if (idAttribute == null)
			return;
		String type = node.getLocalName();
		String id = idAttribute.getNodeValue();
		String name = nameAttribute == null ? id : nameAttribute.getNodeValue();
		int lineNumber = saxonNodeInfo.getLineNumber();
		
		RegisteredLocation loc = new RegisteredLocation(context, type, id, name, file, lineNumber); 
		locationMap.put(context + "/" + type + "/" + id, loc);
	}
	
	void refreshFile(IFile file) throws CoreException {
		IPath location = file.getLocation();
		for (Iterator<RegisteredLocation> it = locationMap.values().iterator(); it.hasNext();) {
			IFile file1 = it.next().getFile();
			if (location.equals(file1.getLocation()))
				it.remove();
		}
		String ext = file.getFileExtension();
		if (ext != null && ext.toLowerCase().equals(PLDocToolkitPlugin.DRL_FILE_EXTENSION)) {
			try {
				Document doc = getXMLDocument(file);
				NodeList rootList = doc.getChildNodes();
				Node rootNode = null;
				for (int i = 0; i < rootList.getLength(); i++) {
					Node node = rootList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						rootNode = node;
						break;
					}
				}
				if (rootNode == null)
					return;
				String nodeName = rootNode.getLocalName();
				if (DOCUMENTATION_CORE.equals(nodeName) || PRODUCT_LINE.equals(nodeName)) {
					NodeList list = rootNode.getChildNodes();
					for (int i = 0; i < list.getLength(); i++) {
						Node child = list.item(i);
						if (child.getNodeType() != Node.ELEMENT_NODE)
							continue;
						String childName = child.getLocalName();
						if (!INF_PRODUCT.equals(childName) && 
							!INF_ELEMENT.equals(childName) && 
							!DICTIONARY.equals(childName)  &&
							!PRODUCT.equals(childName)     &&
							!DIRECTORY.equals(childName)   &&
							!DIRTEPLATE.equals(childName))
							continue;
						register(CORE_CONTEXT, child, file);
					}
				} else if (PRODUCT_DOCUMENTATION.equals(nodeName)) {
					Node idAttribute = rootNode.getAttributes().getNamedItem(PRODUCTID_ATTRIBUTE);
					if (idAttribute == null)
						return;
					String id = idAttribute.getNodeValue();
					NodeList list = rootNode.getChildNodes();
					for (int i = 0; i < list.getLength(); i++) {
						Node child = list.item(i);
						if (child.getNodeType() != Node.ELEMENT_NODE)
							continue;
						String childName = child.getLocalName();
						if (!DICTIONARY.equals(childName) && 
							!DIRECTORY.equals(childName) &&
							!DIRTEPLATE.equals(childName) &&
							!FINAL_INF_PRODUCT.equals(childName))
							continue;
						// Adding a prefix to avoid conflicts in case of CORE.equals(id)
						register(PRODUCT_CONTEXT + id, child, file);
					}
				}
			} catch (ParserConfigurationException e) {
				throw new CoreException(new Status(Status.ERROR, PLDocToolkitPlugin.PLUGIN_ID, 0, "Parser configuration error", e));
			} catch (SAXException e) {
				// ignore
			} catch (IOException e) {
				// ignore
			}
		}
	}
	
	void refreshContainer(IContainer container) throws CoreException {
		IPath location = container.getLocation();
		for (Iterator<RegisteredLocation> it = locationMap.values().iterator(); it.hasNext();) {
			IFile file = it.next().getFile();
			if (location.isPrefixOf(file.getLocation()))
				it.remove();
		}
		container.accept(new IResourceVisitor() {
			public boolean visit(IResource resource) throws CoreException {
				if (resource instanceof IFile)
					refreshFile((IFile) resource);
				return true;
			}
		});
	}
}
