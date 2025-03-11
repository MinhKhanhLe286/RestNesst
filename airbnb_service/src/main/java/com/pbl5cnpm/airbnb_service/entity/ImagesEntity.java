package com.pbl5cnpm.airbnb_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "images")
public class ImagesEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String imageUrl;

    @Column(columnDefinition = "boolean default false")
    Boolean deleted;
    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = true) //  Đảm bảo có tên cột chính xác
    private ListingEntity listingEntity; //  Thêm thuộc tính này để khớp với `mappedBy`
}
