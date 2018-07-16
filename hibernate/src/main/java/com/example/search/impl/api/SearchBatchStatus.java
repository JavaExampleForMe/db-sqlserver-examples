package com.example.search.impl.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class SearchBatchStatus {
    SearchBatchStatusEnum name;
    int code;

    public SearchBatchStatus(SearchBatchStatusEnum searchBatchStatusName) {
        this.name = searchBatchStatusName;
        this.code = searchBatchStatusName.getCode();
    }

    public SearchBatchStatus(int searchBatchStatusCode) {
        this.code = searchBatchStatusCode;
        this.name = SearchBatchStatusEnum.fromCode(searchBatchStatusCode);
    }

}