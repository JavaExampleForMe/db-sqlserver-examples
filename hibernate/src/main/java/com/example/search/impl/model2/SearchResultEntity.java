package com.example.search.impl.model2;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Table(name = "SearchResults")
public class SearchResultEntity extends BaseEntity implements Serializable {

    @EmbeddedId
    private SearchResultIdEntity searchResultIdEntity;

    @Column(name = "SearchTaskId", nullable = false)
    private int searchTaskId;

    @Column(name = "SourceRequestId", nullable = false)
    private int sourceRequestId;

    @Column(name = "iInteractionID", nullable = false)
    private long interactionID;


    @Column(name = "iInitiatorUserID", nullable = false)
    private int userId;

}
