package fr.simplex_software.workshop.fp_design_patterns.factory.tests;

import fr.simplex_software.workshop.fp_design_patterns.factory.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.math.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestProductFactory
{
  private final static ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  Function<Product, Product> showThePrice = p ->
  {
    System.out.print ("The product price is %s".formatted(p.price().toString()));
    return p;
  };

  @BeforeAll
  public static void setUpStreams()
  {
    System.setOut(new PrintStream(outContent));
  }

  @AfterAll
  public static void restoreStreams() {
    System.setOut(System.out);
  }

  @Test
  public void testProductFactory()
  {
    Product product = ProductType.BOOK.newInstance("Book1",
      "A book", new BigDecimal("20.45"));
    assertTrue(product instanceof BookProduct);
    assertEquals(ProductType.BOOK, product.type());
    assertFalse(product instanceof ElectronicProduct);
    assertFalse(product instanceof FashionProduct);
  }

  @Test
  public void testComposition()
  {
    ProductType.BOOK.factory.andThen(showThePrice).apply("Book1",
      "A book", new BigDecimal("20.45"));
    assertEquals("The product price is 20.45", outContent.toString());
  }
}
