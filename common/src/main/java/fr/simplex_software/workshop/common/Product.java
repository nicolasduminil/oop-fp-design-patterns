package fr.simplex_software.workshop.common;

import java.math.*;

public sealed interface Product permits ElectronicProduct, BookProduct, FashionProduct
{
  String name();
  String description();
  BigDecimal price();
  ProductType type();
}
