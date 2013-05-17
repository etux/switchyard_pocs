package com.edevera.switchyard.app1;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.switchyard.component.bean.Service;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 */
@Service(value = ServiceA.class)
@Stateless
@Local(ServiceA.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ServiceABean implements ServiceA {

    @PostConstruct
    public void postConstruct() {
        System.out.println("POST CONSTRUCT for ServiceA");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void operation(RequestA request) {
        System.out.println("Service A Operation");
    }
}
