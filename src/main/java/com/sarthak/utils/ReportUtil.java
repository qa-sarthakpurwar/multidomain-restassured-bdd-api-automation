package com.sarthak.utils;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class ReportUtil {

    public static void log(String message) {
        ExtentCucumberAdapter.addTestStepLog(message);
    }

    public static void logRequest(String request) {
        ExtentCucumberAdapter.addTestStepLog("===== REQUEST =====");
        ExtentCucumberAdapter.addTestStepLog(request);
    }

    public static void logResponse(String response) {
        ExtentCucumberAdapter.addTestStepLog("===== RESPONSE =====");
        ExtentCucumberAdapter.addTestStepLog(response);
    }
}