package fr.simplex_software.workshop.oop_design_patterns.visitor;

import java.math.*;

public class ShippingCostVisitor implements ProductVisitor<BigDecimal>
{
  private static final BigDecimal ELECTRONIC_BASE = new BigDecimal("10.00");
  private static final BigDecimal INSURANCE_RATE = new BigDecimal("0.02");
  private static final BigDecimal BOOK_FLAT = new BigDecimal("3.00");
  private static final BigDecimal FASHION_FLAT = new BigDecimal("5.00");

  public BigDecimal visit(ElectronicProduct product)
  {
    return ELECTRONIC_BASE.add(product.price().multiply(INSURANCE_RATE))
      .setScale(2, RoundingMode.HALF_UP);
  }

  public BigDecimal visit(BookProduct product)
  {
    return BOOK_FLAT;
  }

  public BigDecimal visit(FashionProduct product)
  {
    return FASHION_FLAT;
  }
}
