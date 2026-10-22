package com.sideby.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsrfTokenResponse {
    private String headerName;
    private String token;

    public CsrfTokenResponse() {
    }

    public CsrfTokenResponse(String headerName, String token) {
        this.headerName = headerName;
        this.token = token;
    }
}
