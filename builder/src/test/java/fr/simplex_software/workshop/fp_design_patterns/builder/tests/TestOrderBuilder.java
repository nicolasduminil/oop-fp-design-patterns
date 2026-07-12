package fr.simplex_software.workshop.fp_design_patterns.builder.tests;

import fr.simplex_software.workshop.builder.*;
import fr.simplex_software.workshop.builder.Order;
import fr.simplex_software.workshop.common.*;
import org.junit.jupiter.api.*;

import java.math.*;
import java.util.*;
import java.util.function.*;

import static fr.simplex_software.workshop.fp_design_patterns.builder.OrderBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestOrderBuilder
{
  private final Product book =
    ProductType.BOOK.newInstance("Book1", "A book", new BigDecimal("20.00"));
  private final Product phone =
    ProductType.ELECTRONIC.newInstance("Phone", "A phone", new BigDecimal("100.00"));

  @Test
  public void testComposedSteps()
  {
    Function<Order, Order> config = addItem(book)
      .andThen(addItem(phone))
      .andThen(coupon("SUMMER"))
      .andThen(giftWrap())
      .andThen(note("Gift for Bob"));

    Order order = config.apply(empty("Alice", "EUR"));

    assertEquals("Alice", order.customer());
    assertEquals(List.of(book, phone), order.items());
    assertEquals(Optional.of("SUMMER"), order.coupon());
    assertTrue(order.giftWrapped());
    assertEquals(Optional.of("Gift for Bob"), order.note());
    assertEquals(new BigDecimal("120.00"), order.subtotal());
  }

  @Test
  public void testStepIsAReusableValue()
  {
    UnaryOperator<Order> addBook = addItem(book);
    Order order = addBook.andThen(addBook).apply(empty("Alice", "EUR"));
    assertEquals(List.of(book, book), order.items());
  }

  @Test
  public void testStepsDoNotMutateTheBase()
  {
    Order base = empty("Alice", "EUR");
    addItem(book).apply(base);
    assertTrue(base.items().isEmpty(), "the base order must stay untouched");
  }

  @Test
  public void testMatchesOopBuilderResult()
  {
    Order fp = addItem(book).andThen(giftWrap()).apply(empty("Alice", "EUR"));
    Order oop = fr.simplex_software.workshop.oop_design_patterns.builder.OrderBuilder
      .of("Alice", "EUR").addItem(book).giftWrap().build();
    assertEquals(oop, fp);
  }

  @Test
  public void testMissingRequiredArgumentThrows()
  {
    assertThrows(NullPointerException.class, () -> empty(null, "EUR"));
    assertThrows(NullPointerException.class, () -> addItem(null));
  }
}
