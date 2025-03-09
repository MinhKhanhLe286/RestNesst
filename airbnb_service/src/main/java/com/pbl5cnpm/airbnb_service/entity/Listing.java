package com.pbl5cnpm.airbnb_service.entity;

import java.util.List;

import com.pbl5cnpm.airbnb_service.enums.StatusRoom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "listings")
public class Listing extends BaseEntity {
    @Id
    @GeneratedValue (strategy= GenerationType.IDENTITY)
    Long  Id;
    String title ;
    @Column(columnDefinition = "TEXT")
    String descripton;
    String address;
    String city;
    String county;
    Double price;
    Double area;
    @Enumerated(EnumType.STRING)
    StatusRoom status;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private UserEntity host;
    
    @ManyToMany
    @JoinTable(
        name = "listing_categories",
        joinColumns = @JoinColumn(name = "listing_id"),
        inverseJoinColumns = @JoinColumn(name = "categorie_id")
    )
    List<Categories> categories;

    @ManyToMany
    @JoinTable(
        name = "listing_amenites",
        joinColumns = @JoinColumn(name = "listing_id"),
        inverseJoinColumns = @JoinColumn(name = "amenites_id")
    )
    List<Amenites> amenites;

    @OneToMany(mappedBy = "listing")
    private List<Images> images;
}
