package com.example.search.impl.api;

import com.example.search.api.SearchFilter;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class SearchRequest {

    @NotNull
    private int callerId;

    @NotNull
    private SearchFilter searchFilter;

}
