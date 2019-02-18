package zeman.diktafonmev.Helpers;

import android.os.Environment;
import android.os.StatFs;

/**
 * Created by Nikola on 22.2.2016..
 */
public class FreeSpaceOnSD {


    /**
     * @return Number of bytes available on External storage
     */
    public static long getAvailableSpaceInBytes() {
        long availableSpace = -1L;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

        return availableSpace;
    }


    /**
     * @return Number of kilo bytes available on External storage
     */
    public static long getAvailableSpaceInKB() {
        final long SIZE_KB = 1024L;
        long availableSpace = -1L;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        return availableSpace / SIZE_KB;
    }

    /**
     * @return Number of Mega bytes available on External storage
     */
    public static long getAvailableSpaceInMB() {
        final long SIZE_KB = 1024L;
        final long SIZE_MB = SIZE_KB * SIZE_KB;
        long availableSpace = -1L;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        return availableSpace / SIZE_MB;
    }

    /**
     * @return Number of gega bytes available on External storage
     */
    public static long getAvailableSpaceInGB() {
        final long SIZE_KB = 1024L;
        final long SIZE_GB = SIZE_KB * SIZE_KB * SIZE_KB;
        long availableSpace = -1L;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        return availableSpace / SIZE_GB;
    }

    public static long getSizeInMB() {
        final long SIZE_KB = 1024L;
        final long SIZE_MB = SIZE_KB * SIZE_KB;
        long totalSpace = -1L;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        //availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        totalSpace = (long) stat.getBlockCount() * (long) stat.getBlockSize();

        return totalSpace / SIZE_MB;
    }


    public static long getSizeInGB() {
        final long SIZE_KB = 1024L;
        final long SIZE_GB = SIZE_KB * SIZE_KB * SIZE_KB;
        long totalSpace = -1L;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        //availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        totalSpace = (long) stat.getBlockCount() * (long) stat.getBlockSize();

        return totalSpace / SIZE_GB;
    }

}
