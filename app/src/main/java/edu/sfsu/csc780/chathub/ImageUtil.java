package edu.sfsu.csc780.chathub;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cjkriese on 3/2/18.
 */

public class ImageUtil {
    private static final String TAG = "ImageUtil";
    public static final double MAX_LINEAR_DIMENSION = 200.0;
    public static final String IMAGE_FILE_NAME_PREFIX = "chathub-";

    public static Bitmap getBitmapForUri(Uri imageUri, Context context) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap scaleImage(Bitmap bitmap) {
        int originalHeight = bitmap.getHeight();
        int originalWidth = bitmap.getWidth();
        double scaleFactor = MAX_LINEAR_DIMENSION / (double)(originalHeight + originalWidth);
        // We only want to scale down images, not scale upwards
        if (scaleFactor < 1.0) {
            int targetWidth = (int) Math.round(originalWidth * scaleFactor);
            int targetHeight = (int) Math.round(originalHeight * scaleFactor);
            return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
        } else {
            return bitmap;
        }
    }

    public static Uri savePhotoImage(Context context, Bitmap imageBitmap) {
        File photoFile = null;
        try {
            photoFile = createImageFile(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (photoFile == null) {
            Log.d(TAG, "Error creating media file");
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(photoFile);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        return Uri.fromFile(photoFile);
    }

    static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String imageFileNamePrefix = IMAGE_FILE_NAME_PREFIX + timeStamp;
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileNamePrefix, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        return imageFile;
    }
}
