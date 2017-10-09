package ga.asev.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.AbstractResponseHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class TwitchResponseHandler<T> extends AbstractResponseHandler<T> {
    private Class<T> entityType;
    private ObjectMapper mapper;

    public TwitchResponseHandler(Class<T> entityType, ObjectMapper mapper) {
        this.entityType = entityType;
        this.mapper = mapper;
    }

    public T handleEntity(HttpEntity entity) throws IOException {
        ContentType contentType = ContentType.getOrDefault(entity);
        Charset charset = contentType.getCharset();
        Reader reader = new InputStreamReader(entity.getContent(), charset);
        return mapper.readValue(reader, entityType);
    }

}
