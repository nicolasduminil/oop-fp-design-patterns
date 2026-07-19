package fr.simplex_software.workshop.oop_design_patterns.decorator;

import java.math.*;

public class Discounted extends ProductDecorator
{
  private static final BigDecimal RATE = new BigDecimal("0.10");

  public Discounted(Product product)
  {
    super(product);
  }

  public BigDecimal price()
  {
    return product.price().subtract(amount(product.price(), RATE));
  }

  public String description()
  {
    return product.description() + " (discounted)";
  }

  private static BigDecimal amount(BigDecimal price, BigDecimal rate)
  {
    return price.multiply(rate).setScale(2, RoundingMode.HALF_UP);
  }
}
