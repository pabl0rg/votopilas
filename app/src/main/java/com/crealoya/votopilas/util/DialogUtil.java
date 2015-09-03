package com.crealoya.votopilas.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IntDef;
import android.support.v7.app.AlertDialog;

import com.crealoya.votopilas.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by @pablopantaleon on 7/28/15.
 */
public class DialogUtil {

    @IntDef({HIDE_LIST, HIDE_PARTY, UNHIDE_PARTIES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogState {}

    public static final int HIDE_LIST = 100;
    public static final int HIDE_PARTY = 101;
    public static final int UNHIDE_PARTIES = 102;

    public static void simpleDialog(Context context, @DialogState int state, final DialogListener listener) {
        String title;
        String message;

        if (state == HIDE_LIST) {
            title = context.getString(R.string.dlg_hide_list_title);
            message = String.format(context.getString(R.string.dlg_hide_msg), "listado");
        } else if (state == UNHIDE_PARTIES) {
            title = context.getString(R.string.dlg_unhide_list_title);
            message = "";
        } else {
            title = context.getString(R.string.dlg_hide_party_title);
            message = String.format(context.getString(R.string.dlg_hide_msg), "partidos");
        }

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onClose(true);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onClose(false);
                    }
                }).create().show();
    }

    public interface DialogListener {
        void onClose(boolean isAccepted);
    }
}
