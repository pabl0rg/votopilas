package com.crealoya.votopilas;

import android.app.Application;

import com.crealoya.votopilas.model.Charge;
import com.crealoya.votopilas.model.Education;
import com.crealoya.votopilas.model.LaborExperience;
import com.crealoya.votopilas.model.ListName;
import com.crealoya.votopilas.model.ListPosition;
import com.crealoya.votopilas.model.Party;
import com.crealoya.votopilas.model.PoliticalExperience;
import com.crealoya.votopilas.model.Politician;
import com.crealoya.votopilas.model.Rating;
import com.crealoya.votopilas.util.PreferencesUtil;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

/**
 * Created by Pablo Reyes, @pablopantaleon on 6/24/15.
 */
public class VotopilasApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // setup facebook
//        FacebookSdk.sdkInitialize(getApplicationContext());

        // parse setup
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Party.class);
        ParseObject.registerSubclass(Politician.class);
        ParseObject.registerSubclass(Charge.class);
        ParseObject.registerSubclass(ListPosition.class);
        ParseObject.registerSubclass(PoliticalExperience.class);
        ParseObject.registerSubclass(Education.class);
        ParseObject.registerSubclass(LaborExperience.class);
        ParseObject.registerSubclass(ListName.class);
        ParseObject.registerSubclass(Rating.class);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
//        ParseFacebookUtils.initialize(this);
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));

        // preferences
        PreferencesUtil.init(this);
    }
}
