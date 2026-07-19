package fr.simplex_software.workshop.oop_design_patterns.decorator;

import java.math.*;

public class Taxed extends ProductDecorator
{
  private static final BigDecimal RATE = new BigDecimal("0.20");

  public Taxed(Product product)
  {
    super(product);
  }

  public BigDecimal price()
  {
    return product.price().add(amount(product.price(), RATE));
  }

  public String description()
  {
    return product.description() + " (VAT incl.)";
  }

  private static BigDecimal amount(BigDecimal price, BigDecimal rate)
  {
    return price.multiply(rate).setScale(2, RoundingMode.HALF_UP);
  }
}
