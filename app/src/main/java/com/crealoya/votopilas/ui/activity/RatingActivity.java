package com.crealoya.votopilas.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.model.Rating;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class RatingActivity extends BaseActivity {

    private RatingBar mRatingValue;
    private EditText mRatingMessage;
    private ParseObject mRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.create(savedInstanceState, R.layout.activity_rating);
        showNavigationBack();

        getSupportActionBar().setTitle("Tu Valoración");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mRatingValue = (RatingBar) findViewById(R.id.rating_value);
        mRatingMessage = (EditText) findViewById(R.id.rating_message);
        final String politicianId = getIntent().getStringExtra("PoliticianId");

        ParseQuery<Rating> query = ParseQuery.getQuery(Rating.class);
        query.whereEqualTo("politician", ParseObject.createWithoutData("Politician", politicianId));
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        try {
            mRating = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (mRating == null) {
            mRating = ParseObject.create("Rating");
            mRating.put("politician", ParseObject.createWithoutData("Politician", politicianId));
            mRating.put("user", ParseUser.getCurrentUser());
        } else {
            mRatingValue.setRating(mRating.getInt("value"));
            mRatingMessage.setText(mRating.getString("message"));
        }

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRatingValue.getRating() > 0) {
                    mRating.put("message", mRatingMessage.getText().toString());
                    mRating.put("value", mRatingValue.getRating());
                    try {
                        mRating.save();
                        Toast.makeText(RatingActivity.this, "Rating guardado!", Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(RatingActivity.this, "Error al intentar guardar", Toast.LENGTH_SHORT).show();
                    }
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(RatingActivity.this, "Rating requerido!", Toast.LENGTH_SHORT).show();
                }
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
}
