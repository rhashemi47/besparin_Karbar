package com.besparina.it.karbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by hashemi on 08/27/2018.
 */

public class ImageConvertor {
    public static Bitmap Base64ToBitmap(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String BitmapToBase64(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}
