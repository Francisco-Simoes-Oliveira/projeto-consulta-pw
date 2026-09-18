package br.com.gestao.product;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="products")
public class Product {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=40) private String sku;
 @Column(nullable=false,length=120) private String name;
 @Column(length=80) private String category;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal costPrice;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal salePrice;
 @Column(nullable=false) private Integer stock=0;
 @Column(nullable=false) private Integer minStock=0;
 @Column(nullable=false) private boolean active=true;
 public Product(){}
 public Product(Long id,String sku,String name,String category,BigDecimal costPrice,BigDecimal salePrice,Integer stock,Integer minStock,boolean active){this.id=id;this.sku=sku;this.name=name;this.category=category;this.costPrice=costPrice;this.salePrice=salePrice;this.stock=stock;this.minStock=minStock;this.active=active;}
 public Long getId(){return id;} public String getSku(){return sku;} public void setSku(String v){sku=v;} public String getName(){return name;} public void setName(String v){name=v;} public String getCategory(){return category;} public void setCategory(String v){category=v;} public BigDecimal getCostPrice(){return costPrice;} public void setCostPrice(BigDecimal v){costPrice=v;} public BigDecimal getSalePrice(){return salePrice;} public void setSalePrice(BigDecimal v){salePrice=v;} public Integer getStock(){return stock;} public void setStock(Integer v){stock=v;} public Integer getMinStock(){return minStock;} public void setMinStock(Integer v){minStock=v;} public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
