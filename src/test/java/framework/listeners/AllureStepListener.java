package framework.listeners;

import framework.logger.Logger;
import io.qameta.allure.Attachment;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static framework.browser.Browser.getCurrentUrl;
import static framework.browser.Browser.getDriver;
import static framework.logger.Logger.*;
import static io.qameta.allure.Allure.addAttachment;
import static io.qameta.allure.Allure.getLifecycle;

public class AllureStepListener implements StepLifecycleListener {

    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addLogToStep(String logMessage, String logLevel) {
        String atUrl = " at ".concat(getCurrentUrl());

        getLifecycle().updateStep(step -> {
            if (step.getStatusDetails() == null) {
                StatusDetails statusDetails = new StatusDetails();

                step.setStatusDetails(statusDetails.setMessage(logLevel.concat(atUrl)));
                step.setStatusDetails(statusDetails.setTrace(logMessage));
            } else {
                step.setStatusDetails(step.getStatusDetails().setMessage(step.getStatusDetails().getMessage().concat("⋮ ").concat(logLevel.concat(atUrl))));
                step.setStatusDetails(step.getStatusDetails().setTrace(step.getStatusDetails().getTrace().concat("⋮ ").concat(logMessage)));
            }
        });
    }

    @Override
    public void beforeStepStop(StepResult result) {
        getLifecycle().updateStep(step -> {
            step.setParameters(new LinkedList<>());

            if (step.getStatusDetails() == null || step.getStatusDetails().getTrace() == null) {
                updateStepStatusBasedOnChildSteps(step);
                return;
            }

            Map<String, List<String>> logsMap = new HashMap<>();
            List<String> logLevels = Arrays.asList(step.getStatusDetails().getMessage().split("⋮ "));
            List<String> logMessages = Arrays.asList(step.getStatusDetails().getTrace().split("⋮ "));

            AtomicInteger index = new AtomicInteger(0);
            logLevels.forEach(log -> {
                logsMap.putIfAbsent(log, new ArrayList<>());
                logsMap.get(log).add(logMessages.get(index.get()));
                index.incrementAndGet();
            });

            Set<String> setOfKeys = logsMap.keySet();
            if (setOfKeys.stream().anyMatch(key -> key.contains(FAIL))) {
                step.setStatus(Status.FAILED);
            } else if (setOfKeys.stream().anyMatch(key -> key.contains(WARNING))) {
                step.setStatus(Status.BROKEN);
            } else if (setOfKeys.stream().anyMatch(key -> key.contains(INFORMATION))) {
                step.setStatus(Status.PASSED);
            } else {
                if (step.getSteps().stream().anyMatch(element -> element.getStatusDetails() != null && element.getStatusDetails().isKnown())) {
                    step.getStatusDetails().setKnown(true).setTrace(null).setMessage(null);
                    updateStepStatusBasedOnChildSteps(step);
                    return;
                } else if (step.getStatusDetails().getTrace().contains("SkipException")) {
                    step.setStatus(Status.SKIPPED);
                    logger.info(step.getStatusDetails().getMessage());
                    step.setStatusDetails(null);
                    return;
                } else {
                    step.setStatus(Status.FAILED);
                    takeScreenshotForFailedStep();
                    step.getStatusDetails().setKnown(true);
                    return;
                }
            }

            setOfKeys.forEach(log -> addAttachment(log, String.join("\n-\n", logsMap.get(log))));
            step.setStatusDetails(null);
            updateStepStatusBasedOnChildSteps(step);
        });
    }

    private void updateStepStatusBasedOnChildSteps(StepResult step) {
        if (step.getSteps().stream().anyMatch(element -> element.getStatus().equals(Status.FAILED))) {
            step.setStatus(Status.FAILED);
        } else if (step.getSteps().stream().anyMatch(element -> element.getStatus().equals(Status.BROKEN))) {
            step.setStatus(Status.BROKEN);
        }
    }

    public static void appendStep(String valueToAppend) {
        getLifecycle().updateStep(step -> step.setName(step.getName() + " [" + valueToAppend + "]"));
    }

    @Attachment(value = "FAILED STEP SCREENSHOT", type = "image/png")
    private static byte[] takeScreenshotForFailedStep() {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{reasonOfScreenshot}", type = "image/png")
    public static byte[] takeScreenshot(String reasonOfScreenshot) {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
    }
}