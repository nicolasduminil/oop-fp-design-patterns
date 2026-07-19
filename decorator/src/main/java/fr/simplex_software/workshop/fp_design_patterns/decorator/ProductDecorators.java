package fr.simplex_software.workshop.fp_design_patterns.decorator;

import fr.simplex_software.workshop.common.*;

import java.math.*;
import java.util.function.*;

public final class ProductDecorators
{
  private ProductDecorators() {}

  public static final UnaryOperator<Product> DISCOUNTED = product ->
    product.type().newInstance(product.name(),
      product.description() + " (discounted)",
      product.price().subtract(amount(product.price(), "0.10")));

  public static final UnaryOperator<Product> TAXED = product ->
    product.type().newInstance(product.name(),
      product.description() + " (VAT incl.)",
      product.price().add(amount(product.price(), "0.20")));

  public static final UnaryOperator<Product> GIFT_WRAPPED = product ->
    product.type().newInstance(product.name(),
      product.description() + " (gift-wrapped)",
      product.price().add(new BigDecimal("5.00")));

  private static BigDecimal amount(BigDecimal price, String rate)
  {
    return price.multiply(new BigDecimal(rate)).setScale(2, RoundingMode.HALF_UP);
  }
}
