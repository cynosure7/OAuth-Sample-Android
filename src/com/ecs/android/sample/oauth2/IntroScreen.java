package com.ecs.android.sample.oauth2;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ecs.android.sample.oauth2.store.CredentialSharedPreference;
import com.google.api.client.auth.oauth2.Credential;

public class IntroScreen extends Activity {

	
	private Timer timer = new Timer();
	private Button btnOAuthCisco;
	private SharedPreferences prefs;
	protected int elapsedTime;
	private Button btnApiCisco;
	private Button btnClearCisco;
	private Button btnCodeCisco;
	
	private TextView txtCliendID;
	private TextView txtCliendSecret;

	private TextView txtCode;
	
	private CredentialSharedPreference _appPrefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
	    _appPrefs = new CredentialSharedPreference(getApplicationContext());
		
		btnOAuthCisco = (Button)findViewById(R.id.btn_oauth_cisco);
		btnClearCisco = (Button)findViewById(R.id.btn_clear_cisco);
		btnApiCisco = (Button)findViewById(R.id.btn_api_cisco);
		btnCodeCisco = (Button)findViewById(R.id.btn_oauth_getCode);
		
		this.txtCliendID = (TextView) findViewById(R.id.txt_oauth_clientID);
		this.txtCliendID.setText(Oauth2Params.CISCO_OAUTH2.getClientId());
		
		this.txtCliendSecret = (TextView) findViewById(R.id.txt_oauth_clientSecret);
		this.txtCliendSecret.setText(Oauth2Params.CISCO_OAUTH2.getClientSecret());
		
		this.txtCode = (TextView) findViewById(R.id.txt_oauth_code);
			
		btnOAuthCisco.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startOauthFlow(Oauth2Params.CISCO_OAUTH2);
			}
		});
		
		btnClearCisco.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				clearCredentials(Oauth2Params.CISCO_OAUTH2);
//				getCode(Oauth2Params.CISCO_OAUTH2);
			}

		});
		
		btnApiCisco.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startMainScreen(Oauth2Params.CISCO_OAUTH2);
			}

		});
		
		btnCodeCisco.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getCode(Oauth2Params.CISCO_OAUTH2);
				txtCode.setText(_appPrefs.getSmsBody());
			}
		});
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
    

	/**
	 * Starts the main screen where we show the API results.
	 * 
	 * @param oauth2Params
	 */
	private void startMainScreen(Oauth2Params oauth2Params) {
		Constants.OAUTH2PARAMS = oauth2Params;
		startActivity(new Intent().setClass(this,MainScreen.class));
	}
	
	/**
	 * Starts the activity that takes care of the OAuth2 flow
	 * 
	 * @param oauth2Params
	 */
	private void startOauthFlow(Oauth2Params oauth2Params) {
		Constants.OAUTH2PARAMS = oauth2Params;
		startActivity(new Intent().setClass(this,OAuthAccessTokenActivity.class));
	}
	
	/**
	 * Starts the activity that takes care of the OAuth2 flow
	 * 
	 * @param oauth2Params
	 */
	private void getCode(Oauth2Params oauth2Params) {
		Constants.OAUTH2PARAMS = oauth2Params;
		startActivity(new Intent().setClass(this,OAuthCodeActivity.class));
	}	
	
	/**
	 * Clears our credentials (token and token secret) from the shared preferences.
	 * We also setup the authorizer (without the token).
	 * After this, no more authorized API calls will be possible.
	 * @throws IOException 
	 */
    private void clearCredentials(Oauth2Params oauth2Params)  {
		try {
			new OAuth2Helper(prefs,oauth2Params).clearCredentials();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		startTimer();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		stopTimer();
	}

	private void stopTimer() {
		timer.cancel();
	}

	private String getTokenStatusText(Oauth2Params oauth2Params) throws IOException {
		Credential credential = new OAuth2Helper(this.prefs,oauth2Params).loadCredential();
		String output = null;
		if (credential==null || credential.getAccessToken()==null) {
			output = "No access token found.";
		} else if (credential.getExpiresInSeconds()!=null && 
				credential.getExpiresInSeconds()<0){
			output = "[" + credential.getAccessToken() + "]" + "has expired";
		} else if (credential.getExpiresInSeconds()!=null && 
				credential.getExpiresInSeconds()>0){
			output = "[ " +credential.getAccessToken() + "]" + credential.getExpiresInSeconds() + " seconds remaining";
		} else {
			output = "[" + credential.getAccessToken() + "]" + "does not expire";
		}
		return output;
	}
	
	private String getCodeStatusText() throws IOException {
		CredentialSharedPreference _appPrefs = new CredentialSharedPreference(getApplicationContext());
		String output = null;
		if (_appPrefs==null || _appPrefs.getSmsBody()==null) {
			output = "No code found.";
		} else {
			output = _appPrefs.getSmsBody();
		} 
		return output;
	}
	
	protected  void startTimer() {
		Log.i(Constants.TAG," +++++ Started timer");
		timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	        	Log.i(Constants.TAG," +++++ Refreshing data");
	        	try {
		            Message msg = new Message();
		            Bundle bundle = new Bundle();
		            bundle.putString("cisco", getTokenStatusText(Oauth2Params.CISCO_OAUTH2));
		            bundle.putString("code", getCodeStatusText());
		            msg.setData(bundle);
		            mHandler.sendMessage(msg);
		            
	        	} catch (Exception ex) {
	        		ex.printStackTrace();
	        		timer.cancel();
		            Message msg = new Message();
		            Bundle bundle = new Bundle();
		            bundle.putString("cisco", ex.getMessage());
		            msg.setData(bundle);
	        		mHandler.sendMessage(msg);
	        	}

	        }
	    }, 0, 1000);
	}

	private static class WeakRefHandler extends Handler {
		    private WeakReference<Activity> ref;
		    public WeakRefHandler(Activity ref) {
		        this.ref = new WeakReference<Activity>(ref);
		    }
		    @Override
		    public void handleMessage(Message msg) {
		    	Activity f = ref.get();
		    	((TextView)f.findViewById(R.id.txt_oauth_cisco)).setText(msg.getData().getString("cisco"));
		    	((TextView)f.findViewById(R.id.txt_oauth_code)).setText(msg.getData().getString("code"));
		    	
		    }
	}
	
	private WeakRefHandler mHandler = new WeakRefHandler(this);

}
