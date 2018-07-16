package com.example.search.impl.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class BatchQueryDefinition {
    private String query;
    private QueryParam[] params;
}
