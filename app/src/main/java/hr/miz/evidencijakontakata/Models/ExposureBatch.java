package hr.miz.evidencijakontakata.Models;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExposureBatch {
    private static final String ID_REGEX = "([a-z]+[-][0-9]{4}[-][0-9]{2}[-][0-9]{2})";//find this format: <any lowercase word>-<any 4 digit number>-<any 2 digit number>-<any 2 digit number>
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
            Matcher matcher = Pattern.compile(ID_REGEX).matcher(url.toLowerCase());
            return matcher.find() ? matcher.group(1) : "";
        } catch (Exception e) {
            return "";
        }
    }

    public Queue<String> getQueue() {
        return new LinkedList<>(urls);
    }
}
