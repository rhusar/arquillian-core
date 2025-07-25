package org.jboss.arquillian.junit5;

import org.jboss.arquillian.test.spi.TestRunnerAdaptor;
import org.jboss.arquillian.test.spi.TestRunnerAdaptorBuilder;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.jboss.arquillian.junit5.ContextStore.getContextStore;

/**
 * Manages the lifecycle of JUnit Jupiter test classes within the Arquillian framework.
 * This class implements both {@link AutoCloseable} and {@link ExtensionContext.Store.CloseableResource}
 * for junit5 prior to 5.13.0 to ensure proper resource cleanup.
 *
 * <p>The manager is responsible for:</p>
 * <ul>
 *   <li>Initializing and managing the Arquillian{@link TestRunnerAdaptor}</li>
 *   <li>Handling test suite lifecycle events (beforeSuite, afterSuite)</li>
 *   <li>Providing access to the test runner adaptor for test execution</li>
 *   <li>Managing initialization failures and error handling</li>
 * </ul>
 *
 * <p>This class uses a singleton pattern per test context, ensuring that only one
 * manager instance exists per test suite execution.</p>
 */

public class JUnitJupiterTestClassLifecycleManager implements AutoCloseable,
    ExtensionContext.Store.CloseableResource {
    private static final String MANAGER_KEY = "testRunnerManager";

    private TestRunnerAdaptor adaptor;

    private Throwable caughtInitializationException;

    private JUnitJupiterTestClassLifecycleManager() {
    }

    static JUnitJupiterTestClassLifecycleManager getManager(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = getContextStore(context).getRootStore();
        JUnitJupiterTestClassLifecycleManager instance = store.get(MANAGER_KEY, JUnitJupiterTestClassLifecycleManager.class);
        if (instance == null) {
            instance = new JUnitJupiterTestClassLifecycleManager();
            store.put(MANAGER_KEY, instance);
            instance.initializeAdaptor();
        }
        // no, initialization has been attempted before and failed, refuse
        // to do anything else
        if (instance.hasInitializationException()) {
            instance.handleSuiteLevelFailure();
        }
        return instance;
    }

    private void initializeAdaptor() throws Exception {
        try {
            // ARQ-1742 If exceptions happen during boot
            adaptor = TestRunnerAdaptorBuilder.build();
            // don't set it if beforeSuite fails
            adaptor.beforeSuite();
        } catch (Exception e) {
            // caught exception during BeforeSuite, mark this as failed
            handleBeforeSuiteFailure(e);
        }
    }

    @Override
    public void close() {
        if (adaptor == null) {
            return;
        }

        try {
            adaptor.afterSuite();
            adaptor.shutdown();
        } catch (Exception e) {
            throw new RuntimeException("Could not run @AfterSuite", e);
        }
    }

    private void handleSuiteLevelFailure() {
        throw new RuntimeException(
            "Arquillian initialization has already been attempted, but failed. See previous exceptions for cause",
            caughtInitializationException);
    }

    private boolean hasInitializationException() {
        return caughtInitializationException != null;
    }
    /**
     * Handles initialization failures that occur during the beforeSuite phase.
     * This method captures the exception and stores it for later retrieval,
     * preventing repeated initialization attempts.
     *
     * @param e the exception that occurred during initialization
     */
    private void handleBeforeSuiteFailure(Exception e) throws Exception {
        caughtInitializationException = e;
        throw e;
    }

    /**
     * Retrieves the TestRunnerAdaptor instance managed by this lifecycle manager.
     *
     * @return the TestRunnerAdaptor instance, or null if initialization failed
     */
    TestRunnerAdaptor getAdaptor() {
        return adaptor;
    }
}
