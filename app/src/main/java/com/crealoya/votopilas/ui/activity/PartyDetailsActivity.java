package com.crealoya.votopilas.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crealoya.votopilas.R;
import com.crealoya.votopilas.model.Charge;
import com.crealoya.votopilas.model.Party;
import com.crealoya.votopilas.model.Politician;
import com.crealoya.votopilas.network.Callback;
import com.crealoya.votopilas.network.ParseCloudHelper;
import com.crealoya.votopilas.network.ParseHelper;
import com.crealoya.votopilas.ui.view.ExpandableLayout;
import com.crealoya.votopilas.util.StringManager;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class PartyDetailsActivity extends BaseActivity {

    public static final String EXTRA_PARTY = "extra.party";

    private Party mParty;

    public static void launch(Context context, final Party party) {
        final Intent intent = new Intent(context, PartyDetailsActivity.class);
        intent.putExtra(EXTRA_PARTY, party);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.create(savedInstanceState, R.layout.activity_party_details);
        showNavigationBack();

        // arguments
        mParty = getIntent().getExtras().getParcelable(EXTRA_PARTY);
        getSupportActionBar().setTitle(mParty.name);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // find views details
        ImageView partyPicture = (ImageView) findViewById(R.id.party_picture);
        RatingBar partyRating = (RatingBar) findViewById(R.id.rating_party);

        View presidentLayout = findViewById(R.id.president_layout);
        final TextView presidentName = (TextView) presidentLayout.findViewById(R.id.politician_name);
        final TextView presidentCharge = (TextView) presidentLayout.findViewById(R.id.politician_charge);
        final RatingBar presidentRating = (RatingBar) presidentLayout.findViewById(R.id.rating_politician);
        final ImageView presidentImage = (ImageView) presidentLayout.findViewById(R.id.politician_photo);
        View vicePresidentLayout = findViewById(R.id.vice_president_layout);
        final TextView vicePresidentName = (TextView) vicePresidentLayout.findViewById(R.id.politician_name);
        final TextView vicePresidentCharge = (TextView) vicePresidentLayout.findViewById(R.id.politician_charge);
        final RatingBar vicePresidentRating = (RatingBar) vicePresidentLayout.findViewById(R.id.rating_politician);
        final ImageView vicePresidentImage = (ImageView) vicePresidentLayout.findViewById(R.id.politician_photo);

        // find views general
        final ExpandableLayout expandableGeneral = (ExpandableLayout) findViewById(R.id.expandable_layout_general);
        final ImageView generalArrow = (ImageView) expandableGeneral.findViewById(R.id.expandable_header_section_arrow);
        TextView headerGeneral = (TextView) expandableGeneral.findViewById(R.id.expandable_header_text_view);
        TextView foundationDate = (TextView) expandableGeneral.findViewById(R.id.general_section_foundation_date);
        TextView address = (TextView) expandableGeneral.findViewById(R.id.general_section_address);
        TextView countOffices = (TextView) expandableGeneral.findViewById(R.id.general_section_count_offices);
        TextView phoneNumbers = (TextView) expandableGeneral.findViewById(R.id.general_section_phone_numbers);
        TextView description = (TextView) expandableGeneral.findViewById(R.id.general_section_description);

        // find views transparency
        final ExpandableLayout expandableTransparency = (ExpandableLayout) findViewById(R.id.expandable_layout_transparency);
        final ImageView transparencyArrow = (ImageView) expandableTransparency.findViewById(R.id.expandable_header_section_arrow);
        TextView headerTransparency = (TextView) expandableTransparency.findViewById(R.id.expandable_header_text_view);
        RatingBar ratingPolitician = (RatingBar) expandableTransparency.findViewById(R.id.rating_politician);
        RelativeLayout facebookRow = (RelativeLayout) expandableTransparency.findViewById(R.id.facebook_row);
        ImageView facebookIcon = (ImageView) expandableTransparency.findViewById(R.id.facebook_icon);
        TextView facebookText = (TextView) expandableTransparency.findViewById(R.id.facebook_text);
        ImageView facebookExternal = (ImageView) expandableTransparency.findViewById(R.id.facebook_external);
        RelativeLayout twitterRow = (RelativeLayout) expandableTransparency.findViewById(R.id.twitter_row);
        ImageView twitterIcon = (ImageView) expandableTransparency.findViewById(R.id.twitter_icon);
        TextView twitterText = (TextView) expandableTransparency.findViewById(R.id.twitter_text);
        ImageView twitterExternal = (ImageView) expandableTransparency.findViewById(R.id.twitter_external);
        RelativeLayout webRow = (RelativeLayout) expandableTransparency.findViewById(R.id.web_row);
        ImageView webIcon = (ImageView) expandableTransparency.findViewById(R.id.web_page_icon);
        TextView webText = (TextView) expandableTransparency.findViewById(R.id.web_text);
        ImageView webExternal = (ImageView) expandableTransparency.findViewById(R.id.web_external);

        // values details
        if (mParty.image != null) {
            Glide.with(this).load(mParty.image).into(partyPicture);
        }
        ParseCloudHelper.getRatingByUsersAndPartyAvg(mParty, partyRating);

        // transparency raiting
        float rating = 5.0f;

        // values general
        headerGeneral.setText(getString(R.string.party_details_section_general));
        foundationDate.setText(String.valueOf(mParty.foundation));
        address.setText(mParty.address);
        if (mParty.address.length() == 0)
            rating -= 1.0f;
        countOffices.setText(String.valueOf(mParty.number_of_offices));
        phoneNumbers.setText(mParty.phone);
        if (mParty.phone.length() == 0)
            rating -= 1.0f;
        description.setText(mParty.description);

        // values transparency
        headerTransparency.setText(getString(R.string.party_details_section_transparency));
        if (mParty.facebook != null && mParty.facebook.length() > 0) {
            facebookRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = mParty.facebook;
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    Log.e("PartyDetailsActivity", url);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            });
        } else {
            facebookIcon.setAlpha(0.6f);
            facebookText.setAlpha(0.6f);
            facebookExternal.setAlpha(0.6f);
            rating -= 1.0;
        }
        if (mParty.twitter != null && mParty.twitter.length() > 0) {
            twitterRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = mParty.twitter;
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            });
        } else {
            twitterIcon.setAlpha(0.6f);
            twitterText.setAlpha(0.6f);
            twitterExternal.setAlpha(0.6f);
            rating -= 1.0;
        }
        if (mParty.website!= null && mParty.website.length() > 0) {
            webRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = mParty.website;
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            });
        } else {
            webIcon.setAlpha(0.6f);
            webText.setAlpha(0.6f);
            webExternal.setAlpha(0.6f);
            rating -= 1.0;
        }
        ratingPolitician.setRating(rating);

        // get charges
        ParseHelper.getCharges(new Callback<List<Charge>>() {
            @Override
            public void done(boolean success, List<Charge> data) {
                if (success) {
                    Charge firstCharge = null, secondCharge = null;
                    for (Charge charge : data) {
                        charge.init();
                        Log.e("PartyDetailsActivity", "name: " + charge.objectId);
                        if (charge.name != null && charge.name.equals("Presidente")) {
                            firstCharge = charge;
                        }
                        if (charge.name != null && charge.name.equals("Vicepresidente")) {
                            secondCharge = charge;
                        }
                    }

                    // get party
                    ParseQuery<Party> query = ParseQuery.getQuery(Party.class);
                    final Charge finalFirstCharge = firstCharge;
                    final Charge finalSecondCharge = secondCharge;
                    presidentCharge.setText(finalFirstCharge.name);
                    vicePresidentCharge.setText(finalSecondCharge.name);
                    query.getInBackground(mParty.objectId, new GetCallback<Party>() {
                        public void done(Party party, ParseException e) {
                            if (e == null) {
                                party.init();

                                ParseQuery<Politician> query = ParseQuery.getQuery(Politician.class);
                                query.whereEqualTo("charge", finalFirstCharge);
                                query.whereEqualTo("party", party);

                                query.getFirstInBackground(new GetCallback<Politician>() {
                                    @Override
                                    public void done(Politician president, ParseException e) {
                                        if (e == null && president != null) {
                                            president.init();
                                            presidentName.setText(StringManager.getFullName(president));
                                            ParseCloudHelper.getRatingByUsersAvg(president, presidentRating, null);
                                            if (president.image != null) {
                                                Glide.with(PartyDetailsActivity.this).load(president.image).into(presidentImage);
                                            }
                                        } else {
                                            presidentName.setText(R.string.party_details_no_available);
                                        }
                                    }
                                });

                                ParseQuery<Politician> query2 = ParseQuery.getQuery(Politician.class);
                                query2.whereEqualTo("charge", finalSecondCharge);
                                query2.whereEqualTo("party", party);

                                query2.getFirstInBackground(new GetCallback<Politician>() {
                                    @Override
                                    public void done(Politician vicepresident, ParseException e) {
                                        if (e == null && vicepresident != null) {
                                            vicepresident.init();
                                            vicePresidentName.setText(StringManager.getFullName(vicepresident));
                                            ParseCloudHelper.getRatingByUsersAvg(vicepresident, vicePresidentRating, null);
                                            if (vicepresident.image != null) {
                                                Glide.with(PartyDetailsActivity.this).load(vicepresident.image).into(vicePresidentImage);
                                            }
                                        } else {
                                            vicePresidentName.setText(R.string.party_details_no_available);
                                        }
                                    }
                                });

                            } else {
                                // TODO: handle, something went wrong
                            }
                        }
                    });
                } else {
                    // TODO: handle error here
                }
            }
        });

        // listeners
        expandableGeneral.setExpandableListener(new ExpandableLayout.ExpandableListener() {
            @Override
            public void onAnimate(boolean isExpanded) {
                generalArrow.setImageResource(isExpanded ?
                        R.drawable.ic_hardware_keyboard_arrow_up : R.drawable.ic_hardware_keyboard_arrow_down);
            }
        });

        expandableTransparency.setExpandableListener(new ExpandableLayout.ExpandableListener() {
            @Override
            public void onAnimate(boolean isExpanded) {
                transparencyArrow.setImageResource(isExpanded ?
                        R.drawable.ic_hardware_keyboard_arrow_up : R.drawable.ic_hardware_keyboard_arrow_down);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void showNavigationBack() {
        // TODO: change drawer icon
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
