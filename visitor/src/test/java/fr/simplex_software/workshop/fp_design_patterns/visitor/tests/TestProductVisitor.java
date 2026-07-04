package fr.simplex_software.workshop.fp_design_patterns.visitor.tests;

import fr.simplex_software.workshop.common.*;
import fr.simplex_software.workshop.fp_design_patterns.visitor.*;
import org.junit.jupiter.api.*;

import java.math.*;
import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestProductVisitor
{
  private final Product electronic = new ElectronicProduct("Phone", "A phone", new BigDecimal("100.00"));
  private final Product book = new BookProduct("Book1", "A book", new BigDecimal("100.00"));
  private final Product fashion = new FashionProduct("Shirt", "A shirt", new BigDecimal("100.00"));

  private final Function<Product, BigDecimal> netPrice = product ->
    product.price().subtract(ProductOperations.DISCOUNT.apply(product));

  // start from the net price (price - discount), then add VAT and shipping
  private final Function<Product, BigDecimal> orderTotal = product ->
    netPrice.apply(product)
      .add(ProductOperations.VAT.apply(product))
      .add(ProductOperations.SHIPPING.apply(product));

  private final Function<Product, BigDecimal> priceWithVat = product ->
    product.price().add(ProductOperations.VAT.apply(product));

  @Test
  public void testProductOperations()
  {
    assertEquals(new BigDecimal("20.00"), ProductOperations.VAT.apply(electronic));
    assertEquals(new BigDecimal("5.50"), ProductOperations.VAT.apply(book));
    assertEquals(new BigDecimal("20.00"), ProductOperations.VAT.apply(fashion));
    assertEquals(new BigDecimal("12.00"), ProductOperations.SHIPPING.apply(electronic));
    assertEquals(new BigDecimal("3.00"), ProductOperations.SHIPPING.apply(book));
    assertEquals(new BigDecimal("15.00"), ProductOperations.DISCOUNT.apply(fashion));
    assertNotEquals(ProductOperations.DISCOUNT.apply(electronic), ProductOperations.DISCOUNT.apply(fashion));
  }

  @Test
  public void testOperationComposition()
  {
    assertEquals("discount=15.00", ProductOperations.DISCOUNT
      .andThen(amount -> "discount=" + amount).apply(fashion));
  }

  @Test
  public void testVatViaLambdaBundle()
  {
    ProductVisitor<BigDecimal> vat = new ProductVisitor<>(
      e -> e.price().multiply(new BigDecimal("0.20")).setScale(2, RoundingMode.HALF_UP),
      b -> b.price().multiply(new BigDecimal("0.055")).setScale(2, RoundingMode.HALF_UP),
      f -> f.price().multiply(new BigDecimal("0.20")).setScale(2, RoundingMode.HALF_UP));
    assertEquals(new BigDecimal("20.00"), vat.visit(electronic));
    assertEquals(new BigDecimal("5.50"), vat.visit(book));
    assertEquals(new BigDecimal("20.00"), vat.visit(fashion));
  }

  @Test
  public void testLabelViaLambdaBundle()
  {
    ProductVisitor<String> label = new ProductVisitor<>(
      e -> "electronic:" + e.name(),
      b -> "book:" + b.name(),
      f -> "fashion:" + f.name());
    assertEquals("book:Book1", label.visit(book));
    assertNotEquals(label.visit(electronic), label.visit(book));
  }

  @Test
  public void testPriceWithVat()
  {
    assertEquals(new BigDecimal("120.00"), priceWithVat.apply(electronic));
    assertEquals(new BigDecimal("105.50"), priceWithVat.apply(book));
    assertEquals(new BigDecimal("120.00"), priceWithVat.apply(fashion));
  }

  @Test
  public void testOrderTotal()
  {
    assertEquals(new BigDecimal("122.00"), orderTotal.apply(electronic));
    assertEquals(new BigDecimal("103.50"), orderTotal.apply(book));
    assertEquals(new BigDecimal("110.00"), orderTotal.apply(fashion));
  }

  @Test
  public void testShoppingCartTotal()
  {
    List<Product> cart = List.of(electronic, book, fashion);
    BigDecimal total = cart.stream()
      .map(orderTotal)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
    assertEquals(new BigDecimal("335.50"), total);
  }
}
