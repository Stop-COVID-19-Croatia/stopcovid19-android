package hr.miz.evidencijakontakata.Models;

import java.io.Serializable;
import java.util.ArrayList;

import hr.miz.evidencijakontakata.Utilities.StorageUtils;
import hr.miz.evidencijakontakata.Utilities.Util;

public class PositiveTestManager {
    private static final String keyPositiveTests = "keyPositiveTest";
    private static PositiveTestManager instance;

    private ArrayList<PositiveTest> tests = new ArrayList<>();

    public static PositiveTestManager get() {
        if(instance == null) {
            instance = (PositiveTestManager) StorageUtils.getObject(keyPositiveTests, PositiveTestManager.class);
        }

        if(instance == null) {
            instance = new PositiveTestManager();
        }

        return instance;
    }

    public void add() {
        tests.add(0, new PositiveTest(System.currentTimeMillis()));
        StorageUtils.saveObject(keyPositiveTests, this);
    }

    public ArrayList<PositiveTest> getTests() {
        return tests;
    }

    public static class PositiveTest implements Serializable {
        long date;

        public PositiveTest(long date) {
            this.date = date;
        }

        public String getDateFormated() {
            return Util.dayDateFormat(date);
        }
    }
}
