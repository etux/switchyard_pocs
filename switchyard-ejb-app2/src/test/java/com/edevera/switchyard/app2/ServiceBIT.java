package com.edevera.switchyard.app2;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import com.edevera.switchyard.api.IService;
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
import org.switchyard.component.bean.Reference;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 *         Date: 5/1/13
 *         Time: 1:36 AM
 */
@RunWith(Arquillian.class)
@Named
public class ServiceBIT {

    @Inject
    @Reference("Service")
    private IService service;

//    @Deployment(order = 1, name = "ejb1")
//    public static Archive createDeployment1() {
//        MavenResolverSystem mavenResolver = Maven.configureResolver().fromFile(new File(System.getProperty("user.home"), ".m2/zimory-settings.xml"));
//        EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class, "servicea.ear");
//        archive.addAsModule(
//                mavenResolver.loadPomFromFile("pom.xml")
//                        .resolve("com.edevera.switchyard:switchyard-ejb-app1").withoutTransitivity().asSingleFile()
//        );
//        archive.addAsLibraries(
//                mavenResolver.loadPomFromFile("pom.xml").resolve("com.edevera.switchyard:switchyard-ejb-api").withTransitivity().asFile()
//        );
//        archive.writeTo(System.out, Formatters.VERBOSE);
//        return archive;
//    }

    @Deployment(order = 1, name = "ejb2")
    public static Archive createDeployment2() {
        MavenResolverSystem mavenResolver = Maven.configureResolver().fromFile(new File(System.getProperty("user.home"), ".m2/zimory-settings.xml"));
        EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class, "serviceb.ear");
        archive
            .addAsModule(createEjbDeployment())
            .addAsModule(createTestDeployment())
            .addAsApplicationResource("META-INF/application.xml", "application.xml")
            .addAsLibraries(
                    mavenResolver.loadPomFromFile("pom.xml").resolve("com.edevera.switchyard:switchyard-ejb-api").withTransitivity().asFile()
            );
        archive.writeTo(System.out, Formatters.VERBOSE);
        archive.as(ZipExporter.class).exportTo(new File("target", archive.getName()), true);
        return archive;
    }

    public static JavaArchive createEjbDeployment() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "serviceb-imp.jar")
                                .addAsManifestResource("META-INF/switchyard-ejb.xml", "switchyard.xml")
                                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                                .addClasses(ServiceB.class, ServiceBBean.class, RequestB.class, TransformerServiceB.class);
        archive.writeTo(System.out, Formatters.VERBOSE);
        archive.as(ZipExporter.class).exportTo(new File("target", archive.getName()), true);
        return archive;
    }

    public static WebArchive createTestDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class,"test.war")
                                .addClasses(ServiceBIT.class)
                                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                                .addAsManifestResource("META-INF/switchyard-test.xml", "switchyard.xml")
                                ;
        archive.writeTo(System.out, Formatters.VERBOSE);
        archive.as(ZipExporter.class).exportTo(new File("target", archive.getName()), true);
        return archive;
    }

    @Test
    @OperateOnDeployment("ejb2")
    public void test() {
        Request request = new Request();
        service.operation(request);
    }
}
