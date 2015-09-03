package com.crealoya.votopilas.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crealoya.votopilas.model.Charge;
import com.crealoya.votopilas.model.ListName;
import com.crealoya.votopilas.model.ListPosition;
import com.crealoya.votopilas.model.Party;
import com.crealoya.votopilas.model.Politician;
import com.crealoya.votopilas.util.CacheManager;
import com.crealoya.votopilas.util.UserPreferencesHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by @pablopantaleon on 7/29/15.
 */
public class ParseHelper {

    private static final int QUERY_LIMIT = 300;

    private static final String QUERY_TAG_LIST_POSITION = "query.list_position";
    private static final String QUERY_TAG_LIST_NAME = "query.list_name";
    private static final String QUERY_TAG_PARTY = "query.party";
    private static final String QUERY_TAG_CHARGE = "query.charge";

    private static <T extends ParseObject> void doSimpleQuery(@NonNull ParseQuery<T> query,
                                                               @Nullable final Callback<List<T>> listCallback,
                                                               final String queryTag, final String prefKey) {
        final boolean isCache = CacheManager.isQueryCache(prefKey);

        if (isCache) {
            query.fromLocalDatastore();
        }

        query.findInBackground(new FindCallback<T>() {
            @Override
            public void done(final List<T> data, ParseException e) {
                if (e != null) {
                    if (listCallback != null) {
                        listCallback.done(false, null);
                    }
                } else if (!isCache) {
                    if (listCallback != null) {
                        listCallback.done(true, data);
                    }
                } else {
                    CacheManager.setListQueryCache(queryTag, prefKey, data, listCallback);
                }
            }
        });
    }

    public static void getListNames(@Nullable final Callback<List<ListName>> listCallback) {
        doSimpleQuery(ParseQuery.getQuery(ListName.class).orderByDescending("name"), listCallback, QUERY_TAG_LIST_NAME, CacheManager.PREF_LIST_NAME_CACHE);
    }

    public static void getParties(@Nullable final Callback<List<Party>> listCallback) {
        ParseQuery query = ParseQuery.getQuery(Party.class);
        query.orderByDescending("number_of_politicians");
        doSimpleQuery(query, listCallback, QUERY_TAG_PARTY, CacheManager.PREF_PARTY_CACHE);
    }

    public static void getCharges(@Nullable final Callback<List<Charge>> listCallback) {
        doSimpleQuery(ParseQuery.getQuery(Charge.class), listCallback, QUERY_TAG_CHARGE, CacheManager.PREF_CHARGE_CACHE);
    }

    public static void getListPositions(@NonNull final Callback<List<ListPosition>> listCallback, List<ListName> listNames, Party party) {
        // define Politician Parse Query - fetch all politicians of party
        final ParseQuery<Politician> politicianParseQuery = ParseQuery.getQuery(Politician.class);
        politicianParseQuery.whereEqualTo(Politician.COL_PARTY, party);
        politicianParseQuery.setLimit(QUERY_LIMIT);

        // define ListPosition Parse Query
        // fetch all ListPositions (contains parties, politicians, etc) by name (e.g. Nacional)
        final List<ListName> allListNames = UserPreferencesHelper.getUserLists(listNames);
        ParseQuery<ListPosition> listSettingsQuery = ParseQuery.getQuery(ListPosition.class);
        listSettingsQuery.whereContainedIn(ListPosition.COL_LIST, allListNames);
        listSettingsQuery.whereMatchesKeyInQuery(ListPosition.COL_POLITICIAN, "objectId", politicianParseQuery);
        listSettingsQuery.orderByAscending("position");
        listSettingsQuery.include(ListPosition.COL_POLITICIAN);
        listSettingsQuery.include(ListPosition.COL_CHARGE);

        doSimpleQuery(listSettingsQuery, listCallback, QUERY_TAG_LIST_POSITION, CacheManager.PREF_LIST_POSITION);
    }
}
