package com.example.SAMLTEST.saml;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.xml.security.c14n.Canonicalizer;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.saml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.security.x509.BasicX509Credential;
import org.opensaml.xmlsec.signature.KeyInfo;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.X509Data;
import org.opensaml.xmlsec.signature.impl.KeyInfoBuilder;
import org.opensaml.xmlsec.signature.impl.SignatureBuilder;
import org.opensaml.xmlsec.signature.impl.X509CertificateBuilder;
import org.opensaml.xmlsec.signature.impl.X509DataBuilder;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.opensaml.xmlsec.signature.support.SignatureException;
import org.springframework.stereotype.Service;

@Service
public class SignatureService {

  public Response signResponse(Response response, String certPath, String privKeyPath) throws CertificateException, IOException, InvalidKeySpecException, NoSuchAlgorithmException, MarshallingException, SignatureException, InitializationException {
    InitializationService.initialize();
    CertificateFactory fact = CertificateFactory.getInstance("X.509");
    FileInputStream is = new FileInputStream (certPath);
    X509Certificate cer = (X509Certificate) fact.generateCertificate(is);
    BasicX509Credential idpCredential = new BasicX509Credential(cer, loadPrivateKeyFromFile(privKeyPath));

    Signature signature = new SignatureBuilder().buildObject();
    signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
    signature.setSigningCredential(idpCredential);
    KeyInfo keyInfo = new KeyInfoBuilder().buildObject();
    X509Data x509Data = new X509DataBuilder().buildObject();
    org.opensaml.xmlsec.signature.X509Certificate x509Certificate = new X509CertificateBuilder().buildObject();
    x509Certificate.setValue(String.valueOf(org.apache.xml.security.utils.Base64.encode(idpCredential.getEntityCertificate().getEncoded())));
    x509Data.getX509Certificates().add(x509Certificate);
    keyInfo.getX509Datas().add(x509Data);
    signature.setKeyInfo(keyInfo);
    signature.setCanonicalizationAlgorithm(Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS);
    response.setSignature(signature);
    ResponseMarshaller marshaller = new ResponseMarshaller();
    marshaller.marshall(response);
    org.opensaml.xmlsec.signature.support.Signer.signObject(signature);

    return response;
  }

  private static PrivateKey loadPrivateKeyFromFile(final String filename) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
    String privateKeyPEM = new String(Files.readAllBytes(Paths.get(filename)), Charset.defaultCharset());
    //Create object from unencrypted private key
    privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
    privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----\n", "");
    byte[] encoded = Base64.decodeBase64(privateKeyPEM);
    PKCS8EncodedKeySpec kspec = new PKCS8EncodedKeySpec(encoded);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PrivateKey unencryptedPrivateKey = kf.generatePrivate(kspec);
    return unencryptedPrivateKey;
  }
}
