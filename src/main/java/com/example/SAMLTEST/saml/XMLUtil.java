package com.example.SAMLTEST.saml;

import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.saml.saml2.core.Response;
import org.w3c.dom.Element;

public class XMLUtil {

  public static String toXMLString(Response object) throws TransformerException, MarshallingException {
    Element marshall = XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(Response.DEFAULT_ELEMENT_NAME).marshall(object);
    DOMSource domSource = new DOMSource(marshall);
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    StreamResult result = new StreamResult(new StringWriter());
    transformer.transform(domSource, result);
    return result.getWriter().toString();
  }

}
