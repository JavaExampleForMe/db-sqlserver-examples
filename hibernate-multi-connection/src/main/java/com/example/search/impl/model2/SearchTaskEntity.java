package com.example.search.impl.model2;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "searchtask", uniqueConstraints={@UniqueConstraint(columnNames = {"SourceRequestId"})})
@EqualsAndHashCode(exclude = {"searchTaskId"}, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchTaskEntity  extends BaseEntity implements Serializable {
    @Column(name="SearchTaskId", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private int searchTaskId;

    @Column(name="SourceRequestId", nullable = false)
    private int sourceRequestId;

    @Column(name="SearchTaskStatusId", nullable = false)
    private int searchTaskStatusId;

    @Column(name="SearchTaskProgress", nullable = false)
    private int searchTaskProgress;

    @Column(name = "SearchTaskDefinition", columnDefinition = "nvarchar(MAX)")
    private String searchTaskDefinition;
}
