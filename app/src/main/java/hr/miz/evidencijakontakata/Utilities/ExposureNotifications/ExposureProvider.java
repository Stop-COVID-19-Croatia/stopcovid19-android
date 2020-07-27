package hr.miz.evidencijakontakata.Utilities.ExposureNotifications;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Queue;

import hr.miz.evidencijakontakata.ErrorHandling.CustomError;
import hr.miz.evidencijakontakata.Models.ExposureBatch;
import hr.miz.evidencijakontakata.Models.ExposureBatchCollection;
import hr.miz.evidencijakontakata.Models.ExposureUrlModel;
import hr.miz.evidencijakontakata.Services.CallBackInterface.IResponseCallback;
import hr.miz.evidencijakontakata.Services.UserService;
import hr.miz.evidencijakontakata.Utilities.ZipUtil;
import okhttp3.ResponseBody;

class ExposureProvider {
    private ExposureBatchCollection batchCollection;
    private Queue<ExposureBatch> batches;
    private ArrayList<File> batchFiles = new ArrayList<>();
    private boolean downloadAll = false;

    private ExposureProvider() { }

    private void downloadAndProvide() {
        UserService.getUrls(new IResponseCallback<ExposureUrlModel>() {
            @Override
            public void onSuccess(ExposureUrlModel response) {
                batchCollection = new ExposureBatchCollection(response.urlList, downloadAll);
                batches = batchCollection.getQueue();
                if(batches.size() > 0) {
                    downloadNextBatch();
                }
            }

            @Override
            public void onError(CustomError exception) {

            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void downloadNextBatch() {
        ExposureBatch batch = batches.poll();
        if(batch != null) {
            batchFiles.clear();
            downloadBatch(batch.getQueue());
        }
    }

    private void downloadBatch(Queue<String> urlQueue) {
        String fileName = urlQueue.poll();
        if(fileName != null) {
            String finalFileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            UserService.getExposure(fileName, new IResponseCallback<ResponseBody>() {
                @Override
                public void onSuccess(ResponseBody response) {
                    File file = ZipUtil.getFileFromResponseBody(response,finalFileName);
                    batchFiles.add(file);
                    downloadBatch(urlQueue);
                }

                @Override
                public void onError(CustomError exception) {
                    downloadBatch(urlQueue);
                }
            });
        } else {
            ExposureNotificationWrapper.provideKeyFiles(batchFiles, o -> onFilesProvided(true, o), e -> onFilesProvided(false, e.toString()));
        }
    }

    private void onFilesProvided(boolean success, @Nullable String message) {
        if(success) {
            batchCollection.addNewCheckedBatch(message);
        }

        downloadNextBatch();
    }

    static void initCheck() {
        new ExposureProvider().downloadAndProvide();
    }
}
