package com.samples.flironecamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.flir.thermalsdk.image.ThermalImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import static android.content.ContentValues.TAG;

public class FileStoringMethods {

    public static boolean isWriting;

    public static String sessionTag;
    public static int recordingNumber;

    public void storeThermalImage(ThermalImage image, Context context) {
        String typeTag = "Thermal";
        File pictureFile = getOutputMediaFile(context, typeTag);
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            image.saveAs(String.valueOf(pictureFile));
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

    }

    public void storeBitmapImage(Bitmap image, Context context) {
        String typeTag = "Bitmap";
        File pictureFile = getOutputMediaFile(context, typeTag);
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    public File generateStoragePath(Context context) {

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());
        String tag = timeStamp + "_" + sessionTag.toString() + "_rec_" + String.valueOf(recordingNumber);
        return new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getPackageName()
                + "/Files/" + tag + "/");
    }

    /** Create a File for saving an image or video */
    public File getOutputMediaFile(Context context, String typeTag){
        //To be safe, you should check that the SDCard is mounted
        //using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = generateStoragePath(context);
        //File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/ThermalImages/");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        //dString timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmssSSS").format(new Date());
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp + "_" + typeTag + ".png";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    // rewrite framecounter and upgrade frame  into this class
}
