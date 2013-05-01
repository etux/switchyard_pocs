package com.edevera.switchyard.app2;

import org.switchyard.component.bean.Service;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 *         Date: 5/1/13
 *         Time: 12:49 AM
 */
@Service(ServiceB.class)
public class ServiceBBean implements ServiceB {

    @Override

    public void operation(RequestB requestB) {
        System.out.println("ServiceB operation");
    }
}
