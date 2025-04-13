package com.pbl5cnpm.airbnb_service.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "listings")
public class ListingEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    @Column(columnDefinition = "TEXT")
    String description;
    String address;
    String city;
    Double price;
    String area;
    Boolean isActive;
    Boolean access;
    Boolean deleted; 
    String avgStart;
    Boolean popular;
    LocalDate startDate;
    LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "host_id")
    private UserEntity host;
    
    @ManyToMany
    @JoinTable(
        name = "listing_categories",
        joinColumns = @JoinColumn(name = "listing_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    List<CategoriesEntity> categoriesEntities;

    @ManyToMany
    @JoinTable(
        name = "listing_amenites",
        joinColumns = @JoinColumn(name = "listing_id"),
        inverseJoinColumns = @JoinColumn(name = "amenites_id")
    )
    List<AmenitesEntity> amenitesEntities; 
    
    @OneToMany(mappedBy = "listingEntity", cascade = CascadeType.ALL)
    List<ImagesEntity> imagesEntities;

    @ManyToOne
    @JoinColumn(name = "country_id")
    CountriesEntity countriesEntity;
}
