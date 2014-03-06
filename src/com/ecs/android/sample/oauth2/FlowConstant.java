package com.ecs.android.sample.oauth2;

/**
 * This is constant class.
 * @author Craig Zhang
 *
 */
public interface FlowConstant {
	String AUTHORIZATION_PAGE_TITLE = "Authorization Code Flow";
	String IMPLICIT_PAGE_TITLE = "Implicit Flow";
	String CLIENT_CREDENTIAL_PAGE_TITLE = "Client Credentials Flow";
	String SAML2_PAGE_TITLE = "SAML2 Bearer Assertion Flow";
	String SAML2_INIT_TITLE = "Set up SAML2 env";
	
	String DEFAULT_ORG_ID = "123456789";
	
	String CODE = "code";
	String ERROR = "error";
	String ERROR_DESCRIPTION = "error_description";
	String LOG = "log";
	
	String BASIC = "Basic ";
	
	String samlRequestTemplate = "<samlp:AuthnRequest  xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" ID=\"%requestID%\" Version=\"2.0\" IssueInstant=\"%timestamp%\"><saml:Issuer xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\">%entityID%</saml:Issuer></samlp:AuthnRequest>";
	
	String ACCESS_TOKEN_URI = "/oauth2/v1/access_token";
}

