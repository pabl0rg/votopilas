package com.crealoya.votopilas.network;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.adapter.RatingsToApplyAdapter;
import com.crealoya.votopilas.model.Party;
import com.crealoya.votopilas.model.Politician;
import com.crealoya.votopilas.model.Rating;
import com.crealoya.votopilas.util.PreferencesUtil;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by @pablopantaleon on 7/28/15.
 */
public class ParseCloudHelper {

    // Parse Cloud functions
    private static final String FUNC_GET_RATING_AVG = "getRatingAvg";
    private static final String FUNC_GET_VIP_USERS = "getVIPUsers";

    // Parse Cloud keys
    private static final String KEY_POLITICIAN = "politician";

    public static void getRatingAvg(Politician politician, final RatingBar ratingBar) {
        Map<String, String> politicianMap = new HashMap<>();
        politicianMap.put(KEY_POLITICIAN, politician.getObjectId());
        ParseCloud.callFunctionInBackground(FUNC_GET_RATING_AVG, politicianMap, new FunctionCallback<Object>() {
            @Override
            public void done(Object o, ParseException e) {
                if (e == null && o instanceof Integer) {
                    ratingBar.setRating((Integer) o);
                }
            }
        });
    }

    public static void getRatingByUsersAvg(final Politician politician, final RatingBar ratingBar, @Nullable final Callback callback) {

        //get conditions
        final HashSet<String> vip_users = new HashSet<>(PreferencesUtil.getSetPreference(PreferencesUtil.PREF_RATINGS_TO_APPLY, new HashSet<String>()));
        final boolean all_users = vip_users.contains("0");
        vip_users.remove("0");

        //get users for rating
        List<ParseQuery<ParseObject>> queries = new ArrayList<>();
        if (all_users) queries.add(ParseQuery.getQuery("_User").whereEqualTo("vip", false));
        queries.add(ParseQuery.getQuery("_User").whereContainedIn("objectId", vip_users));

        //get rating
        ParseQuery<Rating> query = ParseQuery.getQuery(Rating.class);
        query.whereEqualTo("politician", ParseObject.createWithoutData("Politician", politician.objectId));
        query.whereMatchesQuery("user", ParseQuery.or(queries));
        query.findInBackground(new FindCallback<Rating>() {
            @Override
            public void done(List<Rating> list, ParseException e) {
                if (e == null && list.size() > 0) {
                    if (callback == null) {
                        int sum = 0;
                        for (Rating rating : list) {
                            sum += rating.getInt("value");
                        }
                        int total = sum / list.size();
                        ratingBar.setRating(total);
                    } else {
                        callback.done(true, list);
                    }
                }
            }
        });
    }

    public static void getRatingByUsersAndPartyAvg(final Party party, final RatingBar ratingBar) {

        //get conditions
        final HashSet<String> vip_users = new HashSet<>(PreferencesUtil.getSetPreference(PreferencesUtil.PREF_RATINGS_TO_APPLY, new HashSet<String>()));
        final boolean all_users = vip_users.contains("0");
        vip_users.remove("0");

        //get users for rating
        List<ParseQuery<ParseObject>> userQueries = new ArrayList<>();
        if (all_users) userQueries.add(ParseQuery.getQuery("_User").whereEqualTo("vip", false));
        userQueries.add(ParseQuery.getQuery("_User").whereContainedIn("objectId", vip_users));
        //get politician for rating
        ParseQuery<ParseObject> politicianQuery = ParseQuery.getQuery("Politician").whereEqualTo("party", ParseObject.createWithoutData("Party", party.objectId));
        //get rating
        ParseQuery<Rating> query = ParseQuery.getQuery(Rating.class);
        query.whereMatchesQuery("politician", politicianQuery);
        query.whereMatchesQuery("user", ParseQuery.or(userQueries));
        query.findInBackground(new FindCallback<Rating>() {
            @Override
            public void done(List<Rating> list, ParseException e) {
                if (e == null && list.size() > 0) {
                    int sum = 0;
                    for (Rating rating : list) {
                        sum += rating.getInt("value");
                    }
                    int total = sum / list.size();
                    ratingBar.setRating(total);
                }
            }
        });
    }

    public static void getVipUsers(final Context context, final ListView listView, final ProgressBar progress,final HashSet<String> checkedUsers) {
        Map<String, String> parameters = new HashMap<>();
        ParseCloud.callFunctionInBackground(FUNC_GET_VIP_USERS, parameters, new FunctionCallback<List<ParseObject>>() {
            @Override
            public void done(final List<ParseObject> users, ParseException e) {
                if (e == null) {
                    //adding user 0
                    ParseObject user = ParseObject.createWithoutData("_User", "0");
                    user.put("name", context.getString(R.string.user_vals_name));
                    users.add(0, user);
                    RatingsToApplyAdapter adapter = new RatingsToApplyAdapter(context, R.layout.item_rating_to_apply_layout, users);
                    listView.setAdapter(adapter);
                    listView.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.INVISIBLE);

                    if (checkedUsers != null && !checkedUsers.isEmpty()) {
                        listView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < users.size(); i++) {
                                    ParseObject user = users.get(i);
                                    if (checkedUsers.contains(user.getObjectId())) {
                                        listView.setItemChecked(i, true);
                                    }
                                }
                            }
                        }, 300);
                    }
                }
            }
        });
    }
}
