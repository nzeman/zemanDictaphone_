package zeman.diktafonmev.Helpers;

import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by praktikant on 1.3.2016..
 */
public class FolderCreation {

    public static boolean CreateDateFolder() {


        Date datum = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //hh-mm-ss__dd-MM-yyyy
        String DateToStr = format.format(datum);


        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "ZemanRecording" + File.separator + DateToStr);
        if (directory.exists()) {

            //Toast.makeText(getApplicationContext(), Environment.getExternalStorageDirectory() + File.separator + "ZemanRecording" + File.separator + DateToStr +
            //        "FOLDER VEC POSTOJI", Toast.LENGTH_SHORT).show();
        } else {
            directory.mkdirs();

            //Toast.makeText(getApplicationContext(), "Napravljen folder za svoj datum!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}


/*
        // NAPOMENA
        // secondary_storage je dodatna eksterna sd kartica koja se umeće u mobitel
        // external_sdcard_storage je internal/unutarnja memorija mobitela koju ne možemo izvaditi


        String strSDCardPath = System.getenv("SECONDARY_STORAGE");

        if ((strSDCardPath == null) || (strSDCardPath.length() == 0)) {
            strSDCardPath = System.getenv("EXTERNAL_SDCARD_STORAGE");

            File externalFilePath = new File(strSDCardPath + File.separator + "ZemanRecording");


            boolean success = true;
            if (!externalFilePath.exists()) {
                success = externalFilePath.mkdirs();
            }
            if (success) {
                // Do something on success
                return true;

            } else {
                //Toast.makeText(getApplicationContext(), "Greška kod kreiranja foldera!", Toast.LENGTH_LONG).show();
                // Do something else on failure
                return false;
            }


        }
        return true;

    }
    */








        /*
        //If may get a full path that is not the right one, even if we don't have the SD Card there.
        //We just need the "/mnt/extSdCard/" i.e and check if it's writable
        if (strSDCardPath != null) {
            if (strSDCardPath.contains(":")) {
                strSDCardPath = strSDCardPath.substring(0, strSDCardPath.indexOf(":"));
            }
            File externalFilePath = new File(strSDCardPath);

            if (externalFilePath.exists() && externalFilePath.canWrite()) {
                //do what you need here
            }
        }

       */




    /*
    // WORKS GOOD
      public static boolean CreateDateFolder() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "ZemanRecording");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            // Do something on success
            return true;

        } else {
            //Toast.makeText(getApplicationContext(), "Greška kod kreiranja foldera!", Toast.LENGTH_LONG).show();
            // Do something else on failure
            return false;
        }


    }


     */



