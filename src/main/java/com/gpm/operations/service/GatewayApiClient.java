package com.gpm.operations.service;

import com.gpm.operations.service.dto.RemoteUserDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GatewayApiClient {

    private final RestTemplate rt;

    public GatewayApiClient(@Qualifier("gatewayRestTemplate") RestTemplate rt) {
        this.rt = rt;
    }

    /** GET /api/users?page=0&size=20 (usually requires ROLE_ADMIN in JHipster) */
    public List<RemoteUserDTO> getUsers(int page, int size) {
        return rt
            .exchange(
                "/users?page={p}&size={s}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RemoteUserDTO>>() {},
                page,
                size
            )
            .getBody();
    }
}
