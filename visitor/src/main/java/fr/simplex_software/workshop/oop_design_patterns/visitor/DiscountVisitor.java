package fr.simplex_software.workshop.oop_design_patterns.visitor;

import java.math.*;

public class DiscountVisitor implements ProductVisitor<BigDecimal>
{
  private static final BigDecimal ELECTRONIC_RATE = new BigDecimal("0.10");
  private static final BigDecimal BOOK_RATE = new BigDecimal("0.05");
  private static final BigDecimal FASHION_RATE = new BigDecimal("0.15");

  public BigDecimal visit(ElectronicProduct product)
  {
    return discount(product.price(), ELECTRONIC_RATE);
  }

  public BigDecimal visit(BookProduct product)
  {
    return discount(product.price(), BOOK_RATE);
  }

  public BigDecimal visit(FashionProduct product)
  {
    return discount(product.price(), FASHION_RATE);
  }

  private static BigDecimal discount(BigDecimal price, BigDecimal rate)
  {
    return price.multiply(rate).setScale(2, RoundingMode.HALF_UP);
  }
}
