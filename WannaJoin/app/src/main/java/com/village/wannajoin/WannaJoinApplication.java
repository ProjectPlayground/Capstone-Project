package com.village.wannajoin;


/**
 * Created by richa on 3/29/16.
 */
/**
 * Includes one-time initialization of Firebase related code
 */
public class WannaJoinApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Initialize Firebase */
        //Firebase.setAndroidContext(this);
        /* Enable disk persistence  */
        //Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
