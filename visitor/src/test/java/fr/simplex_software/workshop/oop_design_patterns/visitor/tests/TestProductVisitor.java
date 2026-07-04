package fr.simplex_software.workshop.oop_design_patterns.visitor.tests;

import fr.simplex_software.workshop.oop_design_patterns.visitor.*;
import org.junit.jupiter.api.*;

import java.math.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestProductVisitor
{
  private final Product electronic = new ElectronicProduct("Phone", "A phone", new BigDecimal("100.00"));
  private final Product book = new BookProduct("Book1", "A book", new BigDecimal("100.00"));
  private final Product fashion = new FashionProduct("Shirt", "A shirt", new BigDecimal("100.00"));

  private BigDecimal orderTotal(Product product)
  {
    // start from the net price (price - discount), then add VAT and shipping
    return netPrice(product)
      .add(product.accept(new VatVisitor()))
      .add(product.accept(new ShippingCostVisitor()));
  }

  private BigDecimal priceWithVat(Product product)
  {
    return product.price().add(product.accept(new VatVisitor()));
  }

  private BigDecimal netPrice(Product product)
  {
    return product.price().subtract(product.accept(new DiscountVisitor()));
  }

  private BigDecimal priceWithShipping(Product product)
  {
    return product.price().add(product.accept(new ShippingCostVisitor()));
  }

  @Test
  public void testLabelVisitorDispatchesPerType()
  {
    ProductVisitor<String> label = new ProductVisitor<>()
    {
      public String visit(ElectronicProduct p) { return "electronic:" + p.name(); }
      public String visit(BookProduct p) { return "book:" + p.name(); }
      public String visit(FashionProduct p) { return "fashion:" + p.name(); }
    };
    assertEquals("electronic:Phone", electronic.accept(label));
    assertEquals("book:Book1", book.accept(label));
    assertEquals("fashion:Shirt", fashion.accept(label));
    assertNotEquals(electronic.accept(label), book.accept(label));
  }

  @Test
  public void testPriceWithVat()
  {
    assertEquals(new BigDecimal("120.00"), priceWithVat(electronic));
    assertEquals(new BigDecimal("105.50"), priceWithVat(book));
    assertEquals(new BigDecimal("120.00"), priceWithVat(fashion));
  }

  @Test
  public void testNetPrice()
  {
    assertEquals(new BigDecimal("90.00"), netPrice(electronic));
    assertEquals(new BigDecimal("95.00"), netPrice(book));
    assertEquals(new BigDecimal("85.00"), netPrice(fashion));
  }

  @Test
  public void testPriceWithShipping()
  {
    assertEquals(new BigDecimal("112.00"), priceWithShipping(electronic));
    assertEquals(new BigDecimal("103.00"), priceWithShipping(book));
    assertEquals(new BigDecimal("105.00"), priceWithShipping(fashion));
  }

  @Test
  public void testOrderTotal()
  {
    assertEquals(new BigDecimal("122.00"), orderTotal(electronic));
    assertEquals(new BigDecimal("103.50"), orderTotal(book));
    assertEquals(new BigDecimal("110.00"), orderTotal(fashion));
  }

  @Test
  public void testShoppingCartTotal()
  {
    List<Product> cart = List.of(electronic, book, fashion);
    BigDecimal total = cart.stream()
      .map(this::orderTotal)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
    assertEquals(new BigDecimal("335.50"), total);
  }

  @Test
  public void testNullVisitorIsRejected()
  {
    assertThrows(NullPointerException.class, () -> book.accept(null));
  }
}
