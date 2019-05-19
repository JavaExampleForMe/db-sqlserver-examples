package com.example.search.impl.model2;

import com.example.search.impl.api.LocalDateTimeUserType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
@MappedSuperclass
public class SearchResultIdEntity implements Serializable {

    @Column(name = "BatchId", nullable = false)
    private int batchId;

    @Type(type = LocalDateTimeUserType.TYPE_NAME)
    @Column(name = "dtContactGMTStartTime", columnDefinition = "DATETIME", nullable = true, unique = false)
    protected LocalDateTime contactGMTStartTime;

    @Column(name = "iArchiveID", nullable = false)
    private long archiveId;

}
