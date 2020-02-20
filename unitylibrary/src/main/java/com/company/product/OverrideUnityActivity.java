package com.company.product;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONObject;

public abstract class OverrideUnityActivity extends UnityPlayerActivity
{
    public static OverrideUnityActivity instance = null;

    abstract protected void showMainActivity(String setToColor);

    protected void UnitySendMessage(String gameObj, String method, String arg) {
        mUnityPlayer.UnitySendMessage(gameObj,method,arg);

        Log.v("SURYA","GAME OBJECT "+gameObj);
        Log.v("SURYA","METHOD NAME "+method);
        Log.v("SURYA","OBJECT "+arg);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        instance = this;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

}
