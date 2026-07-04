package fr.simplex_software.workshop.fp_design_patterns.visitor;

import fr.simplex_software.workshop.common.*;

import java.math.*;
import java.util.function.*;

public final class ProductOperations
{
  private ProductOperations() {}

  public static final Function<Product, BigDecimal> VAT = product -> switch (product)
  {
    case BookProduct(String name, String description, BigDecimal price) -> amount(price, "0.055");
    case ElectronicProduct(String name, String description, BigDecimal price) -> amount(price, "0.20");
    case FashionProduct(String name, String description, BigDecimal price) -> amount(price, "0.20");
  };

  public static final Function<Product, BigDecimal> SHIPPING = product -> switch (product)
  {
    case ElectronicProduct(String name, String description, BigDecimal price) ->
      new BigDecimal("10.00").add(price.multiply(new BigDecimal("0.02"))).setScale(2, RoundingMode.HALF_UP);
    case BookProduct b -> new BigDecimal("3.00");
    case FashionProduct f -> new BigDecimal("5.00");
  };

  public static final Function<Product, BigDecimal> DISCOUNT = product -> switch (product)
  {
    case ElectronicProduct(String name, String description, BigDecimal price) -> amount(price, "0.10");
    case BookProduct(String name, String description, BigDecimal price) -> amount(price, "0.05");
    case FashionProduct(String name, String description, BigDecimal price) -> amount(price, "0.15");
  };

  private static BigDecimal amount(BigDecimal price, String rate)
  {
    return price.multiply(new BigDecimal(rate)).setScale(2, RoundingMode.HALF_UP);
  }
}
