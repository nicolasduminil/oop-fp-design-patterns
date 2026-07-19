package fr.simplex_software.workshop.fp_design_patterns.decorator.tests;

import fr.simplex_software.workshop.common.*;
import fr.simplex_software.workshop.common.Product;
import fr.simplex_software.workshop.oop_design_patterns.decorator.*;
import org.junit.jupiter.api.*;

import java.math.*;
import java.util.function.*;

import static fr.simplex_software.workshop.fp_design_patterns.decorator.ProductDecorators.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestProductDecorator
{
  private final Product book =
    ProductType.BOOK.newInstance("Book1", "A book", new BigDecimal("100.00"));

  @Test
  public void testComposedDecorators()
  {
    Function<Product, Product> decorate = DISCOUNTED.andThen(TAXED).andThen(GIFT_WRAPPED);
    Product wrapped = decorate.apply(book);

    assertEquals(new BigDecimal("113.00"), wrapped.price());
    assertEquals("A book (discounted) (VAT incl.) (gift-wrapped)", wrapped.description());
    assertEquals(ProductType.BOOK, wrapped.type());
  }


  @Test
  public void testMatchesOopDecoratorResult()
  {
    Product fp = DISCOUNTED.andThen(TAXED).andThen(GIFT_WRAPPED).apply(book);
    fr.simplex_software.workshop.oop_design_patterns.decorator.Product oop =
      new GiftWrapped(new Taxed(new Discounted(new BaseProduct(book))));
    assertEquals(oop.price(), fp.price());
    assertEquals(oop.description(), fp.description());
  }

  @Test
  public void testNullComponentIsRejected()
  {
    assertThrows(NullPointerException.class, () -> DISCOUNTED.apply(null));
  }
}
