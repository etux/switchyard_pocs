package com.edevera.switchyard.app2;

import java.io.File;

import javax.inject.Named;
import javax.xml.namespace.QName;

import com.edevera.switchyard.api.Request;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.internal.DefaultContext;
import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 *         Date: 5/1/13
 *         Time: 1:36 AM
 */
@RunWith(Arquillian.class)
@Named
public class ServiceBIT {

    @Deployment(order = 1, name = "api")
    public static Archive createApiDeployment() {
        MavenResolverSystem mavenResolver = Maven.configureResolver().fromFile(new File(System.getProperty("user.home"), ".m2/zimory-settings.xml"));
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "switchyard-ejb-api.jar");
        archive.addPackage("com.edevera.switchyard.api");
        archive.writeTo(System.out, Formatters.VERBOSE);
        archive.as(ZipExporter.class).exportTo(new File("target", archive.getName()), true);
        return archive;
    }

    @Deployment(order = 2, name = "ejb1")
    public static Archive createDeployment1() {
        MavenResolverSystem mavenResolver = Maven.configureResolver().fromFile(new File(System.getProperty("user.home"), ".m2/zimory-settings.xml"));
        EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class, "servicea.ear")
            .addAsApplicationResource("META-INF/serviceA-application.xml", "application.xml")
            .addAsApplicationResource("jboss-deployment-structure.xml")
            .addAsModule(
                    mavenResolver.loadPomFromFile("pom.xml")
                            .resolve("com.edevera.switchyard:switchyard-ejb-app1").withoutTransitivity().asSingleFile())
            /*.addAsLibraries(
                                mavenResolver.loadPomFromFile("pom.xml").resolve("com.edevera.switchyard:switchyard-ejb-api").withTransitivity().asFile())*/
            ;
        archive.writeTo(System.out, Formatters.VERBOSE);
        archive.as(ZipExporter.class).exportTo(new File("target", archive.getName()), true);
        return archive;
    }

    @Deployment(order = 3, name = "ejb2")
    public static Archive createDeployment2() {
        MavenResolverSystem mavenResolver = Maven.configureResolver().fromFile(new File(System.getProperty("user.home"), ".m2/zimory-settings.xml"));
        EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class, "serviceb.ear");
        archive
            .addAsModule(createServiceBDeployment())
            //.addAsModule(createTestDeployment())
            .addAsApplicationResource("META-INF/serviceB-application.xml", "application.xml")
            .addAsApplicationResource("jboss-deployment-structure.xml")
            /*.addAsLibraries(
                    mavenResolver.loadPomFromFile("pom.xml").resolve("com.edevera.switchyard:switchyard-ejb-api").withTransitivity().asFile())*/
        ;
        archive.writeTo(System.out, Formatters.VERBOSE);
        archive.as(ZipExporter.class).exportTo(new File("target", archive.getName()), true);
        return archive;
    }

    public static JavaArchive createServiceBDeployment() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "serviceb-imp.jar")
                                .addAsManifestResource("META-INF/switchyard-serviceb.xml", "switchyard.xml")
                                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                                .addClasses(ServiceB.class, ServiceBBean.class, RequestB.class, TransformerServiceB.class);
        archive.writeTo(System.out, Formatters.VERBOSE);
        archive.as(ZipExporter.class).exportTo(new File("target", archive.getName()), true);
        return archive;
    }

    @Deployment(order = 4, name = "test")
    public static WebArchive createTestDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                                .addClasses(ServiceBIT.class)
                                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                                .addAsWebInfResource("META-INF/jboss-web.xml", "jboss-web.xml")
                                .addAsWebInfResource("jboss-deployment-structure.xml")
                                .addAsManifestResource("META-INF/switchyard-test.xml", "switchyard.xml");
        archive.writeTo(System.out, Formatters.VERBOSE);
        archive.as(ZipExporter.class).exportTo(new File("target", archive.getName()), true);
        return archive;
    }

    @Test
    @OperateOnDeployment("test")
    public void test() throws Exception {
        Request request = new Request();
        RemoteInvoker invoker = new HttpInvoker("http://localhost:8080/switchyard-remote");
        invoker.invoke(new RemoteMessage()
                .setContext(new DefaultContext())
                .setService(new QName("urn:com.edevera.switchyard.app2:switchyard-ejb-app2:1.0-SNAPSHOT", "ServiceB"))
                .setContent(request));

    }
}