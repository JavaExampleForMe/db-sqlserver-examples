package com.example.search.impl.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SearchTaskBatch")
@EqualsAndHashCode(exclude = {"batchId"}, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchTaskBatchEntity extends BaseEntity implements Serializable {
    @Id
    @Column(name="BatchId", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int batchId;

    @Column(name="SearchTaskId", nullable = false)
    private int searchTaskId;

    @Column(name="SourceRequestId", nullable = false)
    private int sourceRequestId;

    @Column(name="SourceRequestTypeId", nullable = false)
    private int sourceRequestTypeId;

    @Column(name="BatchStatusId", nullable = false)
    private int batchStatusId;

    @Column(name="BatchResultsNumRows", nullable = false)
    private int batchResultsNumRows;

    @Column(name = "BatchQueryDefinition", columnDefinition = "nvarchar(MAX)")
    private String batchQueryDefinition;
}
