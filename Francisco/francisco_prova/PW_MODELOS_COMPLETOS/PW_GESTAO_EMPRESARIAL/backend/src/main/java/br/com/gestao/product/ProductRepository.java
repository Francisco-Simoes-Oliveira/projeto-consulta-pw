package br.com.gestao.product; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ProductRepository extends JpaRepository<Product,Long>{ Optional<Product> findBySkuIgnoreCase(String sku); List<Product> findByNameContainingIgnoreCaseOrderByName(String name); }
