package fr.simplex_software.workshop.oop_design_patterns.decorator;

import java.math.*;

public class GiftWrapped extends ProductDecorator
{
  private static final BigDecimal FEE = new BigDecimal("5.00");

  public GiftWrapped(Product product)
  {
    super(product);
  }

  public BigDecimal price()
  {
    return product.price().add(FEE);
  }

  public String description()
  {
    return product.description() + " (gift-wrapped)";
  }
}
