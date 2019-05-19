package com.example.search.impl.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class QueryParam {
    String name;
    String value;
    QueryParamType type;

    public QueryParam(String name, String value, QueryParamType type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public QueryParam(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = QueryParamType.valueOf(type);
    }

}
