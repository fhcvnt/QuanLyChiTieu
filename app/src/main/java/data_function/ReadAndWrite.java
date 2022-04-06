package data_function;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

import java.io.File;

public class ReadAndWrite {
    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;
    private Context context;
    private Activity activity;

    public ReadAndWrite(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // With Android Level >= 23, you have to ask the user
    // for permission with device (For example read/write data on the device).
    public boolean askPermission(int requestId, String permissionName) {

        // Log.i(LOG_TAG, "Ask for Permission: " + permissionName);
        // Log.i(LOG_TAG, "Build.VERSION.SDK_INT: " + android.os.Build.VERSION.SDK_INT);

        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            int permission = ActivityCompat.checkSelfPermission(context, permissionName);

            // Log.i(LOG_TAG, "permission: " + permission);
            //  Log.i(LOG_TAG, "PackageManager.PERMISSION_GRANTED: " + PackageManager.PERMISSION_GRANTED);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                requestPermissions(activity, new String[]{permissionName}, requestId);
                return false;
            }
        }
        return true;
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean askPermissionAndWriteFile() {
        boolean canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!canWrite) {
            //Toast.makeText(context, "Bạn chưa được cấp quyền ghi tệp!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean askPermissionAndReadFile() {
        boolean canRead = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        //
        if (!canRead) {
            //Toast.makeText(context, "Bạn chưa được cấp quyền đọc tệp!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // As soon as the user decides, allows or doesn't allow.
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        //
//        // Note: If request is cancelled, the result arrays are empty.
//        if (grantResults.length > 0) {
//            switch (requestCode) {
//                case REQUEST_ID_READ_PERMISSION: {
//                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                        readFile();
//                    }
//                }
//                case REQUEST_ID_WRITE_PERMISSION: {
//                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                        writeFile();
//                    }
//                }
//            }
//        } else {
//            Toast.makeText(context, "Permission Cancelled!", Toast.LENGTH_SHORT).show();
//        }
//    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // IMPORTANT!!
    public File getAppExternalFilesDir() {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            // /storage/emulated/0/Android/data/org.o7planning.externalstoragedemo/files
            return context.getExternalFilesDir(null);
        } else {
            // @Deprecated in API 29.
            // /storage/emulated/0
            return Environment.getExternalStorageDirectory();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void listExternalStorages() {
        StringBuilder sb = new StringBuilder();

        sb.append("Data Directory: ").append("\n - ")
                .append(Environment.getDataDirectory().toString()).append("\n");

        sb.append("Download Cache Directory: ").append("\n - ")
                .append(Environment.getDownloadCacheDirectory().toString()).append("\n");

        sb.append("External Storage State: ").append("\n - ")
                .append(Environment.getExternalStorageState().toString()).append("\n");

        sb.append("External Storage Directory: ").append("\n - ")
                .append(Environment.getExternalStorageDirectory().toString()).append("\n");

        sb.append("Is External Storage Emulated?: ").append("\n - ")
                .append(Environment.isExternalStorageEmulated()).append("\n");

        sb.append("Is External Storage Removable?: ").append("\n - ")
                .append(Environment.isExternalStorageRemovable()).append("\n");

        sb.append("External Storage Public Directory (Music): ").append("\n - ")
                .append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString()).append("\n");

        sb.append("Download Cache Directory: ").append("\n - ")
                .append(Environment.getDownloadCacheDirectory().toString()).append("\n");

        sb.append("Root Directory: ").append("\n - ")
                .append(Environment.getRootDirectory().toString()).append("\n");

    }
}
