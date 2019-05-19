package com.example.search.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class SearchToken {
    @Getter
    @Setter
    private int searchTaskId;
}
