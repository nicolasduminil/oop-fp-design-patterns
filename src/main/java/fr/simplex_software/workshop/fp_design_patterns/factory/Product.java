package fr.simplex_software.workshop.fp_design_patterns.factory;

import java.math.*;

public interface Product
{
  String name();
  String description();
  BigDecimal price();
  ProductType type();
}
