package fr.simplex_software.workshop.oop_design_patterns.builder;

import fr.simplex_software.workshop.builder.*;
import fr.simplex_software.workshop.common.*;

import java.util.*;

public final class OrderBuilder
{
  private final String customer;
  private final String currency;
  private final List<Product> items = new ArrayList<>();
  private String coupon;
  private boolean giftWrapped;
  private String note;

  private OrderBuilder(String customer, String currency)
  {
    this.customer = Objects.requireNonNull(customer, "Customer is null");
    this.currency = Objects.requireNonNull(currency, "Currency is null");
  }

  public static OrderBuilder of(String customer, String currency)
  {
    return new OrderBuilder(customer, currency);
  }

  public OrderBuilder addItem(Product item)
  {
    items.add(Objects.requireNonNull(item, "Item is null"));
    return this;
  }

  public OrderBuilder coupon(String coupon)
  {
    this.coupon = coupon;
    return this;
  }

  public OrderBuilder giftWrap()
  {
    this.giftWrapped = true;
    return this;
  }

  public OrderBuilder note(String note)
  {
    this.note = note;
    return this;
  }

  public Order build()
  {
    return new Order(customer, currency, items,
      Optional.ofNullable(coupon), giftWrapped, Optional.ofNullable(note));
  }
}
