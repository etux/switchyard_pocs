package com.edevera.switchyard.it;

import java.io.File;

import javax.inject.Named;
import javax.xml.namespace.QName;

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
import org.jboss.shrinkwrap.resolver.api.maven.PomlessResolveStage;
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
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "switchyard-ejb-api.jar")
            .merge(mavenResolver.loadPomFromFile("pom.xml").resolve("com.edevera.switchyard:switchyard-ejb-api").withoutTransitivity().as(JavaArchive.class)[0]);
        archive.writeTo(System.out, Formatters.VERBOSE);
        System.out.println();
        archive.as(ZipExporter.class).exportTo(new File("target", archive.getName()), true);
        return archive;
    }

    @Deployment(order = 2, name = "ejb1")
    public static Archive createApp1Deployment() {
        PomlessResolveStage resolver = Maven.configureResolver().fromFile(new File(System.getProperty("user.home"), ".m2/zimory-settings.xml")).offline();
        EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class, "servicea.ear")
                .merge(resolver.loadPomFromFile("pom.xml").resolve("com.edevera.switchyard:switchyard-ejb-app1-ear:ear:1.0-SNAPSHOT").withoutTransitivity().as(EnterpriseArchive.class)[0]);
        archive.writeTo(System.out, Formatters.VERBOSE);
        System.out.println();
        archive.as(ZipExporter.class).exportTo(new File("target", archive.getName()), true);
        return archive;
    }

    @Deployment(order = 3, name = "ejb2")
    public static Archive createApp2Deployment() {
        PomlessResolveStage mavenResolver = Maven.configureResolver().fromFile(new File(System.getProperty("user.home"), ".m2/zimory-settings.xml")).offline();
        EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class, "serviceb.ear")
            .merge(mavenResolver.loadPomFromFile("pom.xml").resolve("com.edevera.switchyard:switchyard-ejb-app2-ear:ear:1.0-SNAPSHOT").withoutTransitivity().as(EnterpriseArchive.class)[0]);
        archive.writeTo(System.out, Formatters.VERBOSE);
        System.out.println();
        archive.as(ZipExporter.class).exportTo(new File("target", archive.getName()), true);
        return archive;
    }

    @Deployment(order = 4, name = "test")
    public static WebArchive createTestDeployment() {
        PomlessResolveStage mavenResolver = Maven.configureResolver().fromFile(new File(System.getProperty("user.home"), ".m2/zimory-settings.xml")).offline();
        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                .addClasses(ServiceBIT.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                //.addAsWebInfResource("jboss-web.xml", "jboss-web.xml")
                .addAsManifestResource("jboss-deployment-structure-app1.xml", "jboss-deployment-structure.xml")
                //.addAsManifestResource("META-INF/switchyard-test.xml", "switchyard.xml")
                .addAsLibraries(
                        mavenResolver.loadPomFromFile("pom.xml").resolve("org.switchyard:switchyard-remote").withTransitivity().asFile())
                /*.addAsLibrary(
                        ShrinkWrap.create(JavaArchive.class, "switchyard-ejb-api.jar")
                            .addPackage("com.edevera.switchyard.api")
                            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml"))*/
                ;
        archive.writeTo(System.out, Formatters.VERBOSE);
        System.out.println();
        archive.as(ZipExporter.class).exportTo(new File("target", archive.getName()), true);
        return archive;
    }

    @Test
    @OperateOnDeployment("test")
    public void test() throws Exception {
        com.edevera.switchyard.api.RequestB request = new com.edevera.switchyard.api.RequestB();
        RemoteInvoker invoker = new HttpInvoker("http://localhost:8080/switchyard-remote");
        invoker.invoke(new RemoteMessage()
                .setContext(new DefaultContext())
                .setService(new QName("urn:com.edevera.switchyard.app2:switchyard-ejb-app2:1.0-SNAPSHOT", "ServiceB"))
                .setContent(request));

    }
}