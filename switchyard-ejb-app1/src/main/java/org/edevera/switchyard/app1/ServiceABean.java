package org.edevera.switchyard.app1;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.switchyard.component.bean.Service;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 */
@Local(ServiceA.class)
@Stateless
@Service(ServiceA.class)
public class ServiceABean implements ServiceA {

    @Override
    public void operation(RequestA request) {
        System.out.println("Service A Operation");
    }
}
