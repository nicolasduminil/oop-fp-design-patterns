package fr.simplex_software.workshop.fp_design_patterns.factory;

import fr.simplex_software.workshop.oop_design_patterns.factory.*;

import java.math.*;

public record BookProduct(String name, String description, BigDecimal price) implements Product
{
  public ProductType type()
  {
    return ProductType.BOOK;
  }
}
