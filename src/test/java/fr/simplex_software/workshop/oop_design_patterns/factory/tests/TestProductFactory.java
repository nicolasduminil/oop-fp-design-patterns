package fr.simplex_software.workshop.oop_design_patterns.factory.tests;

import fr.simplex_software.workshop.oop_design_patterns.factory.*;
import org.junit.jupiter.api.*;

import java.math.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestProductFactory
{
  @Test
  public void testProductFactory()
  {
    Product product = ProductFactory.newProduct("Book1", "A book",
      new BigDecimal("20.50"), ProductType.BOOK);
    assertTrue (product instanceof BookProduct);
  }

}
