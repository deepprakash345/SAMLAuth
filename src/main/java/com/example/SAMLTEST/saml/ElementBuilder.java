package com.example.SAMLTEST.saml;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.opensaml.core.config.InitializationException;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Conditions;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.saml.saml2.core.Status;
import org.opensaml.saml.saml2.core.StatusCode;
import org.opensaml.saml.saml2.core.Subject;
import org.opensaml.saml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml.saml2.core.impl.ConditionsBuilder;
import org.opensaml.saml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml.saml2.core.impl.NameIDBuilder;
import org.opensaml.saml.saml2.core.impl.ResponseBuilder;
import org.opensaml.saml.saml2.core.impl.StatusBuilder;
import org.opensaml.saml.saml2.core.impl.StatusCodeBuilder;
import org.opensaml.saml.saml2.core.impl.SubjectBuilder;
import org.springframework.stereotype.Service;

@Service
public class ElementBuilder {
  public Assertion createAssertion(String email) throws InitializationException {
    NameIDBuilder nameIDBuilder = new NameIDBuilder();
    NameID nameId = nameIDBuilder.buildObject();
    nameId.setValue(email);
    nameId.setFormat(NameID.EMAIL);

    SubjectBuilder subjectBuilder = new SubjectBuilder();
    Subject subject = subjectBuilder.buildObject(Subject.DEFAULT_ELEMENT_NAME);
    subject.setNameID(nameId);

    AssertionBuilder assertionBuilder = new AssertionBuilder();
    Assertion assertion = assertionBuilder.buildObject(Assertion.DEFAULT_ELEMENT_NAME);
    Issuer issuer = new IssuerBuilder().buildObject();
    issuer.setValue("localhost");
    assertion.setIssuer(issuer);
    assertion.setSubject(subject);
    assertion.setIssueInstant(new DateTime());
    assertion.setID("ASSERTION_ID");
    Conditions conditions = new ConditionsBuilder().buildObject();
    conditions.setNotBefore(new DateTime());
    conditions.setNotOnOrAfter(LocalDateTime.now().plusDays(1).toDateTime());
    assertion.setConditions(conditions);
    return assertion;
  }

  public Response createResponse(Assertion assertion) {
    Response resp = new ResponseBuilder().buildObject();
    StatusBuilder statusBuilder = new StatusBuilder();
    Status status = statusBuilder.buildObject();
    StatusCodeBuilder statusCodeBuilder = new StatusCodeBuilder();
    StatusCode statusCode = statusCodeBuilder.buildObject();
    statusCode.setValue(StatusCode.SUCCESS);
    status.setStatusCode(statusCode);
    resp.setStatus(status);
    resp.setID("DUMP_ID");
    resp.setIssueInstant(new DateTime());
    resp.getAssertions().add(assertion);
    return resp;
  }
}
