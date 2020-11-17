package hr.miz.evidencijakontakata.Models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import hr.miz.evidencijakontakata.Utilities.StorageUtils;

public class ExposureBatchCollection {
    private static final String keyCheckedBatches = "CheckedBatches";

    private ArrayList<String> checkedBatches = new ArrayList<>();
    private ArrayList<ExposureBatch> batches = new ArrayList<>();

    public ExposureBatchCollection(ArrayList<String> urls) {
        loadCheckedBatches();

        for (String url : urls) {
            if(!checkedBatches.contains(ExposureBatch.idFromUrl(url))) {
                ExposureBatch batch = getBatchForUrl(url);
                if (batch != null) {
                    batch.addUnique(url);
                } else {
                    batches.add(new ExposureBatch(url));
                }
            }
        }
    }

    private ExposureBatch getBatchForUrl(String url) {
        for (ExposureBatch batch : batches) {
            if(batch.equals(url)) {
                return batch;
            }
        }

        return null;
    }

    public Queue<ExposureBatch> getQueue() {
        return new LinkedList<>(batches);
    }

    private void loadCheckedBatches() {
        ExposureBatchCollection loaded = (ExposureBatchCollection) StorageUtils.getObject(keyCheckedBatches, ExposureBatchCollection.class);
        this.checkedBatches = loaded != null ? loaded.checkedBatches : new ArrayList<>();
    }

    public void addNewCheckedBatch(String batchId) {
        this.checkedBatches.add(batchId);
        StorageUtils.saveObject(keyCheckedBatches, this);
    }
}
