package com.crealoya.votopilas.network;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crealoya.votopilas.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

/**
 * Created by @pablopantaleon on 7/26/15.
 */
public class LoginHelper {

    /**
     * Facebook Permissions
     */
    private static final List<String> permissions = Arrays.asList("public_profile", "email", "user_friends");

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOGIN_WITH_CANCEL, LOGIN_WITH_OMIT})
    public @interface LoginType {}

    public static final int LOGIN_WITH_CANCEL = 100;
    public static final int LOGIN_WITH_OMIT = 101;

    public static void showLogInDialog(final Context context, @LoginType final int type, @Nullable final ProgressBar progressBar,
                                       final LoginCallback loginCallback) {
        final String cancelText = type == LOGIN_WITH_CANCEL ? context.getString(R.string.cancel) : context.getString(R.string.login_omit);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.login_dialog_title)
                .setMessage(R.string.login_dialog_message)
                .setPositiveButton(R.string.login_with_fb, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        logIn(context, type, progressBar, loginCallback);
                    }
                })
                .setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loginCallback.onLoginCancel();
                    }
                })
                .create();

        dialog.show();
    }

    public static void showLogInDialog(final Context context, @LoginType int type, final LoginCallback loginCallback) {
        showLogInDialog(context, type, null, loginCallback);
    }

    public static void logIn(final Context context, final @LoginType int type, final ProgressBar progressBar,
                             final LoginCallback loginCallback) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        AppCompatActivity activity;

        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
        } else {
            return;
        }

        if (ParseUser.getCurrentUser() != null) {
            loginCallback.onLogin(true);
            return;
        }

        ParseFacebookUtils.logIn(permissions, activity, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }

                    Toast.makeText(context, R.string.login_dialog_try_again, Toast.LENGTH_SHORT).show();
                    loginCallback.onLogin(false);
                } else {
                    // TODO; need to support Parse with the new FB library
                    Request.executeMeRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser graphUser, Response response) {
                            ParseUser parseUser = ParseUser.getCurrentUser();

                            final String email = (String) response.getGraphObject().getProperty("email");

                            if (email != null) {
                                parseUser.setEmail(email);
                            } else {
                                parseUser.setEmail(graphUser.getId() + "@facebook.com");
                            }

                            if (graphUser.getUsername() != null) {
                                parseUser.setUsername(graphUser.getUsername());
                            }
                            parseUser.put("vip", false);
                            parseUser.put("name", graphUser.getName());
                            parseUser.put("fbId", graphUser.getId());
                            parseUser.saveEventually();
                            parseUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (progressBar != null) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                            loginCallback.onLogin(true);
                        }
                    });
                }
            }
        });
    }

    public interface LoginCallback {
        void onLogin(boolean success);

        void onLoginCancel();
    }
}
