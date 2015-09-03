package com.crealoya.votopilas.util;

import com.crealoya.votopilas.model.ListName;
import com.crealoya.votopilas.model.Party;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by @pablopantaleon on 7/29/15.
 */
public class UserPreferencesHelper {

    public static final String PREF_USER_SHOW_LIST = "pref.user_hide_list";
    public static final String PREF_USER_HIDE_PARTY = "pref.user_hide_party";
    public static final String DEFAULT_LIST = "Presidencial";


    public static List<ListName> getUserLists(List<ListName> listNames) {
        HashSet<String> hashSet = (HashSet<String>) PreferencesUtil.getSetPreference(PREF_USER_SHOW_LIST, new HashSet<String>());

        if (hashSet.isEmpty()) { //initializing
            setList(DEFAULT_LIST);
            hashSet.add(DEFAULT_LIST);
        }

        final List<ListName> itemsToRemove = new ArrayList<>();

        for (ListName listName : listNames) {
            listName.init();
            if (!hashSet.contains(listName.name)) {
                itemsToRemove.add(listName);
            }
        }

        listNames.removeAll(itemsToRemove);

        return listNames;
    }

    public static void addToShowLists(String listName) {
        HashSet<String> hashSet = (HashSet<String>) PreferencesUtil.getSetPreference(PREF_USER_SHOW_LIST, new HashSet<String>());

        if (!hashSet.contains(listName)) {
            hashSet.add(listName);
            PreferencesUtil.setSetPreference(PREF_USER_SHOW_LIST, null);
            PreferencesUtil.setSetPreference(PREF_USER_SHOW_LIST, hashSet);
        }
    }

    public static void removeFromShowLists(String listName) {
        HashSet<String> hashSet = (HashSet<String>) PreferencesUtil.getSetPreference(PREF_USER_SHOW_LIST, new HashSet<String>());

        if (hashSet.contains(listName)) {
            hashSet.remove(listName);
            PreferencesUtil.setSetPreference(PREF_USER_SHOW_LIST, null);
            PreferencesUtil.setSetPreference(PREF_USER_SHOW_LIST, hashSet);
        }
    }

    public static String getCurrentListName() {
        HashSet<String> hashSet = (HashSet<String>) PreferencesUtil.getSetPreference(PREF_USER_SHOW_LIST, new HashSet<String>());

        if (hashSet.isEmpty()) { //initializing
            setList(DEFAULT_LIST);
            hashSet.add(DEFAULT_LIST);
        }

        return (String) hashSet.toArray()[0];
    }

    public static void setList(String listName) {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add(listName);
        PreferencesUtil.setSetPreference(PREF_USER_SHOW_LIST, null);
        PreferencesUtil.setSetPreference(PREF_USER_SHOW_LIST, hashSet);
    }



    public static void addToHiddenParties(String listName, String partyName) {
        HashSet<String> hashSet = (HashSet<String>) PreferencesUtil.getSetPreference(PREF_USER_HIDE_PARTY + "-" +  listName, new HashSet<String>());

        if (!hashSet.contains(partyName)) {
            hashSet.add(partyName);
            PreferencesUtil.setSetPreference(PREF_USER_HIDE_PARTY + "-" +  listName, null);
            PreferencesUtil.setSetPreference(PREF_USER_HIDE_PARTY + "-" +  listName, hashSet);
        }
    }

    public static void removeFromHiddenParties(String listName, String partyName) {
        HashSet<String> hashSet = (HashSet<String>) PreferencesUtil.getSetPreference(PREF_USER_HIDE_PARTY + "-" +  listName, new HashSet<String>());

        if (hashSet.contains(partyName)) {
            hashSet.remove(partyName);
            PreferencesUtil.setSetPreference(PREF_USER_HIDE_PARTY + "-" +  listName, null);
            PreferencesUtil.setSetPreference(PREF_USER_HIDE_PARTY + "-" +  listName, hashSet);
        }
    }

    public static void clearHiddenParties() {
        PreferencesUtil.clearAllPreferencesThatStartWith(PREF_USER_HIDE_PARTY);
    }

    public static boolean contains(ListName listName) {
        HashSet<String> hashSet = (HashSet<String>) PreferencesUtil.getSetPreference(PREF_USER_SHOW_LIST, new HashSet<String>());
        return hashSet.contains(listName.name);
    }

    public static boolean contains(String listName, Party party) {
        HashSet<String> hashSet = (HashSet<String>) PreferencesUtil.getSetPreference(PREF_USER_HIDE_PARTY + "-" +  listName, new HashSet<String>());
        return hashSet.contains(party.shortname + " - " + party.name);
    }
}
