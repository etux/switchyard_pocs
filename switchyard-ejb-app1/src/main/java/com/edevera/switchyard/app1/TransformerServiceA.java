package com.edevera.switchyard.app1;

import org.switchyard.annotations.Transformer;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 */
public class TransformerServiceA {

    @Transformer
    public com.edevera.switchyard.app1.RequestA from(com.edevera.switchyard.api.RequestA request) {
        return new com.edevera.switchyard.app1.RequestA();
    }

    @Transformer
    public com.edevera.switchyard.api.RequestA from(com.edevera.switchyard.app1.RequestA request) {
        return new com.edevera.switchyard.api.RequestA();
    }
}
