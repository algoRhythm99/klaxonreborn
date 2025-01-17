package com.github.android.klaxonreborn;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;

/**
 * Partial implementation of a PageReceiver.
 * covers some of the basics, like ensuring that the user is currently oncall
 */
//TODO: import comments from IPageReceiver.
public abstract class PageReceiver extends BroadcastReceiver {

    //XXX: use getter/setter here?
    public static String MY_TRANSPORT;

    /**
     * called when the registered intent is received.
     * this method should handle extracting the relevant data from the
     * transport mechanism, and turning into the appropriate form and insert
     * into the PagerProvider
     */
    public abstract void onReceive(Context context, Intent intent);

    /**
     * Determine if the recevied message meets the user's criteria for a page.
     * this usually involves checking preferences.
     */
    public boolean isPage(ContentValues cv, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("is_oncall", true);
    }

    /**
     * check if we can reply to this page.
     * this is typically done by checking the transport mechanism, as stored in
     * the PagerProvider
     */
    public boolean canReply(Context context, Uri data) {
        Cursor cursor = context.getContentResolver().query(data,
                new String[]{Pager.Pages.TRANSPORT, Pager.Pages._ID},
                null,
                null,
                null);
        cursor.moveToFirst();

        String transport = cursor.getString(cursor.getColumnIndex(Pager.Pages.TRANSPORT));
        if (transport.equals(MY_TRANSPORT)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * replies to a particular message, specified by Uri.
     * this must be overridden by PageReceiver subclasses.
     */
    public abstract void replyTo(Context context, Uri data, String reply, int ack_status);

}


