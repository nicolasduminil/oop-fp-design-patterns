package fr.simplex_software.workshop.oop_design_patterns.visitor;

import fr.simplex_software.workshop.common.ProductType;

import java.math.*;

public interface Product
{
  String name();
  String description();
  BigDecimal price();
  ProductType type();
  <R> R accept(ProductVisitor<R> visitor);
}
