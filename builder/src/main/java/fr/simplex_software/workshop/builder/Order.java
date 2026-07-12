package fr.simplex_software.workshop.builder;

import fr.simplex_software.workshop.common.*;

import java.math.*;
import java.util.*;

public record Order(
  String customer,
  String currency,
  List<Product> items,
  Optional<String> coupon,
  boolean giftWrapped,
  Optional<String> note)
{
  public Order
  {
    Objects.requireNonNull(customer, "Customer is null");
    Objects.requireNonNull(currency, "Currency is null");
    items = items == null ? List.of() : List.copyOf(items);
    coupon = coupon == null ? Optional.empty() : coupon;
    note = note == null ? Optional.empty() : note;
  }

  public BigDecimal subtotal()
  {
    return items.stream()
      .map(Product::price)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
