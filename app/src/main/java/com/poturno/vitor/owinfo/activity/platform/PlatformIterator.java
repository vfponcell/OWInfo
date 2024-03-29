package com.poturno.vitor.owinfo.activity.platform;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.poturno.vitor.owinfo.R;
import com.poturno.vitor.owinfo.downloader.IDownloaderListener;
import com.poturno.vitor.owinfo.downloader.JsonDownloader;
import com.poturno.vitor.owinfo.helper.IIterator;
import com.poturno.vitor.owinfo.helper.IIteratorListener;
import com.poturno.vitor.owinfo.helper.KeyWords;
import com.poturno.vitor.owinfo.helper.Url;
import com.poturno.vitor.owinfo.model.Platform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlatformIterator implements IIterator , IDownloaderListener {

    private Activity activity;

    private IIteratorListener listener;

    private Platform[] platforms;

    private int index = 0;

    private int size = 0;

    public PlatformIterator(IIteratorListener listener, Activity activity) {
        this.listener = listener;
        this.activity = activity;
        new JsonDownloader(this, Url.PLATFORM).execute();

    }

    @Override
    public void onJsonRecived(String json) {
        Log.i("Flag", "json recived");
        try {
            size = new JSONObject(json).getInt("total");
            platforms = new Platform[size];
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonToArray(json);
        listener.ready();
    }

    @Override
    public boolean hasNext() {
        return index < size;
    }

    @Override
    public Object next() {
        if (hasNext()) {
            return platforms[index++];
        } else {
            return null;
        }
    }

    @Override
    public void first() {
        index = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int getPosition() {
        return index;
    }

    private void jsonToArray(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(KeyWords.DATA);
            for (int i = 0; i < size; i++) {
                platforms[i] = jsonToPlatform(array.get(i).toString());
                switch (i){
                    case 0 :
                        platforms[i].setBitmap(BitmapFactory.decodeResource(activity.getResources(),R.drawable.win10));
                        break;
                    case 1 :
                        platforms[i].setBitmap(BitmapFactory.decodeResource(activity.getResources(),R.drawable.ps4));
                        break;
                    case 2 :
                        platforms[i].setBitmap(BitmapFactory.decodeResource(activity.getResources(),R.drawable.xbox));
                        break;
                }
            }
        } catch (Exception e) {

        }
    }

    private Platform jsonToPlatform(String json) {
        Platform platform = new Platform();
        try {
            JSONObject jsonObject = new JSONObject(json);
            platform.setId("" + jsonObject.getInt(KeyWords.ID));
            platform.setName(jsonObject.getString(KeyWords.NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return platform;
    }
}
