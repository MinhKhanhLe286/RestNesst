package com.pbl5cnpm.airbnb_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "images")
public class ImagesEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    String imageUrl;

    @ManyToOne
    @JoinColumn(name = "listing_id") //  Đảm bảo có tên cột chính xác
    private ListingEntity listing; //  Thêm thuộc tính này để khớp với `mappedBy`
}
