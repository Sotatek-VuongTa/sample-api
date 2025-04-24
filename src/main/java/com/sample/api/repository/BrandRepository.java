package com.sample.api.repository;

import com.sample.api.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query("""
        SELECT b FROM Brand b where b.id = :id
    """)
    Brand getBrandById(@Param("id") Long id);

    @Query("""
        SELECT b FROM Brand b where b.brandName = :name
    """)
    Optional<Brand> findBandByBrandName(@Param("name") String brand);
}
