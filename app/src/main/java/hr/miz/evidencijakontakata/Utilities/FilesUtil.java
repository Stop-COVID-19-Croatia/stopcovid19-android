package hr.miz.evidencijakontakata.Utilities;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FilesUtil {
    public static File saveFile(File file, InputStream input) {
        int count = 0;
        try {
            OutputStream output = new FileOutputStream(file);
            byte[] data = new byte[1024];

            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            return file;
        } catch (Exception e) {
            Log.d("", "");
        }
        return null;
    }
}
