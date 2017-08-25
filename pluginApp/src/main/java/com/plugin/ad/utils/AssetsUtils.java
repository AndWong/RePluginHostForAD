package com.plugin.ad.utils;

import android.content.Context;
import android.util.Log;

/**
 * Created by wong on 17-6-15.
 */

public class AssetsUtils {
    /**
     * read file content
     *
     * @param context   the context
     * @param assetPath the asset path
     * @return String string
     */
    public static String readText(Context context, String assetPath) {
        try {
            return ConvertUtils.toString(context.getAssets().open(assetPath));
        } catch (Exception e) {
            Log.e("AssetsUtils", e.getMessage());
            return "";
        }
    }
}