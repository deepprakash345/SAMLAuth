package com.example.SAMLTEST.saml;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import javax.inject.Inject;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.xmlsec.signature.support.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SAMLService {

  @Value("${EMAIL_TO_LOGIN}")
  private String emailToLogin;

  @Value("${CERT_FILENAME}")
  private String publicCertificateFilename;

  @Value("${PRIVATE_KEY_FILENAME}")
  private String privateKeyFilename;

  @Value("${CERT_PATH}")
  private String certificateFolderPath;

  @Inject
  private SignatureService signatureService;

  @Inject
  private ElementBuilder samlElementBuilder;

  public Response createResponse() throws InitializationException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException, IOException, MarshallingException, SignatureException {
    Assertion assertion = samlElementBuilder.createAssertion(emailToLogin);
    Response resp = samlElementBuilder.createResponse(assertion);
    return signatureService
        .signResponse(resp, String.format("%s/%s", certificateFolderPath, publicCertificateFilename),
        String.format("%s/%s", certificateFolderPath, privateKeyFilename));
  }
}
