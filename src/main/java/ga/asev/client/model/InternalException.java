package ga.asev.client.model;

import java.io.IOException;

public class InternalException extends RuntimeException {
    public InternalException(Exception e) {
        super(e);
    }
}
