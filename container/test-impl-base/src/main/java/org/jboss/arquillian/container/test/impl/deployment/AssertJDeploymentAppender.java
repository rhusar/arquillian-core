package org.jboss.arquillian.container.test.impl.deployment;

import org.jboss.arquillian.container.test.spi.client.deployment.CachedAuxilliaryArchiveAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class AssertJDeploymentAppender extends CachedAuxilliaryArchiveAppender {
    @Override
    protected Archive<?> buildArchive() {
        return ShrinkWrap.create(JavaArchive.class, "arquillian-assertj.jar")
            .addPackages(true, "org.assertj")
            .addPackages(true, "net.bytebuddy");
    }
}
