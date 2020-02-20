package com.example.vuforialib;


import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.company.product.OverrideUnityActivity;
import com.unity3d.player.Api;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends OverrideUnityActivity {

    // Replace with your vuforia database keys
    private String LicenceKeyToUnity = "AcEmsVH/////AAABmTIBcS+Y20+Qjg+Jm6HZKjRCd7QlB0kbg7u51LVFw4IxOdMl1aBxg6nXZe5fCGXJSHqRY71OCtT1J02daVcAC3AW53R5urA8JYMgJ3zLB6mmwZSeOmH1ueNyZrXtW//OHVhgHMElAIA5Ws+KNbWLRhPESzEho2A4laZKPrxfHR21ID3gjxpMsZOv+URu4cp2AAde5LCG165bTXBglhzVuW5YyuXsppFKK0NT3ylM5Yk1qEcOT/oVXFvQFcWTGlapW402WcF7LRHS2Ya3yM0cr6VLMsvNqehArWTdM4Ya9u0YzdHm7uwG4isZ4iThg2+vLitkYxFG0ot+KyIdM/TLXvsGgEgV3bN/ColgsmrR9Z4e";
    private String AccessKeyToUnity = "e18395eb18679b91c83ec494152dcb1ee9355dd1";
    private String SecretKeyToUnity = "651471353c1b59a815f62e450dd3fa2c7bf6d8f0";
    private String android_id;
    private JSONObject jsonObject;

    @Override
    protected void showMainActivity(String setToColor) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        jsonObject = new JSONObject();

        try {
            jsonObject.put("license_key",LicenceKeyToUnity);
            jsonObject.put("client_access",AccessKeyToUnity);
            jsonObject.put("client_secret",SecretKeyToUnity);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.v("RAJU","DEVELOPER : "+jsonObject);

        UnitySendMessage("LoadSceneFromAndriod","DeviceID",android_id);

        UnitySendMessage("LoadSceneFromAndriod","GetAPI", Api.BASE_URL_UNITY);

        UnitySendMessage("LoadSceneFromAndriod","KeysToAccess",String.valueOf(jsonObject));






    }

    @Override
    protected void UnitySendMessage(String gameObj, String method, String arg) {
        super.UnitySendMessage(gameObj, method, arg);
    }
}
