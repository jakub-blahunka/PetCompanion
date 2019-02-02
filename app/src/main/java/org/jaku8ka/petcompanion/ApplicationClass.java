package org.jaku8ka.petcompanion;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class ApplicationClass extends android.app.Application {

    public static final String APPLICATION_ID = "D466B1D6-D66A-B3F3-FF92-E3D1822E8100";
    public static final String API_KEY = "A95C43F0-A8C1-0AB2-FF63-E4DC3E71D400";
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
