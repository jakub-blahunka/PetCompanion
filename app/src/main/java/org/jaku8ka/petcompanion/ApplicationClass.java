package org.jaku8ka.petcompanion;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class ApplicationClass extends android.app.Application {

    public static final String APPLICATION_ID = "7E37FB23-97E4-058C-FF64-5452FA3E9E00";
    public static final String API_KEY = "7475427F-1B3D-D697-FF0D-8C5B21ABEF00";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static List<Pet> pets;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
    }
}
