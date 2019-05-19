package com.example.search.impl.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class SearchTaskStatus {
    SearchTaskStatusEnum name;
    int code;

    public SearchTaskStatus(SearchTaskStatusEnum searchTaskStatusName) {
        this.name = searchTaskStatusName;
        this.code = searchTaskStatusName.getCode();
    }

    public SearchTaskStatus(int searchTaskStatusCode) {
        this.code = searchTaskStatusCode;
        this.name = SearchTaskStatusEnum.fromCode(searchTaskStatusCode);
    }

}