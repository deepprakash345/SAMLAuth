package com.example.SAMLTEST.controller;

import com.example.SAMLTEST.saml.SAMLService;
import com.example.SAMLTEST.saml.XMLUtil;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.inject.Inject;
import javax.xml.transform.TransformerException;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.xmlsec.signature.support.SignatureException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.opensaml.saml.saml2.core.Response;

@RestController
@RequestMapping("/saml")
public class SAMLController {
  @Inject
  private SAMLService SAMLService;

  @RequestMapping(value = "/checkABC", method = RequestMethod.GET)
  public String getSAMLRESPONSE()
      throws CertificateException, InitializationException, IOException, MarshallingException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, TransformerException {
    Response resp= SAMLService.createResponse();
    String respo= XMLUtil.toXMLString(resp);
    String base64Encoded=Base64.getEncoder().encodeToString(respo.getBytes());
    System.out.println("XML=\n\n"+respo);
    System.out.println("BASE64\n\n"+base64Encoded);
    return base64Encoded;
  }

}
