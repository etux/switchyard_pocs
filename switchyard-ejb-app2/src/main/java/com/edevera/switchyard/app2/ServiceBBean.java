package com.edevera.switchyard.app2;

import javax.inject.Inject;

import com.edevera.switchyard.api.IService;
import com.edevera.switchyard.api.Request;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 *         Date: 5/1/13
 *         Time: 12:49 AM
 */
@Service(ServiceB.class)
public class ServiceBBean implements ServiceB {

    @Inject
    @Reference("{urn:org.edevera.switchyard:switchyard-ejb:1.0-SNAPSHOT}ServiceA")
    private IService service;

    @Override
    public void operation(RequestB requestB) {
        System.out.println("ServiceB operation");
        final Request request = new Request();
        service.operation(request);
    }
}
