package fr.simplex_software.workshop.oop_design_patterns.visitor;

import fr.simplex_software.workshop.common.ProductType;

import java.math.*;

public record FashionProduct (String name, String description, BigDecimal price) implements Product
{
  public ProductType type()
  {
    return ProductType.FASHION;
  }

  public <R> R accept(ProductVisitor<R> visitor)
  {
    return visitor.visit(this);
  }
}
