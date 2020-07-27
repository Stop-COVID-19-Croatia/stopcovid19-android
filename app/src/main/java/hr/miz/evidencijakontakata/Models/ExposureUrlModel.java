package hr.miz.evidencijakontakata.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import hr.miz.evidencijakontakata.Utilities.StorageUtils;

public class ExposureUrlModel {
    private static final String keyUrlList = "urlList";
    private static DownloadedList dlist;
    public ExposureUrlModel() {
        getList();
    }

    @SerializedName(keyUrlList)
    public ArrayList<String> urlList = new ArrayList<>();

    public Queue<String> getUrlQueue(boolean downloadAll) {
        ArrayList<String> newUrls = new ArrayList<>();
        for (String url: urlList) {
            if (!newUrls.contains(url)){
                newUrls.add(url);
            }
        }

        if(!downloadAll) {
            ArrayList<String> cleanedList = new ArrayList<>(newUrls);
            newUrls.clear();
            for (String url : cleanedList) {
                if(!dlist.listOfDownloads.contains(url)) {
                    dlist.listOfDownloads.add(url);
                    newUrls.add(url);
                }
            }
        }

        return new LinkedList<>(newUrls);
    }

    /*public Queue<String> getUrlQueue(boolean downloadAll) {
        ArrayList<String> cleanedList = new ArrayList<>();
        for (String url : urlList) {
            if(downloadAll || !dlist.listOfDownloads.contains(url)) {
                if(!cleanedList.contains(url)) {
                    cleanedList.add(url);
                    if (!downloadAll) {
                        dlist.listOfDownloads.add(url);
                    }
                }
            }
        }
        return new LinkedList<>(cleanedList);
    }*/

    public static void saveList() {
        StorageUtils.saveObject(keyUrlList, dlist);
    }

    private void getList() {
        dlist = (DownloadedList) StorageUtils.getObject(keyUrlList, DownloadedList.class);
        dlist = dlist != null ? dlist : new DownloadedList();
    }

    private class DownloadedList implements Serializable {
        private ArrayList<String> listOfDownloads = new ArrayList<>();
    }
}
