package hr.miz.evidencijakontakata.Models;

import com.google.android.gms.nearby.exposurenotification.ExposureSummary;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import hr.miz.evidencijakontakata.Utilities.ExposureNotifications.ExposureNotificationWrapper;
import hr.miz.evidencijakontakata.Utilities.StorageUtils;

public class ExposureSummaryCollection {

    private static final String keySummaryCollection = "SummaryCollection";
    private static ExposureSummaryCollection instance;
    private ArrayList<String> exposureTokens = new ArrayList<>();
    private ArrayList<ExposureSummaryModel> summaries = new ArrayList<>();

    public static ExposureSummaryCollection getInstance() {
        if(instance == null) {
            instance = StorageUtils.getObject(keySummaryCollection, ExposureSummaryCollection.class);
        }

        if(instance == null) {
            instance = new ExposureSummaryCollection();
        }

        return instance;
    }

    public void loadSummaries(ISummaryLoaderListener iSummaryLoaderListener) {
        removeOldSummaries();
        if (exposureTokens != null && !exposureTokens.isEmpty()) {
            loadTokenQueue(new LinkedList<>(exposureTokens), iSummaryLoaderListener);
        } else if (iSummaryLoaderListener != null) {
            runListCleaner();
            iSummaryLoaderListener.onSummariesLoaded();
        }
    }

    private void loadTokenQueue(Queue<String> tokens, ISummaryLoaderListener iSummaryLoaderListener) {
        String token = tokens.poll();
        if(token != null) {
            ExposureNotificationWrapper.get().getExposureSummary(token)
                    .addOnSuccessListener(exposureSummary -> {
                        addExposureSummary(exposureSummary, token);
                        exposureTokens.remove(token);
                    })
                    .addOnCompleteListener(task -> loadTokenQueue(tokens, iSummaryLoaderListener));
        } else if(iSummaryLoaderListener != null){
            runListCleaner();
            iSummaryLoaderListener.onSummariesLoaded();
        }
    }

    public void runListCleaner() {
        for (int i = summaries.size()-1; i >= 0; i--) {
            if(!summaries.get(i).isValid()) {
                summaries.remove(i);
            }
        }
        StorageUtils.saveObject(keySummaryCollection, this);
    }

    private void removeOldSummaries() {
        for (int i = summaries.size()-1; i >= 0; i--) {
            if(summaries.get(i).token == null || summaries.get(i).token.isEmpty()) {
                summaries.remove(i);
            }
        }
    }

    public void addExposureSummary(ExposureSummary exposureSummary, String token) {
        if(exposureSummary.getDaysSinceLastExposure() <= 14) {
            addSummary(new ExposureSummaryModel(exposureSummary, token));
        } else {
            removeSummary(token);
        }
    }

    void addSummary(ExposureSummaryModel summary) {
        if(summaries == null) {
            summaries = new ArrayList<>();
        }

        if (!summaries.contains(summary)) {
            summaries.add(summary);
            StorageUtils.saveObject(keySummaryCollection, this);
        }
    }

    private void removeSummary(String token) {
        for (ExposureSummaryModel exposureSummary : summaries) {
            if(exposureSummary.equals(token)) {
                summaries.remove(exposureSummary);
                StorageUtils.saveObject(keySummaryCollection, this);
                break;
            }
        }
    }

    //Call only after loadCollection was called
    public ExposureSummaryModel getRiskiestSummaryModel() {
        if(summaries != null && !summaries.isEmpty()) {
            ExposureSummaryModel model = summaries.get(0);
            for (ExposureSummaryModel expModel : summaries) {
                if(model.summationRiskScore < expModel.summationRiskScore) {
                    model = expModel;
                }
            }

            return model;
        }

        return null;
    }

    public interface ISummaryLoaderListener {
        void onSummariesLoaded();
    }
}
