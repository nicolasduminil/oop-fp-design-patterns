package fr.simplex_software.workshop.oop_design_patterns.builder.tests;

import fr.simplex_software.workshop.builder.*;
import fr.simplex_software.workshop.builder.Order;
import fr.simplex_software.workshop.common.*;
import fr.simplex_software.workshop.oop_design_patterns.builder.*;
import org.junit.jupiter.api.*;

import java.math.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrderBuilder
{
  private final Product book =
    ProductType.BOOK.newInstance("Book1", "A book", new BigDecimal("20.00"));
  private final Product phone =
    ProductType.ELECTRONIC.newInstance("Phone", "A phone", new BigDecimal("100.00"));

  @Test
  public void testFullOrder()
  {
    Order order = OrderBuilder.of("Alice", "EUR")
      .addItem(book).addItem(phone)
      .coupon("SUMMER").giftWrap().note("Gift for Bob")
      .build();

    assertEquals("Alice", order.customer());
    assertEquals("EUR", order.currency());
    assertEquals(List.of(book, phone), order.items());
    assertEquals(Optional.of("SUMMER"), order.coupon());
    assertTrue(order.giftWrapped());
    assertEquals(Optional.of("Gift for Bob"), order.note());
    assertEquals(new BigDecimal("120.00"), order.subtotal());
  }

  @Test
  public void testMinimalOrderDefaultsOptionals()
  {
    Order order = OrderBuilder.of("Alice", "EUR").build();

    assertTrue(order.items().isEmpty());
    assertEquals(Optional.empty(), order.coupon());
    assertFalse(order.giftWrapped());
    assertEquals(Optional.empty(), order.note());
    assertEquals(BigDecimal.ZERO, order.subtotal());
  }

  @Test
  public void testResultIsImmutable()
  {
    Order order = OrderBuilder.of("Alice", "EUR").addItem(book).build();
    assertThrows(UnsupportedOperationException.class, () -> order.items().add(phone));
  }

  @Test
  public void testMissingRequiredArgumentThrows()
  {
    assertThrows(NullPointerException.class, () -> OrderBuilder.of(null, "EUR"));
    assertThrows(NullPointerException.class, () -> OrderBuilder.of("Alice", null));
  }
}
