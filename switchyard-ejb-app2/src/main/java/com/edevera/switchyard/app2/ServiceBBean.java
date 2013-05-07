package com.edevera.switchyard.app2;

import javax.inject.Inject;

import com.edevera.switchyard.api.ServiceA;
import com.edevera.switchyard.api.RequestA;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 */
@Service(ServiceB.class)
public class ServiceBBean implements ServiceB {

    @Inject
    @Reference("ServiceA")
    private ServiceA service;

    @Override
    public void operation(RequestB requestB) {
        System.out.println("ServiceB operation");
        final RequestA request = new RequestA();
        service.operationA(request);
    }
}
