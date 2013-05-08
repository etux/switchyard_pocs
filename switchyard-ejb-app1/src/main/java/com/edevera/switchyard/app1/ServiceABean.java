package com.edevera.switchyard.app1;

import org.switchyard.component.bean.Service;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 */
@Service(value = ServiceA.class)
public class ServiceABean implements ServiceA {

    @Override
    public void operation(RequestA request) {
        System.out.println("Service A Operation");
    }
}
