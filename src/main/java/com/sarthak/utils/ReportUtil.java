package com.sarthak.utils;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class ReportUtil {

    public static void log(String message) {
        safeLog(message);
    }

    public static void logRequest(String request) {
        safeLog("===== REQUEST =====");
        safeLog(request);
    }

    public static void logResponse(String response) {
        safeLog("===== RESPONSE =====");
        safeLog(response);
    }

    private static void safeLog(String message) {
        try {
            ExtentCucumberAdapter.addTestStepLog(message);
        } catch (Exception e) {
            // fallback for parallel safety
            System.out.println(message);
        }
    }
}