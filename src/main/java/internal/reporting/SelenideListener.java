package internal.reporting;

import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEventListener;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.*;
import io.qameta.allure.util.ResultsUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * Custom Selenide listener class for Allure reporting:
 * it gets logs from Selenide test execution context and prepares reports for Allure.
 * <p></p>
 * This custom listener adds some features to the standard functionality:
 * password masking, full page screenshots, adding console logs, etc...
 */
public class SelenideListener implements LogEventListener {

    /**
     * RegExp pattern to match URLs with Basic Authentication.
     * E.g. https://login:pass@example.com/
     */
    private static final String BASIC_AUTH_URL_PATTERN = "(.+://.+):(.+)@(.+)";
    private static final String BASIC_AUTH_URL_REPLACEMENT = "$1:***@$3";

    private final boolean isSaveScreenshots = true;
    private final boolean isSaveFullScreenScreenshot = true;
    private final boolean isSavePageSource = true;
    private final boolean isSaveConsoleLog = true;
    private final boolean isSaveVideo = Boolean.parseBoolean(System.getProperty("capabilities.enableVideo"));

    private final AllureLifecycle lifecycle = Allure.getLifecycle();

    /**
     * Actions to take BEFORE any Selenide action/method/command.
     *
     * @param event event that's created on Selenide action
     *              (e.g. "navigate to url", "click on element", "check a condition")
     */
    @Override
    public void beforeEvent(final @Nonnull LogEvent event) {
        //  Empty implementation
    }

    /**
     * Actions to take AFTER any Selenide action/method/command.
     * <p></p>
     * This method helps add all the additional functionality
     * that is needed in test steps in Allure reports.
     * <p>
     * Note: normally, adding attachments works if step execution has failed
     * (e.g. as a result of a regular assertion error)
     *
     * @param event event that's created on Selenide action
     *              (e.g. "navigate to url", "click on element", "check a condition")
     */
    @Override
    public void afterEvent(final @Nonnull LogEvent event) {
        lifecycle.getCurrentTestCase().ifPresent(uuid -> {
            var stepUUID = UUID.randomUUID().toString();

            lifecycle.startStep(stepUUID, new StepResult()
                    .setName(event.toString())
                    .setStatus(Status.PASSED));

            maskPasswordsInTestSteps(event);

            lifecycle.updateStep(stepResult -> stepResult.setStart(stepResult.getStart() - event.getDuration()));

            if (LogEvent.EventStatus.FAIL.equals(event.getStatus())) {
                var handles = WebDriverRunner.getWebDriver().getWindowHandles();

                for (String handle : handles) {
                    Selenide.switchTo().window(handle);

                    var title = WebDriverRunner.getWebDriver().getTitle();

                    if (isSaveScreenshots) {
                        lifecycle.addAttachment("Screenshot - " + title, "image/png", "png",
                                getScreenshotBytes());
                    }
                    if (isSaveFullScreenScreenshot) {
                        lifecycle.addAttachment("Full page screenshot - " + title, "image/png", "png",
                                getFullScreenScreenshotBytes());
                    }
                    if (isSavePageSource) {
                        lifecycle.addAttachment("Page source - " + title, "text/html", "html",
                                getPageSourceBytes());
                    }
                    if (isSaveConsoleLog) {
                        if (WebDriverRunner.isChrome()) {
                            lifecycle.addAttachment("Console log - " + title, "text/plain", "txt",
                                    getConsoleLogsBytes());
                        }
                    }
                    if (isSaveVideo && Configuration.remote != null && !Configuration.remote.isBlank()) {
                        var sessionId = ((RemoteWebDriver) WebDriverRunner.getWebDriver()).getSessionId();
                        var videoUrl = Configuration.remote.replace("/wd/hub", "/video");
                        var videoLink = String.format("%s/%s.mp4", videoUrl, sessionId.toString());

                        lifecycle.addAttachment(videoLink, "text/html", "html",
                                getVideoLinkFileBytes(videoLink));
                    }

                    lifecycle.addAttachment("Current page's URL", "text/plain", "txt",
                            WebDriverRunner.getWebDriver().getCurrentUrl()
                                    .replaceAll(BASIC_AUTH_URL_PATTERN, BASIC_AUTH_URL_REPLACEMENT)
                                    .getBytes());
                }

                lifecycle.updateStep(stepResult -> {
                    var status = ResultsUtils.getStatus(event.getError())
                            .orElse(Status.BROKEN);
                    stepResult.setStatus(status);

                    var details = ResultsUtils.getStatusDetails(event.getError())
                            .orElse(new StatusDetails());
                    stepResult.setStatusDetails(details);
                });
            }

            lifecycle.stopStep(stepUUID);
        });
    }

    /**
     * Mask password values in Allure's test steps.
     *
     * @param event any Selenide's action event
     */
    private void maskPasswordsInTestSteps(LogEvent event) {
        //  Masking password values in test steps
        if (event.getElement().toLowerCase().contains("password")) {
            var passwordMasked = event.toString().replaceAll("value\\(.+\\)", "value(***)");
            lifecycle.updateStep(stepResult -> stepResult.setName(passwordMasked));
        }

        //  Masking password in Basic Authentication URLs in test steps (e.g. https://login:pass@example.com/)
        if (event.getElement().toLowerCase().contains("open") &&
                event.getSubject().matches(BASIC_AUTH_URL_PATTERN)) {
            var basicAuthUrlMasked = event.toString()
                    .replaceAll(BASIC_AUTH_URL_PATTERN, BASIC_AUTH_URL_REPLACEMENT);
            lifecycle.updateStep(stepResult -> stepResult.setName(basicAuthUrlMasked));
        }
    }

    /**
     * Takes screenshot of the currently visible part of the current page.
     *
     * @return raw bytes that represent the taken screenshot.
     */
    private static byte[] getScreenshotBytes() {
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Takes screenshot of the entire page that test is currently at.
     *
     * @return raw bytes that represent the taken screenshot.
     */
    private static byte[] getFullScreenScreenshotBytes() {
        var image = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(1_000))
                .takeScreenshot(WebDriverRunner.getWebDriver())
                .getImage();

        var byteStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", byteStream);
        } catch (IOException ignored) {
        }

        return byteStream.toByteArray();
    }

    /**
     * Get HTML page source for the current page.
     *
     * @return raw bytes that represent the page's html code
     */
    private static byte[] getPageSourceBytes() {
        return WebDriverRunner.getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Get developer's console logs from the browser.
     * For Google Chrome only!
     *
     * @return raw bytes that represent the logs from the browser's dev console.
     */
    private static byte[] getConsoleLogsBytes() {
        List<LogEntry> logEntries = WebDriverRunner.getWebDriver().manage()
                .logs().get(LogType.BROWSER).getAll();

        StringBuilder result = new StringBuilder();
        for (LogEntry entry : logEntries) {
            result.append(entry.getTimestamp())
                    .append(" ")
                    .append(entry.getLevel())
                    .append(" ")
                    .append(entry.getMessage())
                    .append("\n");
        }

        return result.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Get a link to the test's video replay.
     *
     * @param videoLink web link to the test's video replay
     *                  (e.g. "http://path.to.the.video.com/video1365.mp4")
     * @return raw bytes that represent the *.html file with link to the video
     */
    private static byte[] getVideoLinkFileBytes(String videoLink) {
        var videoLinkFile = new File("video.html");
        byte[] videoFileBytes = null;
        try {
            var htmlContent = "<html><body>" +
                    "<a target=\"_blank\" href=\"" + videoLink + "\">Test video replay</a>" +
                    "</body></html>";

            FileUtils.writeStringToFile(videoLinkFile, htmlContent, Charset.defaultCharset());
            videoFileBytes = FileUtils.readFileToByteArray(videoLinkFile);

            videoLinkFile.deleteOnExit();
        } catch (IOException ignored) {
        }

        return videoFileBytes;
    }
}
