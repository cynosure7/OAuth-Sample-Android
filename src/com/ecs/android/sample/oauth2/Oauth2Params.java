package com.ecs.android.sample.oauth2;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;

/**
 * 
 * Enum that encapsulates the various OAuth2 connection parameters for the different providers
 * 
 * We capture the following properties for the demo application
 * 
 * clientId
 * clientSecret
 * scope
 * rederictUri
 * apiUrl
 * tokenServerUrl
 * authorizationServerEncodedUrl
 * accessMethod
 * 
 * @author davydewaele
 *
 */
public enum Oauth2Params {

	CISCO_OAUTH2("C18a6e32ed22e2c1ed6c750527b1f0b1e6c3a6046d110cc6dd0bad96f59c704d2","0e723a4083d65ae55e56f012c7c4149d451254af348fa4138f19d667425b89c0","https://124.cisco.com:9443/idb/oauth2/v1/access_token", "https://124.cisco.com:9443/idb/oauth2/v1/authorize",BearerToken.authorizationHeaderAccessMethod(),"SampleResourceService:read","urn:ietf:wg:oauth:2.0:oob","cisco","http://124.cisco.com/oauth2sampleresource"); 
	
    private String clientId;
	private String clientSecret;
	private String tokenServerUrl;
	private String authorizationServerEncodedUrl;
	private AccessMethod accessMethod;
	private String scope;
	private String rederictUri;
	private String userId;
	private String apiUrl;

	Oauth2Params(String clientId,String clientSecret,String tokenServerUrl,String authorizationServerEncodedUrl,AccessMethod accessMethod,String scope,String rederictUri,String userId,String apiUrl) {
		this.clientId=clientId;
		this.clientSecret=clientSecret;
		this.tokenServerUrl=tokenServerUrl;
		this.authorizationServerEncodedUrl=authorizationServerEncodedUrl;
		this.accessMethod=accessMethod;
		this.scope=scope;
		this.rederictUri=rederictUri;
		this.userId=userId;
		this.apiUrl=apiUrl;
	}
	
	public String getClientId() {
		if (this.clientId==null || this.clientId.length()==0) {
			throw new IllegalArgumentException("Please provide a valid clientId in the Oauth2Params class");
		}
		return clientId;
	}
	public String getClientSecret() {
		if (this.clientSecret==null || this.clientSecret.length()==0) {
			throw new IllegalArgumentException("Please provide a valid clientSecret in the Oauth2Params class");
		}
		return clientSecret;
	}
	
	public String getScope() {
		return scope;
	}
	public String getRederictUri() {
		return rederictUri;
	}
	public String getApiUrl() {
		return apiUrl;
	}
	public String getTokenServerUrl() {
		return tokenServerUrl;
	}

	public String getAuthorizationServerEncodedUrl() {
		return authorizationServerEncodedUrl;
	}
	
	public AccessMethod getAccessMethod() {
		return accessMethod;
	}
	
	public String getUserId() {
		return userId;
	}
}
