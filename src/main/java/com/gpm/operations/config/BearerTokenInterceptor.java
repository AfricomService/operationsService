package com.gpm.operations.config;

import com.gpm.operations.service.RemoteAuthService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BearerTokenInterceptor implements ClientHttpRequestInterceptor {

    private final RemoteAuthService auth;

    public BearerTokenInterceptor(RemoteAuthService auth) {
        this.auth = auth;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().setBearerAuth(auth.getToken());
        ClientHttpResponse response = execution.execute(request, body);

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            response.close();
            auth.invalidate();
            request.getHeaders().setBearerAuth(auth.getToken());
            return execution.execute(request, body);
        }
        return response;
    }
}
