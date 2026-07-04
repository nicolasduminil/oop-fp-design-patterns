package fr.simplex_software.workshop.common;

import java.math.*;

public record BookProduct (String name, String description, BigDecimal price) implements Product
{
  public ProductType type()
  {
    return ProductType.BOOK;
  }
}
