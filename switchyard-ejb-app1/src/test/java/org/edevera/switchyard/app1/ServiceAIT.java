package org.edevera.switchyard.app1;

import java.io.File;

import javax.inject.Inject;

import com.edevera.switchyard.app1.RequestA;
import com.edevera.switchyard.app1.ServiceA;
import com.edevera.switchyard.app1.ServiceABean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.bean.Reference;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 *         Date: 5/1/13
 *         Time: 3:03 AM
 */
@RunWith(Arquillian.class)
public class ServiceAIT {

    @Inject
    @Reference
    ServiceA serviceA;

    @Deployment
    public static Archive createArchive() {
        MavenResolverSystem mavenResolver = Maven.configureResolver().fromFile(new File(System.getProperty("user.home"), ".m2/zimory-settings.xml"));
        EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class, "servicea.ear");
        archive
            .addAsModule(getEjbArchive())
            .addAsModule(getWebArchive())
            .addAsLibraries(
                mavenResolver.loadPomFromFile("pom.xml").resolve("com.edevera.switchyard:switchyard-ejb-api").withTransitivity().asFile());
        archive.writeTo(System.out, Formatters.VERBOSE);
        return archive;
    }

    private static JavaArchive getEjbArchive() {
        JavaArchive archive  = ShrinkWrap.create(JavaArchive.class, "servicea-imp.jar")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/switchyard.xml")
                .addClasses(ServiceA.class, ServiceABean.class, RequestA.class);
        archive.writeTo(System.out, Formatters.VERBOSE);
        return archive;
    }

    private static WebArchive getWebArchive() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test-servicea-impl.war")
                .addClass(ServiceAIT.class);
        archive.writeTo(System.out, Formatters.VERBOSE);
        return archive;
    }

    @Test
    public void test() {
        final RequestA request = new RequestA();
        serviceA.operationA(request);
    }
}
