package ga.asev.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.asev.client.model.InternalException;
import ga.asev.client.model.TokenException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.ws.rs.NotFoundException;
import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.springframework.util.StringUtils.isEmpty;

public class BaseTwitchApiClient {

    private final CloseableHttpClient client = HttpClients.createDefault();
    private final ObjectMapper mapper = new ObjectMapper().disable(FAIL_ON_UNKNOWN_PROPERTIES);

    protected <T> T get(String uri, Class<T> entityType, String token) {
        return call(new HttpGet(uri), entityType, token);
    }

    protected <T> T post(String uri, Class<T> entityType) {
        return call(new HttpPost(uri), entityType, null);
    }

    protected <T> T call(HttpUriRequest request, Class<T> entityType, String token) {
        if (!isEmpty(token)) {
            request.addHeader("Authorization", "OAuth " + token);
        }

        ResponseHandler<T> rh = new TwitchResponseHandler<>(entityType, mapper);

        try {
            return client.execute(request, rh);
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 401) {
                throw new TokenException();
            }
            if (e.getStatusCode() == 404) {
                throw new NotFoundException(e.getMessage());
            }
            throw new InternalException(e);
        } catch (IOException e) {
            throw new InternalException(e);
        }
    }


}
