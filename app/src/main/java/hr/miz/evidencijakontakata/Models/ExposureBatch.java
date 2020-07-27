package hr.miz.evidencijakontakata.Models;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ExposureBatch {
    private String batchId;
    private ArrayList<String> urls = new ArrayList<>();

    ExposureBatch(String url) {
        this.batchId = idFromUrl(url);
        urls.add(url);
    }

    void addUnique(String url) {
        if(!urls.contains(url)) {
            urls.add(url);
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof ExposureBatch) {
            return ((ExposureBatch) obj).batchId.equals(this.batchId);
        } else if(obj instanceof String) {
            return idFromUrl((String) obj).equals(batchId);
        }

        return false;
    }

    public static String idFromUrl(String url) {
        try {
            return url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('-'));
        } catch (Exception e) {
            return "";
        }
    }

    public Queue<String> getQueue() {
        return new LinkedList<>(urls);
    }
}
