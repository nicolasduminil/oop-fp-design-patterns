package fr.simplex_software.workshop.fp_design_patterns.factory;

import fr.simplex_software.workshop.oop_design_patterns.factory.*;

import java.math.*;

public record ElectronicProduct (String name, String description, BigDecimal price) implements Product
{
  public ElectronicProduct(String name, String description, BigDecimal price)
  {
    this.name = name;
    this.description = description;
    this.price = price;
  }

  public ProductType type()
  {
    return ProductType.ELECTRONIC;
  }
}
