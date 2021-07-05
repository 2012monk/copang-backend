package com.alconn.copang.inquiry;

import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

//@Embeddable
public class TimeEntity {

    @CreationTimestamp
    private LocalDateTime registerDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;
}
