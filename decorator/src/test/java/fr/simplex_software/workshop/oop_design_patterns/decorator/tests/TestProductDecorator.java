package fr.simplex_software.workshop.oop_design_patterns.decorator.tests;

import fr.simplex_software.workshop.common.*;
import fr.simplex_software.workshop.oop_design_patterns.decorator.*;
import fr.simplex_software.workshop.oop_design_patterns.decorator.Product;
import org.junit.jupiter.api.*;

import java.math.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestProductDecorator
{
  private final Product book = new BaseProduct(
    ProductType.BOOK.newInstance("Book1", "A book", new BigDecimal("100.00")));

  @Test
  public void testDecoratePriceAndDescription()
  {
    Product discounted = new Discounted(book);
    assertEquals(new BigDecimal("90.00"), discounted.price());
    assertEquals("A book (discounted)", discounted.description());
  }

  @Test
  public void testDecorateSimple()
  {
    Product discounted = new Discounted(book);
    assertEquals("Book1", discounted.name());
    assertEquals(ProductType.BOOK, discounted.type());
  }

  @Test
  public void testDecoratorsStack()
  {
    Product wrapped = new GiftWrapped(new Taxed(new Discounted(book)));
    assertEquals(new BigDecimal("113.00"), wrapped.price());
    assertEquals("A book (discounted) (VAT incl.) (gift-wrapped)", wrapped.description());
  }

  @Test
  public void testNullComponentIsRejected()
  {
    assertThrows(NullPointerException.class, () -> new Discounted(null));
    assertThrows(NullPointerException.class, () -> new BaseProduct(null));
  }
}
