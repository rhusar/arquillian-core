/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.container.test.impl.enricher.resource;

import java.net.URL;
import org.jboss.arquillian.container.spi.client.protocol.metadata.HTTPContext;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.spi.client.protocol.metadata.Servlet;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.api.Secured;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * ArquillianTestEnricherTestCase
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @author <a href="http://community.jboss.org/people/silenius">Samuel Santos</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class URLResourceProviderTestCase extends OperatesOnDeploymentAwareProviderBase {
    @Override
    protected ResourceProvider getResourceProvider() {
        return new URLResourceProvider();
    }

    @Test
    public void shouldBeAbleToInjectBaseContextURL() throws Exception {
        URLBaseContextClass test = execute(
            URLBaseContextClass.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST", 8080)));

        Assert.assertEquals("http://TEST:8080", test.url.toExternalForm());
    }

    @Test
    public void shouldBeAbleToInjectBaseContextURLSecure() throws Exception {
        URLBaseContextClass test = execute(
            URLBaseContextClass.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST", 8080, true)));

        Assert.assertEquals("https://TEST:8080", test.url.toExternalForm());
    }

    @Test
    public void shouldBeAbleToInjectBaseSecureContextURL() throws Exception {
        SecureURLBaseContextClass test = execute(
            SecureURLBaseContextClass.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST", 443)));

        Assert.assertEquals("https://TEST", test.url.toExternalForm());
    }

    @Test
    public void shouldBeAbleToInjectBaseSecureContextURLWithPort() throws Exception {
        SecureURLWithPortBaseContextClass test = execute(
            SecureURLWithPortBaseContextClass.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST", 8443)));

        Assert.assertEquals("https://TEST:8443", test.url.toExternalForm());
    }

    @Test
    public void shouldBeAbleToInjectBaseSecureContextURLQualified() throws Exception {
        SecureURLBaseContextClassQualified test = execute(
            SecureURLBaseContextClassQualified.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST-Y", 443)),
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST-X", 443)));

        Assert.assertEquals("https://TEST-X", test.url.toExternalForm());
    }

    @Test
    public void shouldBeAbleToInjectBaseSecureContextURLWithPortQualified() throws Exception {
        SecureURLWithPortBaseContextClassQualified test = execute(
            SecureURLWithPortBaseContextClassQualified.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST-Y", 8443)),
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST-X", 8443)));

        Assert.assertEquals("https://TEST-X:8443", test.url.toExternalForm());
    }

    @Test
    public void shouldBeAbleToInjectServletSecureContextURL() throws Exception {
        SecureURLServletContextClass test = execute(
            SecureURLServletContextClass.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST", 443)
                    .add(new Servlet(SecureURLServletContextClass.class.getSimpleName(), "/test"))));

        Assert.assertEquals("https://TEST/test/", test.url.toExternalForm());
    }

    @Test
    public void shouldBeAbleToInjectServletSecureContextURLWithPort() throws Exception {
        SecureURLWithPortServletContextClass test = execute(
            SecureURLWithPortServletContextClass.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST", 8443)
                    .add(new Servlet(SecureURLWithPortServletContextClass.class.getSimpleName(), "/test"))));

        Assert.assertEquals("https://TEST:8443/test/", test.url.toExternalForm());
    }

    @Test
    public void shouldBeAbleToInjectBaseContextURLQualified() throws Exception {
        URLBaseContextClassQualified test = execute(
            URLBaseContextClassQualified.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST-Y", 8080)),
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST-X", 8080)));

        Assert.assertEquals("http://TEST-X:8080", test.url.toExternalForm());
    }

    @Test
    public void shouldBeAbleToInjectServletContextURL() throws Exception {
        URLServletContextClass test = execute(
            URLServletContextClass.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST", 8080)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test"))));

        Assert.assertEquals("http://TEST:8080/test/", test.url.toExternalForm());
    }

    @Test
    public void shouldBeAbleToInjectServletContextURLSecure() throws Exception {
        URLServletContextClass test = execute(
            URLServletContextClass.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST", 8080, true)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test"))));

        Assert.assertEquals("https://TEST:8080/test/", test.url.toExternalForm());
    }

    @Test
    public void shouldBeAbleToInjectServletContextURLQualified() throws Exception {
        URLServletContextClassQualified test = execute(
            URLServletContextClassQualified.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST-Y", 8080)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test-Y"))),
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST-X", 8080)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test-X"))));

        Assert.assertEquals("http://TEST-X:8080/test-X/", test.url.toExternalForm());
    }

    @Test
    public void shouldBeAbleToInjectServletContextURLQualifiedAndTargeted() throws Exception {
        URLBaseContextClassQualifiedTargeted test = execute(
            URLBaseContextClassQualifiedTargeted.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("NAME-A", "TEST-A-Y", 8080)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test-Y")))
                .addContext(new HTTPContext("NAME-B", "TEST-B-Y", 8080)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test-Y"))),
            new ProtocolMetaData()
                .addContext(new HTTPContext("NAME-A", "TEST-A-X", 8080)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test-X")))
                .addContext(new HTTPContext("NAME-B", "TEST-B-X", 8080)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test-X"))));

        Assert.assertEquals("http://TEST-B-X:8080/test-X/", test.url.toExternalForm());
    }

    @Test
    public void shouldBeAbleToInjectServletContextURLQualifiedAndNoTarget() throws Exception {
        URLServletContextClassQualified test = execute(
            URLServletContextClassQualified.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("NAME-A", "TEST-A-Y", 8080)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test-Y")))
                .addContext(new HTTPContext("NAME-B", "TEST-B-Y", 8080)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test-Y"))),
            new ProtocolMetaData()
                .addContext(new HTTPContext("NAME-A", "TEST-A-X", 8080)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test-X")))
                .addContext(new HTTPContext("NAME-B", "TEST-B-X", 8080)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test-X"))));

        // Default on multiple Named contexts is the first one added
        Assert.assertEquals("http://TEST-A-X:8080/test-X/", test.url.toExternalForm());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionOnMissingContainerRegistry() throws Exception {
        execute(
            false,
            true,
            URLServletContextClassQualified.class,
            ProtocolMetaData.class,
            new ProtocolMetaData(),
            new ProtocolMetaData());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionOnMissingDeploymentScenario() throws Exception {
        execute(
            true,
            false,
            URLServletContextClassQualified.class,
            ProtocolMetaData.class,
            new ProtocolMetaData(),
            new ProtocolMetaData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnUnKnownDeployment() throws Exception {
        execute(
            URLBaseContextClassQualifiedMissing.class,
            ProtocolMetaData.class,
            new ProtocolMetaData(),
            new ProtocolMetaData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnUnKnownTargetInDeployment() throws Exception {
        execute(
            URLBaseContextClassQualifiedTargetedMissing.class,
            ProtocolMetaData.class,
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST-Y", 8080)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test-Y"))),
            new ProtocolMetaData()
                .addContext(new HTTPContext("TEST-X", 8080)
                    .add(new Servlet(URLServletContextClass.class.getSimpleName(), "/test-X"))));
    }

    public static class SecureURLBaseContextClass {
        @ArquillianResource @Secured
        public URL url;
    }

    public static class SecureURLWithPortBaseContextClass {
        @ArquillianResource @Secured(port = 8443)
        public URL url;
    }

    public static class URLBaseContextClass {
        @ArquillianResource
        public URL url;
    }

    public static class URLServletContextClass {
        @ArquillianResource(URLServletContextClass.class)
        public URL url;
    }

    public static class URLBaseContextClassQualified {
        @ArquillianResource
        @OperateOnDeployment("X")
        public URL url;
    }

    public static class SecureURLServletContextClass {
        @ArquillianResource(SecureURLServletContextClass.class) @Secured
        public URL url;
    }

    public static class SecureURLWithPortServletContextClass {
        @ArquillianResource(SecureURLWithPortServletContextClass.class) @Secured(port = 8443)
        public URL url;
    }

    public static class SecureURLBaseContextClassQualified {
        @ArquillianResource @OperateOnDeployment("X") @Secured
        public URL url;
    }

    public static class SecureURLWithPortBaseContextClassQualified {
        @ArquillianResource @OperateOnDeployment("X") @Secured(port = 8443)
        public URL url;
    }

    public static class URLServletContextClassQualified {
        @ArquillianResource(URLServletContextClass.class)
        @OperateOnDeployment("X")
        public URL url;
    }

    public static class URLBaseContextClassQualifiedMissing {
        @ArquillianResource
        @OperateOnDeployment("MISSING")
        public URL url;
    }

    public static class URLBaseContextClassQualifiedTargeted {
        @ArquillianResource
        @OperateOnDeployment("X")
        @TargetsContainer("NAME-B")
        public URL url;
    }

    public static class URLBaseContextClassQualifiedTargetedMissing {
        @ArquillianResource
        @OperateOnDeployment("X")
        @TargetsContainer("MISSING")
        public URL url;
    }
}
