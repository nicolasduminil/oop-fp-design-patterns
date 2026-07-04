package fr.simplex_software.workshop.oop_design_patterns.visitor;

import java.math.*;

public class VatVisitor implements ProductVisitor<BigDecimal>
{
  private static final BigDecimal STANDARD_RATE = new BigDecimal("0.20");
  private static final BigDecimal REDUCED_RATE = new BigDecimal("0.055");

  public BigDecimal visit(ElectronicProduct product)
  {
    return vat(product.price(), STANDARD_RATE);
  }

  public BigDecimal visit(BookProduct product)
  {
    return vat(product.price(), REDUCED_RATE);
  }

  public BigDecimal visit(FashionProduct product)
  {
    return vat(product.price(), STANDARD_RATE);
  }

  private static BigDecimal vat(BigDecimal price, BigDecimal rate)
  {
    return price.multiply(rate).setScale(2, RoundingMode.HALF_UP);
  }
}
