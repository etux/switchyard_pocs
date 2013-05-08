package com.edevera.switchyard.app2;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.switchyard.internal.DefaultContext;
import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 *         Date: 5/7/13
 *         Time: 9:03 PM
 */
public class ServiceBClient {


    public static void main(String[] args) throws IOException {
        com.edevera.switchyard.api.RequestB request = new com.edevera.switchyard.api.RequestB();
        RemoteInvoker invoker = new HttpInvoker("http://localhost:8080/switchyard-remote");
        invoker.invoke(new RemoteMessage()
                .setContext(new DefaultContext())
                .setService(new QName("urn:com.edevera.switchyard.app2:switchyard-ejb-app2:1.0-SNAPSHOT", "ServiceB"))
                .setContent(request));
    }
}
