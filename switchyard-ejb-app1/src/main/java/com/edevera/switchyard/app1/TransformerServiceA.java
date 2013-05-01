package com.edevera.switchyard.app1;

import com.edevera.switchyard.api.Request;
import org.switchyard.annotations.Transformer;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 */
public class TransformerServiceA {

    @Transformer
    public RequestA from(Request request) {
        return new RequestA();
    }

    @Transformer
    public Request from(RequestA request) {
        return new Request();
    }
}
