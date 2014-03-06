package com.ecs.android.sample.oauth2;

import java.security.KeyManagementException;  
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;  
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;  
import java.security.cert.X509Certificate;  
  
import javax.net.ssl.SSLContext;  
import javax.net.ssl.TrustManager;  
import javax.net.ssl.X509TrustManager;  
  

import org.apache.http.client.HttpClient;  
import org.apache.http.conn.ClientConnectionManager;  
import org.apache.http.conn.scheme.Scheme;  
import org.apache.http.conn.scheme.SchemeRegistry;  
import org.apache.http.conn.ssl.SSLSocketFactory;  
import org.apache.http.impl.client.DefaultHttpClient;  

/**
 * This class will get https client.
 * @author Craig Zhang
 *
 */
public class HttpsClient {
	private static X509TrustManager tm = new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] xcs, String string)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] xcs, String string)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};
	public static HttpClient getInstance(int port) throws KeyManagementException,
	NoSuchAlgorithmException {
		try {
		HttpClient client = new DefaultHttpClient();
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory ssf;
		
			ssf = new MySSLSocketFactory(ctx);
		
		ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		ClientConnectionManager ccm = client.getConnectionManager();
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", ssf, port));
		client = new DefaultHttpClient(ccm, client.getParams());
		return client;
		} 
		catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
}
}
