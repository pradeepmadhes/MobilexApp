package com.mobilex.lawmobilelibrary.activities;

import android.app.Activity;
import android.os.Bundle;

public class DemoActivity extends Activity 
{
    @Override
    public void onCreate(Bundle icicle) 
    {
        super.onCreate(icicle);
        setContentView(R.layout.demoview);
//        Uri path = Uri.parse("android.resource://com.droisys.secnsec.activities/" + R.raw.ad_4);
//        VideoView myVid = (VideoView) findViewById(R.id.videoView_1);
//        myVid.setVideoURI(path);
    }
}
