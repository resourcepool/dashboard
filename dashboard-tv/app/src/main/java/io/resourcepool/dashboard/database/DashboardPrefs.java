package io.resourcepool.dashboard.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by loicortola on 04/03/2017.
 */
public class DashboardPrefs {
    private static final String PREFS_KEY = "io.resourcepool.dashboard.PREFS_KEY";
    private static final String PREF_SERVER = "server";
    private static final String PREF_FEED = "feedId";
    private static final String PREF_REVISION = "revision";

    public static void saveServerHost(Context ctx, String host) {
        SharedPreferences p = ctx.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        p.edit().putString(PREF_SERVER, host).apply();
    }

    public static String getServerHost(Context ctx) {
        return ctx.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE).getString(PREF_SERVER, null);
    }

    public static void clearServerHost(Context ctx) {
        SharedPreferences p = ctx.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        p.edit().remove(PREF_SERVER).apply();
    }

    public static void saveFeed(Context ctx, String feedId) {
        SharedPreferences p = ctx.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        p.edit().putString(PREF_FEED, feedId).apply();
    }

    public static String getFeed(Context ctx) {
        return ctx.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE).getString(PREF_FEED, null);
    }

    public static void saveRevision(Context ctx, long revision) {
        SharedPreferences p = ctx.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        p.edit().putLong(PREF_REVISION, revision).apply();
    }

    public static long getRevision(Context ctx) {
        return ctx.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE).getLong(PREF_REVISION, 0);
    }

}
