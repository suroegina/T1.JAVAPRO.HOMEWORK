package org.example.homeworks.users.repository;

import org.example.homeworks.users.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.user.id = :userId")
    List<Product> findByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findById(@Param("id") Long id);
}
