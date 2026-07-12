package fr.simplex_software.workshop.fp_design_patterns.builder;

import fr.simplex_software.workshop.builder.*;
import fr.simplex_software.workshop.common.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public final class OrderBuilder
{
  private OrderBuilder() {}

  public static Order empty(String customer, String currency)
  {
    return new Order(customer, currency, List.of(), Optional.empty(), false, Optional.empty());
  }

  public static UnaryOperator<Order> addItem(Product item)
  {
    Objects.requireNonNull(item, "Item is null");
    return order -> new Order(order.customer(), order.currency(),
      Stream.concat(order.items().stream(), Stream.of(item)).toList(),
      order.coupon(), order.giftWrapped(), order.note());
  }

  public static UnaryOperator<Order> coupon(String coupon)
  {
    return order -> new Order(order.customer(), order.currency(), order.items(),
      Optional.ofNullable(coupon), order.giftWrapped(), order.note());
  }

  public static UnaryOperator<Order> giftWrap()
  {
    return order -> new Order(order.customer(), order.currency(), order.items(),
      order.coupon(), true, order.note());
  }

  public static UnaryOperator<Order> note(String note)
  {
    return order -> new Order(order.customer(), order.currency(), order.items(),
      order.coupon(), order.giftWrapped(), Optional.ofNullable(note));
  }
}
