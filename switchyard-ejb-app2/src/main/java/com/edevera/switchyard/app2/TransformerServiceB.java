package com.edevera.switchyard.app2;

import com.edevera.switchyard.api.Request;
import org.switchyard.annotations.Transformer;

/**
 * @author <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 *         Date: 5/1/13
 *         Time: 1:29 AM
 */
public class TransformerServiceB {

    @Transformer
    public RequestB transform(Request request) {
        return new RequestB();
    }

    @Transformer
    public Request transform(RequestB request) {
        return new Request();
    }
}
