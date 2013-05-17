package com.edevera.switchyard.app2;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import com.edevera.switchyard.api.RequestA;
import com.edevera.switchyard.api.ServiceA;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 */
@Service(ServiceB.class)
@Stateless
@Local(ServiceB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ServiceBBean implements ServiceB {

    @PostConstruct
    public void postConstruct() {
        System.out.println("POST CONSTRUCT for ServiceB");
    }

    @Inject
    @Reference("ServiceA")
    private ServiceA service;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void operation(RequestB requestB) {
        System.out.println("ServiceB operation");
        final RequestA request = new RequestA();
        service.operation(request);
    }
}
