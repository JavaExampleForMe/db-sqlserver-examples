package com.example.search.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DBDetails {
    private String database;
    private String schema;
    private String username;
    private String password;
    private String host;
    private String port;

    public boolean verifyAllFieldsAreFull(){
        if ((database == null) || (schema == null) || (username == null) || (password == null) || (host == null) || (port == null))
            return false;
        return true;
    }
}
