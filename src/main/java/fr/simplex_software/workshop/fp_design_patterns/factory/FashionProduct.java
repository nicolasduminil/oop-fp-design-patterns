package fr.simplex_software.workshop.fp_design_patterns.factory;

import java.math.*;

public record FashionProduct(String name, String description, BigDecimal price) implements Product
{
  public ProductType type()
  {
    return ProductType.FASHION;
  }
}
