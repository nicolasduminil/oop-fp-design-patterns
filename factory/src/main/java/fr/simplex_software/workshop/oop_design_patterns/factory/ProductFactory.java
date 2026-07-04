package fr.simplex_software.workshop.oop_design_patterns.factory;

import fr.simplex_software.workshop.common.*;

import java.math.*;
import java.util.*;

public class ProductFactory
{
  public static Product newProduct (String name, String description, BigDecimal price, ProductType productType)
  {
    Objects.requireNonNull(name, "Name is null");
    Objects.requireNonNull(description, "Description is null");
    Objects.requireNonNull(price, "Price is null");
    Objects.requireNonNull(productType, "Product type is null");
    return switch (productType)
    {
      case BOOK -> new BookProduct(name, description, price);
      case ELECTRONIC -> new ElectronicProduct(name, description, price);
      case FASHION -> new FashionProduct(name, description, price);
    };
  }
}
