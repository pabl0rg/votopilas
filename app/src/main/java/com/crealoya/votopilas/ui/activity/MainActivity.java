package com.crealoya.votopilas.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.adapter.PartyPoliticianViewPagerAdapter;
import com.crealoya.votopilas.model.ListName;
import com.crealoya.votopilas.model.Party;
import com.crealoya.votopilas.model.Politician;
import com.crealoya.votopilas.network.Callback;
import com.crealoya.votopilas.network.ParseHelper;
import com.crealoya.votopilas.ui.fragment.DrawerFragment;
import com.crealoya.votopilas.ui.fragment.PartyPoliticianListFragment;
import com.crealoya.votopilas.util.UserPreferencesHelper;
import com.crealoya.votopilas.util.PreferencesUtil;
import com.parse.ParseFacebookUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Pablo Reyes, @pablopantaleon on 6/28/15.
 */
public class MainActivity extends BaseActivity implements DrawerFragment.OnDrawerInteractionListener,
        PartyPoliticianListFragment.PartyPoliticianListener {

    private final String FB_PROFILE_URL = "https://graph.facebook.com/v2.1/%s/picture?width=270&height=270";

    public static final int REQUEST_RATINGS_TO_APPLY = 99;
    public static final int REQUEST_LISTNAMES = 100;
    public static final int REQUEST_PARTIES = 101;
    // UI components
    private DrawerLayout mDrawerLayout;
    private TabLayout mPartiesTabLayout;
    private ViewPager mPartyInfoViewPager;
//    private View mNavDrawerHeader;
//    private TextView mUsername;
//    private ImageView mAvatar;

    // Adapters
    private PartyPoliticianViewPagerAdapter mPartyPoliticianViewPagerAdapter;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.create(savedInstanceState, R.layout.activity_main);

        // find views
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mPartiesTabLayout = (TabLayout) findViewById(R.id.parties_tab_layout);
        mPartyInfoViewPager = (ViewPager) findViewById(R.id.party_viewpager);
//        mNavDrawerHeader = findViewById(R.id.navigation_view_header);
//        mUsername = (TextView) findViewById(R.id.username);
//        mAvatar = (ImageView) findViewById(R.id.avatar);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        // custom ui
        int whiteColor = getResources().getColor(android.R.color.white);
        mPartiesTabLayout.setTabTextColors(whiteColor, whiteColor);

        // set listeners
//        navigationView.setNavigationItemSelectedListener(this);

        // setup drawer layout
//        if (ParseUser.getCurrentUser() != null) {
//            mNavDrawerHeader.setBackgroundColor(getResources().getColor(R.color.nav_view_header));
//            mUsername.setText(String.valueOf(ParseUser.getCurrentUser().get("name")));
//            if (ParseUser.getCurrentUser().has("fbId")) {
//                Glide.with(MainActivity.this)
//                        .load(String.format(FB_PROFILE_URL, ParseUser.getCurrentUser().get("fbId")))
//                        .transform(new CircleTransformation(MainActivity.this))
//                        .crossFade()
//                        .into(mAvatar);
//            }
//        } else {
//            navigationView.getMenu().findItem(R.id.drawer_list_item_logout_login).setTitle(R.string.drawer_list_item_login);
//        }
        refreshTabs();
    }

    private void refreshTabs() {
        // fetch parties
        ParseHelper.getParties(new Callback<List<Party>>() {
            @Override
            public void done(boolean success, List<Party> data) {
                if (success) {
                    ArrayList<Party> finalDataList = new ArrayList<>();

                    for (Party party : data) {
                        party.init();
                        if (!UserPreferencesHelper.contains(UserPreferencesHelper.getCurrentListName(), party)) {
                            finalDataList.add(party);
                        }
                    }
                    mPartyPoliticianViewPagerAdapter = new PartyPoliticianViewPagerAdapter(getSupportFragmentManager(), finalDataList);
                    mPartyInfoViewPager.setAdapter(mPartyPoliticianViewPagerAdapter);

                    // setup view pager
                    mPartiesTabLayout.setupWithViewPager(mPartyInfoViewPager);
                    mPartiesTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
                    mPartiesTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                } else {
                    // TODO: show some toast about connection
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                final Party party = mPartyPoliticianViewPagerAdapter.getParty(mPartyInfoViewPager.getCurrentItem());

                if (party != null) {
                    SearchActivity.launch(this, party);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onNavigationItemSelected(final MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            case R.id.drawer_list_item_choose_value:
//                Intent selectValuesActivity = new Intent(MainActivity.this, SelectValuesActivity.class);
//                HashSet<String> usersIds = new HashSet<>(PreferencesUtil.getSetPreference(PreferencesUtil.PREF_RATINGS_TO_APPLY, new HashSet<String>()));
//                selectValuesActivity.putExtra(SelectValuesActivity.EXTRA_USERS_IDS, usersIds);
//                startActivityForResult(selectValuesActivity, REQUEST_RATINGS_TO_APPLY);
//                break;
//            case R.id.drawer_list_item_restore_lists:
//                RestoreActivity.launch(this, RestoreActivity.LIST, REQUEST_LISTNAMES);
//                break;
//            case R.id.drawer_list_item_restore_parties:
//                RestoreActivity.launch(this, RestoreActivity.PARTY, REQUEST_PARTIES);
//                break;
//            case R.id.drawer_list_item_search:
//                final Party party = mPartyPoliticianViewPagerAdapter.getParty(mPartyInfoViewPager.getCurrentItem());
//
//                if (party != null) {
//                    SearchActivity.launch(this, party);
//                }
//                break;
//            case R.id.drawer_list_item_about:
//                startActivity(new Intent(this, AboutActivity.class));
//                break;
//            case R.id.drawer_list_item_logout_login:
//                if (ParseUser.getCurrentUser() == null) {
//                    LoginHelper.showLogInDialog(this, LoginHelper.LOGIN_WITH_CANCEL, new LoginHelper.LoginCallback() {
//                        @Override
//                        public void onLogin(boolean success) {
//                            if (success) {
//                                mNavDrawerHeader.setBackgroundColor(getResources().getColor(R.color.nav_view_header));
//                                mUsername.setText((CharSequence) ParseUser.getCurrentUser().get("name"));
//                                menuItem.setTitle(getString(R.string.drawer_list_item_logout));
//
//                                if (ParseUser.getCurrentUser().has("fbId")) {
//                                    Glide.with(MainActivity.this)
//                                            .load(String.format(FB_PROFILE_URL, ParseUser.getCurrentUser().get("fbId")))
//                                            .transform(new CircleTransformation(MainActivity.this))
//                                            .crossFade()
//                                            .into(mAvatar);
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onLoginCancel() {
//                            // do nothing
//                        }
//                    });
//                } else {
//                    ParseUser.logOut();
//                    com.facebook.Session fbs = com.facebook.Session.getActiveSession();
//
//                    if (fbs != null) {
//                        fbs.closeAndClearTokenInformation();
//                    }
//
//                    mNavDrawerHeader.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
//                    mUsername.setText(getString(R.string.drawer_header_anonymous));
//                    mAvatar.setImageBitmap(null);
//                    menuItem.setTitle(getString(R.string.drawer_list_item_login));
//                }
//                break;
//            default:
//                return false;
//        }
//
//        menuItem.setChecked(true);
//        mDrawerLayout.closeDrawers();
//        return true;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_RATINGS_TO_APPLY == requestCode) {
            if (resultCode == RESULT_OK) {
                HashSet<String> usersIds = (HashSet<String>) data.getSerializableExtra(SelectValuesActivity.EXTRA_USERS_IDS);
                PreferencesUtil.setSetPreference(PreferencesUtil.PREF_RATINGS_TO_APPLY, usersIds);
            }
        } else if (REQUEST_LISTNAMES == requestCode || REQUEST_PARTIES == requestCode) {
            if (resultCode == RESULT_OK) {
                refreshTabs();
            }
        } else {
            ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
        }
    }

    @Override
    public void removeParty(Party party) {
        UserPreferencesHelper.addToHiddenParties(UserPreferencesHelper.getCurrentListName(), party.shortname + " - " + party.name);
        refreshTabs();
    }

    @Override
    public void onPoliticianSelected(Politician politician) {
        Intent intent = new Intent(this, PoliticianActivity.class);
        intent.putExtra(Politician.PARSE_POLITICIAN_OBJECT, politician);
        startActivity(intent);
    }

    @Override
    public void onListClicked(ListName list) {
        UserPreferencesHelper.setList(list.name);
        mDrawerLayout.closeDrawers();
        refreshTabs();
    }
}
