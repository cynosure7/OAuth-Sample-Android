package com.ecs.android.sample.oauth2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.ecs.android.sample.oauth2.store.SharedPreferencesCredentialStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class OAuth2Helper {

	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory(); 

	private final CredentialStore credentialStore;
	
	private AuthorizationCodeFlow flow;

	private Oauth2Params oauth2Params;
	
	
	public OAuth2Helper(SharedPreferences sharedPreferences, Oauth2Params oauth2Params) {
		this.credentialStore = new SharedPreferencesCredentialStore(sharedPreferences);
		this.oauth2Params = oauth2Params;
		this.flow = new AuthorizationCodeFlow.Builder(oauth2Params.getAccessMethod() , HTTP_TRANSPORT, JSON_FACTORY, new GenericUrl(oauth2Params.getTokenServerUrl()), new ClientParametersAuthentication(oauth2Params.getClientId(),oauth2Params.getClientSecret()), oauth2Params.getClientId(), oauth2Params.getAuthorizationServerEncodedUrl()).setCredentialStore(this.credentialStore).build();
		
	}

	public OAuth2Helper(SharedPreferences sharedPreferences) {
		this(sharedPreferences,Constants.OAUTH2PARAMS);
	}
	
	public String getAuthorizationUrl() {
		String authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(oauth2Params.getRederictUri()).setScopes(convertScopesToString(oauth2Params.getScope())).build();
		return authorizationUrl;
	}
	
	public void retrieveAndStoreAccessToken(String authorizationCode) throws IOException {
		Log.i(Constants.TAG,"retrieveAndStoreAccessToken for code " + authorizationCode);
		
		HttpClient client = OAuth2Helper.getHttpClientByURL(oauth2Params.getTokenServerUrl());
		
		String parameters = this.getAuthorizationParameters(authorizationCode);
	
		Response res = OAuth2Helper.httpClientPostCall(client, oauth2Params.getTokenServerUrl(), parameters, this.getAuthorization());
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> respMap = new HashMap<String, Object>();
		respMap = objectMapper.readValue(res.getRespMsg(), Map.class);
		//expires_in=3599,
		//token_type=Bearer,
		//refresh_token=30c6712c-fa48-48c4-8e0e-cc7dff686e46,
		//refresh_token_expires_in=7775999,
		//access_token=0277a131-48d4-45bc-bcd5-587de9a6cdcd}
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setAccessToken((String)respMap.get("access_token"));
		tokenResponse.setExpiresInSeconds(((Integer)respMap.get("expires_in")).longValue());
		tokenResponse.setTokenType((String)respMap.get("token_type"));
		tokenResponse.setRefreshToken((String)respMap.get("refresh_token"));
//		tokenResponse.setAccessToken(res.get)
		
//		TokenResponse tokenResponse = flow.newTokenRequest(authorizationCode).setScopes(convertScopesToString(oauth2Params.getScope())).setRedirectUri(oauth2Params.getRederictUri()).execute();
		
//		Log.i(Constants.TAG, "Found tokenResponse :");
//		Log.i(Constants.TAG, "Access Token : " + tokenResponse.getAccessToken());
//		Log.i(Constants.TAG, "Refresh Token : " + tokenResponse.getRefreshToken());
		flow.createAndStoreCredential(tokenResponse, oauth2Params.getUserId());
	}
	
	public static Response httpClientPostCall(HttpClient client, String requestUrl,
			String parameters, String authorization) {
		String repMsg = null;
		try {
			HttpPost post = new HttpPost(requestUrl);
			
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			if (""!=(authorization)) {
				post.setHeader("Authorization", authorization);
				
			}
			StringEntity reqEntity = null;
			if(parameters!=null) {
				reqEntity = new StringEntity(parameters);
				post.setEntity(reqEntity);
			}
			StringBuilder httpLogBuilder = new StringBuilder();
			httpLogBuilder.append("Token Request : \n").append(post.getRequestLine().toString()).append("\n");
			httpLogBuilder.append(getHeaderString(post.getAllHeaders()));
			if(reqEntity!=null){
				httpLogBuilder.append("\n").append(EntityUtils.toString(reqEntity, "UTF_8"));
			}
			org.apache.http.HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			repMsg = EntityUtils.toString(entity, "UTF_8");
			httpLogBuilder.append("\n==========================\nToken Response:\n")
			.append(getHeaderString(response.getAllHeaders())).append("\n"+repMsg);
			return new Response(repMsg, httpLogBuilder.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
	}
	
	public static Response httpClientGetCall(HttpClient client, String requestUrl,
			String parameters, String authorization) {
		String repMsg = null;
		try {
			String targetURL = requestUrl;
			if(parameters!=null){
				targetURL = targetURL+ "?" + parameters;
			}
			HttpGet httpGet = new HttpGet(targetURL);
			httpGet.setHeader("Authorization", authorization);
			
			org.apache.http.HttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			
			StringBuilder httpLogBuilder = new StringBuilder();
			httpLogBuilder.append("Sample Resource Request : \n").append(httpGet.getRequestLine().toString()).append("\n");
			httpLogBuilder.append(getHeaderString(httpGet.getAllHeaders()));
			httpLogBuilder.append("\n");

			repMsg = EntityUtils.toString(entity, "UTF-8");
			httpLogBuilder.append("\n==========================\nSample Resource Response:\n")
			.append(getHeaderString(response.getAllHeaders())).append("\n"+repMsg);
			return new Response(repMsg, httpLogBuilder.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	
	private static String getHeaderString(Header[] allHeaders) {
		StringBuilder sb = new StringBuilder();
		for(Header header : allHeaders){
			sb.append(header.toString()+"\n");
		}
		return sb.toString();
	}
	
	private String getAuthorizationParameters(String code)
			throws UnsupportedEncodingException {
		StringBuffer paramsBuf = new StringBuffer(1024);
		paramsBuf.append("grant_type=authorization_code");
		paramsBuf.append("&code=").append(code);
		paramsBuf.append("&redirect_uri=").append(URLEncoder.encode(oauth2Params.getRederictUri(), "UTF-8"));
		paramsBuf.append("&realm=").append(URLEncoder.encode("oauthtestorg1", "UTF-8"));
		String paramStr = paramsBuf.toString();
		paramsBuf.setLength(0);
		return paramStr;
	}
	
	protected String getAuthorization() {
		StringBuffer buf = new StringBuffer(256);		
		buf.append(oauth2Params.getClientId());
		buf.append(":");
		buf.append(oauth2Params.getClientSecret());
		String base64Str = Base64.encodeToString(buf.toString().getBytes(),Base64.DEFAULT);
		base64Str = base64Str.replaceAll("\r", "");
		base64Str = base64Str.replaceAll("\n", "");
		base64Str = "Basic " + base64Str;
		buf.setLength(0);
		return base64Str;
	}
	
	
	public String executeApiCall() throws IOException {
		Log.i(Constants.TAG,"Executing API call at url " + this.oauth2Params.getApiUrl());
		return this.getSampleResource(this.loadCredential().getAccessToken()).values().toString();
//		return HTTP_TRANSPORT.createRequestFactory(loadCredential()).buildGetRequest(new GenericUrl(this.oauth2Params.getApiUrl())).execute().parseAsString();
	}
	
	public Credential loadCredential() throws IOException {
		return flow.loadCredential(oauth2Params.getUserId());
	}

	public void clearCredentials() throws IOException {
		 flow.getCredentialStore().delete(oauth2Params.getUserId(), null);		
	}
	
	private Collection<String> convertScopesToString(String scopesConcat) {
		String[] scopes = scopesConcat.split(",");
		Collection<String> collection = new ArrayList<String>();
		Collections.addAll(collection, scopes);
		return collection;
	}

	/**
	 * Call sample resource service to get resource. 
	 * @param request
	 * @return
	 */
	protected Map getSampleResource(String accessToken) {
		Map<String, String> respMap = new HashMap<String, String>();
		if (""==(accessToken)) {
			respMap.put(FlowConstant.ERROR, "invalid_request");
			respMap.put(FlowConstant.ERROR_DESCRIPTION, "Missing parameters: access token");
			return respMap;
		}
		String scope = "SampleResourceService:read";
		if (""==(scope)) {
			respMap.put(FlowConstant.ERROR, "invalid_request");
			respMap.put(FlowConstant.ERROR_DESCRIPTION, "Missing parameters: scope");
			return respMap;
		}
		
		try {
			HttpClient client = OAuth2Helper.getHttpClientByURL(oauth2Params.getApiUrl());
			
			
			/*
				Read User: GET  http://localhost:8080/oauth2sampleresource/rest/v1/user/123   
				Write User: POST  http://localhost:8080/oauth2ampleresource/rest/v1/user/
				List User: GET  http://localhost:8080/oauth2sampleresource/rest/v1/user/
			 */		
			String requestUrl = oauth2Params.getApiUrl() + "/rest/v1/user/";
			if("SampleResourceService:read".equalsIgnoreCase(scope)){
				requestUrl =  requestUrl + "123";
			} 
			Response resp = null;
			String authorization =  "Bearer " + accessToken;
			if("SampleResourceService:write".equalsIgnoreCase(scope)){
				resp = OAuth2Helper.httpClientPostCall(client, requestUrl, null, authorization);
			} else {
				resp = OAuth2Helper.httpClientGetCall(client, requestUrl, null, authorization);				
			}
			
			if (resp == null || ""==(resp.getRespMsg())) {
				respMap.put(FlowConstant.ERROR, "Unknown error");
				respMap.put(FlowConstant.ERROR_DESCRIPTION, "Unknown error");
			} else {
				ObjectMapper objectMapper = new ObjectMapper();
				respMap = objectMapper.readValue(resp.getRespMsg(), Map.class);
			}
			String httpLog = null;
			if(resp != null) {
				httpLog = resp.getHttpLog();
			}
			if(""!=(httpLog)){
				respMap.put(FlowConstant.LOG, httpLog);
			}
		} catch (Exception ex) {
			respMap.put(FlowConstant.ERROR, "Unknown error");
			respMap.put(FlowConstant.ERROR_DESCRIPTION, "Unknown error");
			ex.printStackTrace();
		}		
		return respMap;
	}
	
	public static HttpClient getHttpClientByURL(String urlStr) {
		try {
			URL url = new URL(urlStr);
			if ("HTTPS".equalsIgnoreCase(url.getProtocol())) {
				int port = url.getPort();
				if (port == -1) {
					port = url.getDefaultPort();
				}
				return HttpsClient.getInstance(port);
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DefaultHttpClient();
	}
	
}
