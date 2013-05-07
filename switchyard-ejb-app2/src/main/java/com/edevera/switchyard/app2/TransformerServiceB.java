package com.edevera.switchyard.app2;
import org.switchyard.annotations.Transformer;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 *         Date: 5/1/13
 *         Time: 1:29 AM
 */
public class TransformerServiceB {

    @Transformer
    public RequestB transform(com.edevera.switchyard.api.RequestB request) {
        return new RequestB();
    }

    @Transformer
    public com.edevera.switchyard.api.RequestB transform(RequestB request) {
        return new com.edevera.switchyard.api.RequestB();
    }
}
