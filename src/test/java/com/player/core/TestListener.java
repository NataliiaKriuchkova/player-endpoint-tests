package com.player.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);
    private static final String SEPARATOR = "========================================================";

    @Override
    public void onStart(ITestContext context) {
        log.info(SEPARATOR);
        log.info("Suite: {}", context.getSuite().getName());
        log.info(SEPARATOR);
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info(SEPARATOR);
        log.info("Suite Finished: {}", context.getSuite().getName());
        log.info(SEPARATOR);
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Starting: {}", result.getMethod().getDescription());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info(SEPARATOR);
        log.info("PASSED: {}", result.getMethod().getDescription());
        log.info(SEPARATOR);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error(SEPARATOR);
        log.error("FAILED: {}", result.getMethod().getDescription());
        log.error(SEPARATOR);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn(SEPARATOR);
        log.warn("SKIPPED: {}", result.getMethod().getDescription());
        log.warn(SEPARATOR);
    }
}
