package top.thsunkist.brainhealthy.utilities;

import android.content.Context;
import android.os.Environment;

import top.thsunkist.brainhealthy.BrainhealthyApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileManager {

    public static void profileExternalFiles() {
        final File root = BrainhealthyApplication.CONTEXT.getExternalFilesDir("");
        final ArrayList<File> queue = new ArrayList<>();
        queue.add(root);

        long fileSizeTotal = 0;

        while (!queue.isEmpty()) {
            final File curr = queue.remove(queue.size() - 1);
            if (curr.isDirectory()) {
                /* A folder */
                for (File f : curr.listFiles()) {
                    queue.add(f);
                }
            } else {
                /* A Single File */
                fileSizeTotal += curr.length();
            }
        }
    }

    /**
     * Temporary file for uploading. 临时文件，专门用来上传.
     *
     * @param suffix optional name suffix
     * @return temp file
     */
    public static File getTempImageInputFile(final String suffix) {
        return new File(BrainhealthyApplication.CONTEXT.getCacheDir().getAbsoluteFile() + File.separator + "temp" + suffix + ".jpg");
    }

    public static File getTempInputFile(final String suffix) {
        return new File(BrainhealthyApplication.CONTEXT.getCacheDir().getAbsoluteFile() + File.separator + "temp" + suffix + ".log");
    }

    /**
     * Temporary file for uploading. 临时文件，专门用来上传.
     *
     * @return temp file
     */
    public static File getTempImageInputFile() {
        return getTempImageInputFile("");
    }

    /**
     * 解压缩.
     *
     * @param zipFile         压缩包
     * @param targetDirectory 解压缩的目标位置
     * @throws IOException
     */
    public static void unzip(File zipFile, File targetDirectory, boolean deleteZip) throws IOException {
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                if (ze.isDirectory()) continue;
                FileOutputStream outFile = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1) outFile.write(buffer, 0, count);
                } finally {
                    outFile.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }
        targetDirectory.setLastModified(System.currentTimeMillis());
        if (deleteZip) {
            zipFile.delete();
        }
    }

    public static File getDownloadFolderImageOutputFile(final String suffix) {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile() + File.separator + suffix + ".jpg");
    }

    public static File getDownloadFolderOutputFile(final String suffix) {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile() + File.separator + suffix + ".log");
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static long getDirSize(File dir) {
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception ignored) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
