package com.crealoya.votopilas.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crealoya.votopilas.R;
import com.crealoya.votopilas.adapter.EducationListAdapter;
import com.crealoya.votopilas.adapter.LaborExperienceListAdapter;
import com.crealoya.votopilas.adapter.PoliticalExperienceListAdapter;
import com.crealoya.votopilas.adapter.PoliticianRatingListAdapter;
import com.crealoya.votopilas.model.Education;
import com.crealoya.votopilas.model.LaborExperience;
import com.crealoya.votopilas.model.PoliticalExperience;
import com.crealoya.votopilas.model.Politician;
import com.crealoya.votopilas.model.Rating;
import com.crealoya.votopilas.network.ParseCloudHelper;
import com.crealoya.votopilas.ui.view.NestedListView;
import com.crealoya.votopilas.util.StringManager;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PoliticianActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String EXTRA_POLITICIAN = "extra.politician";
    private Politician mPolitician;
    private RecyclerView mainList;
    private Context mContext;
    private ListView mPoliticalExperienceList;
    private ListView mLaborExperienceList;
    private ListView mEducationList;
    private ListView mRatingList;
    private RatingBar mRatingBar;
    private TextView mPartyChange;
    private boolean mPoliticalExperienceExpaned, mLaboralExperienceExpaned, mEducationExpaned;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politician);
        mContext = this;
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPartyChange = (TextView) findViewById(R.id.partyChange);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        Object obj = getIntent().getParcelableExtra(Politician.PARSE_POLITICIAN_OBJECT);

        mPoliticalExperienceList = (NestedListView) findViewById(R.id.political_experience_list);
        mLaborExperienceList = (NestedListView) findViewById(R.id.labor_experience_list);
        mEducationList = (NestedListView) findViewById(R.id.education_list);

        findViewById(R.id.show_rating).setOnClickListener(this);

        if (obj instanceof Politician) {
            mPolitician  = (Politician) obj;

            ImageView picture = (ImageView) findViewById(R.id.politician_photo);
            TextView charge = (TextView) findViewById(R.id.charge);
            TextView list = (TextView) findViewById(R.id.list);
            mRatingBar = (RatingBar) findViewById(R.id.rating_politician);

            collapsingToolbar.setTitle((mPolitician.listPosition != null ? mPolitician.listPosition.position : "") + ". " + StringManager.getFullName(mPolitician));
            if (mPolitician.listPosition.alternate){
                String alternate = mContext.getString(R.string.alternate);
                charge.setText(mPolitician.listPosition != null && mPolitician.listPosition.charge != null ? mPolitician.listPosition.charge.name  + " " + alternate : "");
            } else {
                charge.setText(mPolitician.listPosition != null && mPolitician.listPosition.charge != null ? mPolitician.listPosition.charge.name : "");
            }

            list.setText(mPolitician.listPosition != null && mPolitician.listPosition.list != null ? mPolitician.listPosition.list.name : "");

            if (mPolitician.image != null) {
                Glide.with(this).load(mPolitician.image).into(picture);
            }

            ParseQuery<Politician> politicianParseQuery = ParseQuery.getQuery(Politician.class);
            politicianParseQuery.getInBackground(mPolitician.objectId, new GetCallback<Politician>() {
                @Override
                public void done(final Politician politician, ParseException e) {

                    ParseRelation<PoliticalExperience> relation =  politician.getRelation(Politician.COL_POLITICAL_EXP);


                    relation.getQuery().findInBackground(new FindCallback<PoliticalExperience>() {
                        @Override
                        public void done(List<PoliticalExperience> list, ParseException e) {
                            if (e == null && list.size() > 0) {
                                findViewById(R.id.expandable_header_political_experience).setVisibility(View.VISIBLE);
                                mPoliticalExperienceList.setVisibility(View.VISIBLE);

                                mPoliticalExperienceList.setAdapter(new PoliticalExperienceListAdapter(mContext, (ArrayList<PoliticalExperience>) list));
                                final int size = list.size();
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int position = 0; position < size; position++) {
                                    final PoliticalExperience experience = list.get(position);
                                    experience.init();

                                    if (experience.partyChange) {
                                        stringBuilder.append(position + 1 == size ? experience.party : experience.party + ", ");
                                    }
                                }

                                if (stringBuilder.length() > 0) {
                                    findViewById(R.id.partyChangeLabel).setVisibility(View.VISIBLE);
                                    mPartyChange.setVisibility(View.VISIBLE);
                                }

                                mPartyChange.setText(stringBuilder.toString());
                            } else {

                            }
                        }
                    });

                    try {
                        ParseRelation<LaborExperience> relationLabor =  politician.getRelation(Politician.COL_LABOR_EXP);
                        ArrayList<LaborExperience> laborList =  (ArrayList<LaborExperience>) relationLabor.getQuery().find();
                        if (laborList.size() > 0) {
                            findViewById(R.id.expandable_header_laboral_experience).setVisibility(View.VISIBLE);
                            mLaborExperienceList.setVisibility(View.VISIBLE);
                            mLaborExperienceList.setAdapter(
                                    new LaborExperienceListAdapter(mContext, laborList));
                        }
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        ParseRelation<Education> relationEduc =  politician.getRelation(Politician.COL_EDUCATION);
                        ArrayList<Education> educationList =  (ArrayList<Education>) relationEduc.getQuery().find();
                        if (educationList.size() > 0) {
                            findViewById(R.id.expandable_header_education).setVisibility(View.VISIBLE);
                            mEducationList.setVisibility(View.VISIBLE);
                            mEducationList.setAdapter(
                                    new EducationListAdapter(mContext, (ArrayList<Education>) relationEduc.getQuery().find()));
                        }
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            loadRating();
        } else {
            finish();
        }
    }

    private void loadRating() {
        //load rating
        ParseQuery<Rating> query = ParseQuery.getQuery(Rating.class);
        query.whereEqualTo("politician", ParseObject.createWithoutData("Politician", mPolitician.objectId));
        query.include("user");
        query.include("politician");
        query.findInBackground(new FindCallback<Rating>() {
            @Override
            public void done(List<Rating> list, ParseException e) {
                if (e == null) {
                    mRatingList = (NestedListView) findViewById(R.id.rating_list);
                    mRatingList.setAdapter(
                            new PoliticianRatingListAdapter(mContext, new ArrayList<>(list)));

                }
            }
        });
        ParseCloudHelper.getRatingByUsersAvg(mPolitician, mRatingBar, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadRating();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_rating:
                if (ParseUser.getCurrentUser() == null) {
                    Intent intent = new Intent(PoliticianActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(PoliticianActivity.this, RatingActivity.class);
                    intent.putExtra("PoliticianId", mPolitician.objectId);
                    startActivityForResult(intent, 100);
                }
                break;

        }
    }
}
