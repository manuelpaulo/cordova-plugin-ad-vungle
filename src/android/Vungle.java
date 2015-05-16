//Copyright (c) 2014 Sang Ki Kwon (Cranberrygame)
//Email: cranberrygame@yahoo.com
//Homepage: http://www.github.com/cranberrygame
//License: MIT (http://opensource.org/licenses/MIT)
package com.cranberrygame.cordova.plugin.ad.video.vungle;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import android.annotation.TargetApi;
import android.app.Activity;
import android.util.Log;
//
import com.vungle.publisher.AdConfig;
import com.vungle.publisher.EventListener;
import com.vungle.publisher.Orientation;
import com.vungle.publisher.VunglePub;
//md5
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//Util
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Surface;
//
import java.util.*;//Random

class Util {

	//ex) Util.alert(cordova.getActivity(),"message");
	public static void alert(Activity activity, String message) {
		AlertDialog ad = new AlertDialog.Builder(activity).create();  
		ad.setCancelable(false); // This blocks the 'BACK' button  
		ad.setMessage(message);  
		ad.setButton("OK", new DialogInterface.OnClickListener() {  
			@Override  
			public void onClick(DialogInterface dialog, int which) {  
				dialog.dismiss();                      
			}  
		});  
		ad.show(); 		
	}
	
	//https://gitshell.com/lvxudong/A530_packages_app_Camera/blob/master/src/com/android/camera/Util.java
	public static int getDisplayRotation(Activity activity) {
	    int rotation = activity.getWindowManager().getDefaultDisplay()
	            .getRotation();
	    switch (rotation) {
	        case Surface.ROTATION_0: return 0;
	        case Surface.ROTATION_90: return 90;
	        case Surface.ROTATION_180: return 180;
	        case Surface.ROTATION_270: return 270;
	    }
	    return 0;
	}

	public static final String md5(final String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
        }
        return "";
    }
}

public class Vungle extends CordovaPlugin {
	private final String LOG_TAG = "Vungle";
	private CallbackContext callbackContextKeepCallback;
	//
	protected String email;
	protected String licenseKey;
	public boolean validLicenseKey;
	protected String TEST_APP_ID = "com.cranberrygame.pluginsforcordova";
	//
	protected String appId;
	
	// get the VunglePub instance
	final VunglePub vunglePub = VunglePub.getInstance();
	
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
    }
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		if (action.equals("setLicenseKey")) {
			setLicenseKey(action, args, callbackContext);

			return true;
		}	
		else if (action.equals("setUp")) {
			setUp(action, args, callbackContext);

			return true;
		}			
		else if (action.equals("checkAvailable")) {
			checkAvailable(action, args, callbackContext);

			return true;
		}			
		else if (action.equals("showRewardedVideoAd")) {
			showRewardedVideoAd(action, args, callbackContext);
						
			return true;
		}
		
		return false; // Returning false results in a "MethodNotFound" error.
	}

	private void setLicenseKey(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		final String email = args.getString(0);
		final String licenseKey = args.getString(1);				
		Log.d(LOG_TAG, String.format("%s", email));			
		Log.d(LOG_TAG, String.format("%s", licenseKey));
		
		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				_setLicenseKey(email, licenseKey);
			}
		});
	}
	
	private void setUp(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		//Activity activity=cordova.getActivity();
		//webView
		//args.length()
		//args.getString(0)
		//args.getString(1)
		//args.getInt(0)
		//args.getInt(1)
		//args.getBoolean(0)
		//args.getBoolean(1)
		//JSONObject json = args.optJSONObject(0);
		//json.optString("adUnit")
		//json.optString("adUnitFullScreen")
		//JSONObject inJson = json.optJSONObject("inJson");
		//final String adUnit = args.getString(0);
		//final String adUnitFullScreen = args.getString(1);				
		//final boolean isOverlap = args.getBoolean(2);				
		//final boolean isTest = args.getBoolean(3);				
		//Log.d(LOG_TAG, String.format("%s", adUnit));			
		//Log.d(LOG_TAG, String.format("%s", adUnitFullScreen));
		//Log.d(LOG_TAG, String.format("%b", isOverlap));
		//Log.d(LOG_TAG, String.format("%b", isTest));		
		final String appId = args.getString(0);
		Log.d(LOG_TAG, String.format("%s", appId));			
		
		//callbackContextKeepCallback = callbackContext;
			
		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				_setUp(appId);
			}
		});
	}
	
	private void checkAvailable(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		callbackContextKeepCallback = callbackContext;
	
		cordova.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				_checkAvailable();
			}
		});
	}

	private void showRewardedVideoAd(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		callbackContextKeepCallback = callbackContext;
	
		cordova.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				_showRewardedVideoAd();
			}
		});
	}
	
	public void _setLicenseKey(String email, String licenseKey) {
		this.email = email;
		this.licenseKey = licenseKey;
		
		//
		String str1 = Util.md5("com.cranberrygame.cordova.plugin.: " + email);
		String str2 = Util.md5("com.cranberrygame.cordova.plugin.ad.vungle: " + email);
		if(licenseKey != null && (licenseKey.equalsIgnoreCase(str1) || licenseKey.equalsIgnoreCase(str2))) {
			Log.d(LOG_TAG, String.format("%s", "valid licenseKey"));
			this.validLicenseKey = true;
		}
		else {
			Log.d(LOG_TAG, String.format("%s", "invalid licenseKey"));
			this.validLicenseKey = false;
			
			//Util.alert(cordova.getActivity(),"Cordova Vungle: invalid email / license key. You can get free license key from https://play.google.com/store/apps/details?id=com.cranberrygame.pluginsforcordova");			
		}		
	}
	
	private void _setUp(String appId) {
		this.appId = appId;
		
		if (!validLicenseKey) {
			if (new Random().nextInt(100) <= 1) {//0~99					
				this.appId = TEST_APP_ID;
			}
		}
		
		vunglePub.init(cordova.getActivity(), appId);

		final AdConfig config = vunglePub.getGlobalAdConfig();
		config.setOrientation(Orientation.autoRotate);//for android
		//config.setOrientation(Orientation.matchVideo);
	}

	private void _checkAvailable() {
		final boolean available = vunglePub.isCachedAdAvailable();

		if (available) {
			Log.d(LOG_TAG, "available");
			
			PluginResult pr = new PluginResult(PluginResult.Status.OK, "onAvailable");
			pr.setKeepCallback(true);
			callbackContextKeepCallback.sendPluginResult(pr);
		}
		else {
			Log.d(LOG_TAG, "unavailable");
			
			PluginResult pr = new PluginResult(PluginResult.Status.ERROR, "onUnavailable");
			pr.setKeepCallback(true);
			callbackContextKeepCallback.sendPluginResult(pr);
		}
	}
	
	private void _showRewardedVideoAd() {
		vunglePub.setEventListener(new EventListener() {
			@Override
			public void onCachedAdAvailable() {
				Log.d(LOG_TAG, "onCachedAdAvailable");
			}
			
			@Override
			public void onAdUnavailable(String arg0) {
				Log.d(LOG_TAG, "onAdUnavailable");
			}
			
			@Override
			public void onAdStart() {//cranberrygame
				// Called before playing an ad
				Log.d(LOG_TAG, "onAdStart");
				
				PluginResult pr = new PluginResult(PluginResult.Status.OK, "onRewardedVideoAdShown");
				pr.setKeepCallback(true);
				callbackContextKeepCallback.sendPluginResult(pr);
				//PluginResult pr = new PluginResult(PluginResult.Status.ERROR);
				//pr.setKeepCallback(true);
				//callbackContextKeepCallback.sendPluginResult(pr);					
			}

			@Override
			public void onAdEnd(boolean wasCallToActionClicked) {//cranberrygame
				// Called when the user leaves the ad and control is returned to your application
				Log.d(LOG_TAG, "onAdEnd");
				
				PluginResult pr = new PluginResult(PluginResult.Status.OK, "onRewardedVideoAdHidden");
				pr.setKeepCallback(true);
				callbackContextKeepCallback.sendPluginResult(pr);
				//PluginResult pr = new PluginResult(PluginResult.Status.ERROR);
				//pr.setKeepCallback(true);
				//callbackContextKeepCallback.sendPluginResult(pr);					
			}
			
			@Override
			public void onVideoView(boolean isCompletedView, int watchedMillis, int videoDurationMillis) {
				// Called each time an ad completes. isCompletedView is true if at least  
				// 80% of the video was watched, which constitutes a completed view.  
				// watchedMillis is for the longest video view (if the user replayed the 
				// video).
				if (isCompletedView) {
					Log.d(LOG_TAG, "onVideoView: completed");

					PluginResult pr = new PluginResult(PluginResult.Status.OK, "onRewardedVideoAdCompleted");
					pr.setKeepCallback(true);
					callbackContextKeepCallback.sendPluginResult(pr);
					//PluginResult pr = new PluginResult(PluginResult.Status.ERROR);
					//pr.setKeepCallback(true);
					//callbackContextKeepCallback.sendPluginResult(pr);
				}
				else {
					Log.d(LOG_TAG, "onVideoView: not completed");
					
					PluginResult pr = new PluginResult(PluginResult.Status.OK, "onRewardedVideoAdNotCompleted");
					pr.setKeepCallback(true);
					callbackContextKeepCallback.sendPluginResult(pr);
					//PluginResult pr = new PluginResult(PluginResult.Status.ERROR);
					//pr.setKeepCallback(true);
					//callbackContextKeepCallback.sendPluginResult(pr);
				}
			}
		});
		
		final AdConfig overrideConfig = new AdConfig();			
		vunglePub.playAd(overrideConfig);
	}	
} 