package hr.miz.evidencijakontakata.Models;

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
            instance = new ExposureSummaryCollection();
        }

        return instance;
    }

    void addNewSummary(String token, ExposureSummaryModel summary) {
        ExposureSummaryCollection tmp = ((ExposureSummaryCollection) StorageUtils.getObject(keySummaryCollection, ExposureSummaryCollection.class));
        if (tmp != null && tmp.exposureTokens != null) {
            exposureTokens = tmp.exposureTokens;
        }

        if (!exposureTokens.contains(token)) {
            exposureTokens.add(token);
            summaries.add(summary);
            StorageUtils.saveObject(keySummaryCollection, this);
        }
    }

    private void removeToken(String token) {
        exposureTokens.remove(token);
        StorageUtils.saveObject(keySummaryCollection, this);
    }

    public void loadCollection(ISummaryLoaderListener iSummaryLoaderListener) {
        ExposureSummaryCollection summaryCollection = (ExposureSummaryCollection) StorageUtils.getObject(keySummaryCollection, ExposureSummaryCollection.class);
        if (summaryCollection != null && summaryCollection.exposureTokens != null) {
            loadTokenQueue(new LinkedList<>(summaryCollection.exposureTokens), iSummaryLoaderListener);
        } else if(iSummaryLoaderListener != null){
            iSummaryLoaderListener.onSummariesLoaded();
        }
    }

    private void loadTokenQueue(Queue<String> tokens, ISummaryLoaderListener iSummaryLoaderListener) {
        String token = tokens.poll();
        if(token != null) {
            ExposureNotificationWrapper.get().getExposureSummary(token)
                    .addOnSuccessListener(exposureSummary -> summaries.add(new ExposureSummaryModel(exposureSummary)))
                    .addOnFailureListener(e -> removeToken(token))
                    .addOnCompleteListener(task -> loadTokenQueue(tokens, iSummaryLoaderListener));
        } else if(iSummaryLoaderListener != null){
            iSummaryLoaderListener.onSummariesLoaded();
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
