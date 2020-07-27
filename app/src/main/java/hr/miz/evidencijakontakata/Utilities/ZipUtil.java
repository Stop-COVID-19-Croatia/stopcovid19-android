package hr.miz.evidencijakontakata.Utilities;

import hr.miz.evidencijakontakata.CroatiaExposureNotificationApp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import okhttp3.ResponseBody;

public class ZipUtil {
    private static final Integer DEFAULT_BUFFER = 1024;
    private static final String EXPOSURES_PATH = CroatiaExposureNotificationApp.getInstance().getFilesDir() + "/exposures/";

    public static File unzip(ResponseBody response, String zipName) {
        File archiveFile = getFileFromResponseBody(response, zipName);
        if(archiveFile != null) {
            return unzip(archiveFile.getPath());
        }

        return null;
    }

    public static File getFileFromResponseBody(ResponseBody response, String zipName) {
        return FilesUtil.saveFile(new File(CroatiaExposureNotificationApp.getInstance().getCacheDir(), zipName), response.byteStream());
    }

    private static File unzip(String zipPath) {
        File destinationDir = new File(EXPOSURES_PATH + zipPath.substring(0, zipPath.lastIndexOf('.')));

        ZipFile zip = null;
        try {
            destinationDir.mkdirs();
            zip = new ZipFile(zipPath);

            Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

            while (zipFileEntries.hasMoreElements()) {
                ZipEntry entry = zipFileEntries.nextElement();
                String entryName = entry.getName();
                entryName = entryName.replace("\\", "/");
                File destFile = new File(destinationDir, entryName);
                File destinationParent = destFile.getParentFile();
                if (destinationParent != null && !destinationParent.exists()) {
                    destinationParent.mkdirs();
                }
                if (!entry.isDirectory()) {
                    BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
                    int currentByte;
                    byte[] data = new byte[DEFAULT_BUFFER];
                    FileOutputStream fos = new FileOutputStream(destFile);
                    BufferedOutputStream dest = new BufferedOutputStream(fos, DEFAULT_BUFFER);
                    while ((currentByte = is.read(data, 0, DEFAULT_BUFFER)) > 0) {
                        dest.write(data, 0, currentByte);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException ignored) {
                }
            }
        }
        return destinationDir;
    }
}
