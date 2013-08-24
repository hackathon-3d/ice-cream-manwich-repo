package com.icm;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class BooksWithFriendsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //only do things for setup in here. see DashboardActivity for the launcher activity.
        
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
    }
}
