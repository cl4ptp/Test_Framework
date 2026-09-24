package internal.reporting.aspects;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;

/**
 * Aspect class for logging actions from AssertJ library into Allure reports.
 * This class is a modified version of <b>allure-assertj</b> library.
 * <p></p>
 * Modifications help to provide Soft Assertions framework actions with better reporting.
 */
@SuppressWarnings("unused")
@Aspect
public class AllureAssertJAspect {
    private static final InheritableThreadLocal<AllureLifecycle> lifecycle = new InheritableThreadLocal<>() {
        @Override
        protected AllureLifecycle initialValue() {
            return Allure.getLifecycle();
        }
    };

    /**
     * Pointcut for catching the creation of new assertions
     * (e.g. assertThat(..) methods)
     */
    @Pointcut("execution(public org.assertj.core.api.AbstractAssert.new(..))")
    public void anyAssertCreation() {
        //pointcut body, should be empty
    }

    /**
     * Pointcut for catching the creation of proxy assertions
     * (normally, for Soft Assertions)
     */
    @Pointcut("execution(* org.assertj.core.api.AssertJProxySetup.*(..))")
    public void proxyMethod() {
        //pointcut body, should be empty
    }

    /**
     * Pointcut for catching any assertions, except for proxy assertions
     * (e.g. isEqualTo(), isNotNull()...)
     */
    @Pointcut("execution(public * org.assertj.core.api.AbstractAssert+.*(..)) && !proxyMethod()")
    public void anyAssert() {
        //pointcut body, should be empty
    }

    /**
     * Pointcut for catching methods that add new Assertion Errors for Soft Assertions.
     */
    @Pointcut("execution(* org.assertj.core.api.ErrorCollector.collectAssertionError(..))")
    public void proxyCollectError() {
        //pointcut body, should be empty
    }

    /**
     * Reporting actions after creating new assertions.
     *
     * @param joinPoint current intercepted context for aspect
     */
    @After("anyAssertCreation()")
    public void logAssertCreation(final JoinPoint joinPoint) {
        final String actual = joinPoint.getArgs().length > 0
                ? ObjectUtils.toString(joinPoint.getArgs()[0])
                : "<?>";
        final String uuid = UUID.randomUUID().toString();
        final String name = String.format("assertThat '%s'", actual);

        final StepResult result = new StepResult()
                .setName(name)
                .setStatus(Status.PASSED);

        getLifecycle().startStep(uuid, result);
        getLifecycle().stopStep(uuid);
    }

    /**
     * Reporting action before starting an actual check in the assertion.
     *
     * @param joinPoint current intercepted context for aspect
     */
    @Before("anyAssert()")
    public void stepStart(final JoinPoint joinPoint) {
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        final String uuid = UUID.randomUUID().toString();
        final String name = joinPoint.getArgs().length > 0
                ? String.format("%s '%s'", methodSignature.getName(), arrayToString(joinPoint.getArgs()))
                : methodSignature.getName();

        final StepResult result = new StepResult()
                .setName(name);

        getLifecycle().startStep(uuid, result);
        getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
    }

    /**
     * Report action after assertion throws an exception.
     * Typically, happens when assertion fails (actual and expected values are different).
     *
     * @param e any exception that was thrown during the assertion
     *          (normally, it's AssertionError)
     */
    @AfterThrowing(pointcut = "anyAssert()", throwing = "e")
    public void stepFailed(final Throwable e) {
        getLifecycle().updateStep(s -> s
                .setStatus(getStatus(e).orElse(Status.BROKEN))
                .setStatusDetails(getStatusDetails(e).orElse(null)));
        getLifecycle().stopStep();
    }

    /**
     * Report the end of the assertion action.
     */
    @AfterReturning(pointcut = "anyAssert()")
    public void stepStop() {
        getLifecycle().getCurrentTestCaseOrStep().ifPresent(uuid -> getLifecycle().stopStep(uuid));
    }

    /**
     * Report any soft assertion error.
     * Adds screenshot, in case it may be helpful.
     *
     * @param joinPoint current intercepted context for aspect
     */
    @After("proxyCollectError()")
    public void logSoftAssertionError(JoinPoint joinPoint) {
        for (var arg : joinPoint.getArgs()) {
            if (arg instanceof AssertionError) {
                AssertionError error = (AssertionError) arg;

                getLifecycle().updateStep(s -> s
                        .setStatus(Status.FAILED)
                        .setStatusDetails(getStatusDetails(error).orElse(null)));

                var title = WebDriverRunner.getWebDriver().getTitle();
                getLifecycle().addAttachment("Screenshot - " + title, "image/png", "png",
                        ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES)
                );
            }
        }
    }

    /**
     * Get current Allure Lifecycle object.
     * The main object to manipulate test/step results.
     *
     * @return current Allure Lifecycle context (with collection of test results and more)
     */
    public static AllureLifecycle getLifecycle() {
        return lifecycle.get();
    }

    /**
     * Convert array of Objects to String.
     * Useful for reporting in test steps.
     *
     * @param array any array of Java Objects to convert
     * @return string representation of Java Objects as a collection
     * (e.g. "Object1String Object2String Object3String")
     */
    private static String arrayToString(final Object... array) {
        return Stream.of(array)
                .map(ObjectUtils::toString)
                .collect(Collectors.joining(" "));
    }
}
