/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010 Red Hat Inc. and/or its affiliates and other contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
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
package org.jboss.arquillian.container.test.impl.client.protocol.local;

import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.test.spi.ContainerMethodExecutor;
import org.jboss.arquillian.container.test.spi.client.deployment.DeploymentPackager;
import org.jboss.arquillian.container.test.spi.client.protocol.Protocol;
import org.jboss.arquillian.container.test.spi.command.CommandCallback;
import org.jboss.arquillian.core.api.Injector;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.spi.TestMethodExecutor;

/**
 * A Protocol that invokes the {@link TestMethodExecutor#invoke()} directly.
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 */
public class LocalProtocol implements Protocol<LocalProtocolConfiguration> {
    public static final String NAME = "Local";

    @Inject
    private Instance<Injector> injector;

    /* (non-Javadoc)
     * @see org.jboss.arquillian.spi.client.protocol.Protocol#getProtocolConfigurationClass()
     */
    public Class<LocalProtocolConfiguration> getProtocolConfigurationClass() {
        return LocalProtocolConfiguration.class;
    }

    /* (non-Javadoc)
     * @see org.jboss.arquillian.spi.client.protocol.Protocol#getDescription()
     */
    public ProtocolDescription getDescription() {
        return new ProtocolDescription(NAME);
    }

    /* (non-Javadoc)
     * @see org.jboss.arquillian.spi.client.protocol.Protocol#getPackager()
     */
    public DeploymentPackager getPackager() {
        return new LocalDeploymentPackager();
    }

    /* (non-Javadoc)
     * @see org.jboss.arquillian.spi.client.protocol.Protocol#getExecutor(org.jboss.arquillian.spi.client.protocol.ProtocolConfiguration, org.jboss.arquillian.spi.client.protocol.metadata.ProtocolMetaData)
     */
    public ContainerMethodExecutor getExecutor(LocalProtocolConfiguration protocolConfiguration,
        ProtocolMetaData metaData, CommandCallback callback) {
        return injector.get().inject(new LocalContainerMethodExecutor());
    }
}
