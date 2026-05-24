package fr.simplex_software.workshop.fp_design_patterns.factory;

import io.vavr.*;

import java.math.*;
import java.util.*;

public enum ProductType
{
  ELECTRONIC(ElectronicProduct::new),
  FASHION(FashionProduct::new),
  BOOK(BookProduct::new);

  public final Function3<String, String, BigDecimal, Product> factory;

  ProductType (Function3<String, String, BigDecimal, Product> factory)
  {
    this.factory = factory;
  }

  public Product newInstance (String name, String description, BigDecimal price)
  {
    Objects.requireNonNull(name, "Name is null");
    Objects.requireNonNull(description, "Description is null");
    Objects.requireNonNull(price, "Price is null");
    return this.factory.apply (name, description, price);
  }
}
