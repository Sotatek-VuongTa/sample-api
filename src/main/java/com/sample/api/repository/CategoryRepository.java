package com.sample.api.repository;

import com.sample.api.entity.Category;
import com.sample.api.projection.CheapestComboProjection;
import com.sample.api.projection.LowestPriceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = """
    SELECT 
        p.category, p.brand, p.cost 
    FROM (
        SELECT DISTINCT ON (c.category_name)
            c.category_name AS category,
            b.brand_name AS brand,
            c.cost AS cost,
            c.priority_order AS p_order
        FROM categories c
        JOIN brands b ON c.brand_id = b.id
        ORDER BY c.category_name, c.cost ASC
    ) AS p
    ORDER BY p.p_order
    """, nativeQuery = true)
    List<CheapestComboProjection> findCheapestCombo();

    @Query("""
        SELECT sum(c.cost) as totalCost, c.brand.id as brandId FROM Category c group by (c.brand)
    """)
    List<LowestPriceProjection> getLowestPrice();

    @Query("""
        SELECT c FROM Category c where LOWER(c.categoryName) = LOWER(:categoryName)
    """)
    List<Category> getCategoriesByCategoryName(@Param("categoryName") String category);

    @Query("""
        SELECT max(c.pOrder) FROM Category c
    """)
    Long findMaxPOder();
}
