package com.example.search.impl.model;

import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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
