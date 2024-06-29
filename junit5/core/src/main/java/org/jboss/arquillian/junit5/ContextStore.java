package org.jboss.arquillian.junit5;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

class ContextStore {
    private static final String NAMESPACE_KEY = "arquillianNamespace";

    private static final String INTERCEPTED_TEMPLATE_NAMESPACE_KEY = "interceptedTestTemplates";

    private static final String RESULT_NAMESPACE_KEY = "results";

    private final ExtensionContext context;

    private ContextStore(ExtensionContext context) {
        this.context = context;
    }

    static ContextStore getContextStore(ExtensionContext context) {
        return new ContextStore(context);
    }

    ExtensionContext.Store getRootStore() {
        return context.getRoot().getStore(ExtensionContext.Namespace.create(NAMESPACE_KEY));
    }

    private ExtensionContext.Store getTemplateStore() {
        return context.getStore(ExtensionContext.Namespace.create(NAMESPACE_KEY, INTERCEPTED_TEMPLATE_NAMESPACE_KEY));
    }

    private ExtensionContext.Store getResultStore() {
        return context.getStore(ExtensionContext.Namespace.create(NAMESPACE_KEY, RESULT_NAMESPACE_KEY));
    }

    boolean isRegisteredTemplate(Method method) {
        final ExtensionContext.Store templateStore = getTemplateStore();

        final boolean isRegistered = templateStore.getOrDefault(method.toGenericString(), boolean.class, false);
        if (!isRegistered) {
            templateStore.put(method.toGenericString(), true);
        }
        return isRegistered;
    }

    void storeResult(String uniqueId, Throwable throwable) {
        final ExtensionContext.Store resultStore = getResultStore();
        resultStore.put(uniqueId, throwable);
        // TODO: find source and unwrap it where it is thrown, not here.
        if (throwable instanceof InvocationTargetException) {
            resultStore.put(uniqueId, throwable.getCause());
        } else {
            resultStore.put(uniqueId, throwable);
        }
    }

    Optional<Throwable> getResult(String uniqueId) {
        final ExtensionContext.Store resultStore = getResultStore();
        return Optional.ofNullable(resultStore.getOrDefault(uniqueId, Throwable.class, null));
    }
}
