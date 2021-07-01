package com.alconn.copang.review;

import com.alconn.copang.client.Client;
import com.alconn.copang.item.Item;
import com.alconn.copang.order.OrderItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
public class Review {

    @Id
    @GeneratedValue
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client writer;

    private boolean satisfied;

    @Lob
    private String content;

    private String image;

    private int rating;

    public void updateReview(String content, String image, Integer rating, Boolean satisfied) {
        this.content = content == null ? this.content : content;
        this.image = image == null ? this.image : image;
        this.rating = rating == null ? this.rating : rating;
        this.satisfied = satisfied == null ? this.satisfied : satisfied;
    }



    @JsonFormat(pattern = "yyyy.MM.dd", locale = "Seoul/Asia", shape = JsonFormat.Shape.STRING)
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime registerDate;

    @JsonFormat(pattern = "yyyy.MM.dd", locale = "Seoul/Asia", shape = JsonFormat.Shape.STRING)
    @UpdateTimestamp
    private LocalDateTime updatedDate;


}
