package com.example.AuthenticationService.Objects;

public class oAuthResponse {
    private String access_token;
    private String token_type;
    private Integer expires_in;
    private String scope;
    private String id_token;
    private String refresh_token;

    public oAuthResponse(){}

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public String getScope() {
        return scope;
    }

    public String getId_token() {
        return id_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public oAuthResponse(String refresh_token, String access_token, String token_type, Integer expires_in, String scope, String id_token) {
        this.refresh_token = refresh_token;
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.scope = scope;
        this.id_token = id_token;
    }
}
