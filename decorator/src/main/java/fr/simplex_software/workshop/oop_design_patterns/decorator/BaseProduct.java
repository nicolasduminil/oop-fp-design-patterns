package fr.simplex_software.workshop.oop_design_patterns.decorator;

import fr.simplex_software.workshop.common.ProductType;

import java.math.*;
import java.util.*;

public record BaseProduct(fr.simplex_software.workshop.common.Product product) implements Product
{
  public BaseProduct
  {
    Objects.requireNonNull(product, "Product is null");
  }

  public String name()
  {
    return product.name();
  }

  public String description()
  {
    return product.description();
  }

  public BigDecimal price()
  {
    return product.price();
  }

  public ProductType type()
  {
    return product.type();
  }
}
