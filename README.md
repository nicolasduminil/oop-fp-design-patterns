# Rethinking Java Design Patterns: from OOP to FP

Functional Programming answer, to those who wonder how to integrate or combine 
it with Object-Oriented Programming, is usually: *Turtles all the way down*.

This is an aphorism which origin is credited to Richard Feynman. In his book,
*Surely You're Joking, Mr. Feynman !*, published in 1985, he tells the story 
of one of his conferences on the nature on the universe, where he has been challenged
by someone in the audience, saying that the universe rests on a turtle. Feynman
asked then what the turtle is resting on and the answer was: "another bigger turtle".
And when he smugly asked what the bigger turtle is resting on, the attendee said:
"It's turtles all the way down, you can't trick me !"

This metaphor is often used in the context of the Functional Programming to describe
an infinite series of entities governed by a recursive principle. And it's also
the answer of the Functional Programming to developers coming from an Object-Oriented
mindset: "just do functional all the way down". But, in order to adopt a more systematic
approach of combining object-oriented principles in a functional style, a more 
practical answer is required and this is what I'm trying to do here.

We, as developers, fortunatelly don't have to reinvent the wheel. All the problems
are solved nowadays, especially since the LLM agents became the most common 
digital infrastructure. But as surprizing as it might seem to our younger 
colleagues, who can't live 48 hours without AI, even before LLMs, a general 
approach fitting solutions to problems existed, in the form of design patterns.

As a matter of fact, the Object-Oriented Programming proposes repeatable solutions
tested, proven and formalized, called design patterns, that you most likely already
used, even if you aren't mandatory aware that you did.

The *Gang of Four* classified these patterns in three groups:

  - *Behavioral patterns* which deal with responsibilities and communication between objects.
  - *Creational patterns* that abstract the objects creation / instantiation process.
  - *Structural patterns* that compose objects such that to form larger or enhanced ones.

Let's take one of the most commonly used patterns in each category and see how 
to combine their object-oriented inherent nature to a more functional approach.

## The Factory

This design pattern belongs to the creational category and its purpose is to 
instantiate objects without exposing implementation details.

### The object-oriented approach

The figure below shows the class diagram of a factory design pattern:

![factory-oop](factory-oop.png "OOP Factory")

Our scenario here is a simple one: a `Product` interface implemented by three 
classes: `BookProduct`, `ElectronicProduct` and `FashionProduct`. They can be 
created through the `ProductFactory` class, as follows:

    public class ProductFactory
    {
      public static Product newProduct (String name, String description, BigDecimal price, ProductType productType)
      {
        Objects.requireNonNull(name, "Name is null");
        ...
        return switch (productType)
        {
          case BOOK -> new BookProduct(name, description, price);
          case ELECTRONIC -> new ElectronicProduct(name, description, price);
          case FASHION -> new FashionProduct(name, description, price);
          default -> throw new IllegalArgumentException ("Unknown type: %s".formatted(productType));
        };
      }
    }

Using this factory, it's very easy to create a `BookProduct`, for example, while
avoiding to expose implementation details:

        ...
        Product product = ProductFactory.newProduct("Book1", "A book",
          new BigDecimal("20.50"), ProductType.BOOK);
        ...

As you probably noticed, the `ProductType` enumerated defines the three categories.
If a new product is to be introduced, the factory has to be modified such that to
reflect this business changement. And this interdependence of the factory and the
enumerated makes the whole approach fragile.

In order to reduce this fragility, we need to introduce a compile-time validation 
with a more functional approach.

### The functional approach

Our example is an over-simplified case of a product management system. The presented
factory instantiate different simple records having the same arguments. These 
identical constructors give us the possibility to move the factory directly into
the `ProductType` enumerated, such that any new product automatically requires 
a correspondent factory.

Java `enum` types are based on constant names, but we can attach to each one its 
correspondent value. Or, even better, a factory function for creating discrete 
products. Look at that:

    public enum ProductType
    {
      ELECTRONIC(ElectronicProduct::new),
      FASHION(FashionProduct::new),
      BOOK(BookProduct::new);

      public final TriFunction<String, String, BigDecimal, Product> factory;

      ProductType (TriFunction<String, String, BigDecimal, Product> factory)
      {
        this.factory = factory;
      }

      public Product newInstance (String name, String description, BigDecimal price)
      {
        Objects.requireNonNull(name, "Name is null");
        ...
        return this.factory.apply (name, description, price);
      }
    }

Now, creating a new `Product` instance is easier:

    Product product = ProductType.BOOK.newInstance("Book1",
      "A book", new BigDecimal("20.45"));

The public property `factory` seems redundant now that a dedicated method for the
instance creation is available. But it provides a very convenient functional way
to interact further with the factory. For example:

    ProductType.BOOK.factory.andThen(showThePrice).apply("Book1",
      "A book", new BigDecimal("20.45"));

as shown in the `TestProductFactory` class, in the `fp_design_paterns.factory` 
package. Of course, given that our products need three arguments constructors and
since Java doesn't provide an equivalent of the `BiFunction` class, but with three
input arguments, you will need to craft a `TriFunction` class, as shown below:

    @FunctionalInterface
    public interface TriFunction<A, B, C, R>
    {
      R apply(A a, B b, C c);
      default <K> TriFunction<A, B, C, K> andThen(Function<? super R, ? extends K> f) 
      {
        Objects.requireNonNull(f);
        return (A a, B b, C c) -> f.apply(apply(a, b, c));
      }
    }

You can do that or, if like me, you prefer to use a reliable library, then Vavr
already defines a `Function3` interface that has the behavior you want. Just 
include the following Maven dependency:

    <dependency>
      <groupId>io.vavr</groupId>
      <artifactId>vavr</artifactId>
      <version>1.0.1</version>
    </dependency>

This library is the good choice if you need to define functions with up to 8 
arguments. Then, you just need to replace, in `ProductType`, the following
definition:

    public final TriFunction<String, String, BigDecimal, Product> factory;

    ProductType (TriFunction<String, String, BigDecimal, Product> factory)
    {
      this.factory = factory;
    }

bvy this one:

    public final Function3<String, String, BigDecimal, Product> factory;

    ProductType (Function3<String, String, BigDecimal, Product> factory)
    {
      this.factory = factory;
    }

## The Visitor

This design pattern belongs to the behavioral category and its purpose is to 
add new operations to an existing object hierarchy without modifying the classes 
of that hierarchy. It is the classic answer to the *expression problem*: when the 
set of types is stable but the set of operations grows, the Visitor lets you keep 
adding operations cheaply.

We reuse the same domain as the factory: a `Product` implemented by `BookProduct`, 
`ElectronicProduct` and `FashionProduct`. To give the visitor a reason to exist, 
each operation now behaves differently per product type:

  - **VAT**: a reduced 5.5% rate for books, the standard 20% rate otherwise.
  - **Shipping**: `10.00 + 2%` of the price for (fragile, insured) electronics, a 
    flat `3.00` for books and a flat `5.00` for fashion.
  - **Discount**: 10% for electronics, 5% for books, 15% for fashion.

### The object-oriented approach

The classic Visitor relies on *double dispatch*. Each `Product` accepts a visitor 
and calls back the overload matching its own type:

    public interface Product
    {
      ...
      <R> R accept(ProductVisitor<R> visitor);
    }

    public record BookProduct (String name, String description, BigDecimal price) implements Product
    {
      ...
      public <R> R accept(ProductVisitor<R> visitor)
      {
        return visitor.visit(this);
      }
    }

The operation lives in a generic visitor, one `visit` overload per concrete type:

    public interface ProductVisitor<R>
    {
      R visit(ElectronicProduct product);
      R visit(BookProduct product);
      R visit(FashionProduct product);
    }

Computing the VAT of any product is then a matter of applying a concrete visitor:

    BigDecimal vat = book.accept(new VatVisitor());

Adding a new operation (shipping, discount, ...) only requires a new 
`ProductVisitor` implementation - the `Product` classes never change. This is the 
reverse of the trade-off the factory made: the factory made adding a new 
*operation* easy but a new *product type* costly (you must edit its central 
`switch`); the visitor makes adding a new *operation* free but shifts that same 
cost onto types - a new product type now forces **every** visitor to be updated. 
It is the classic *expression problem*: you can make types cheap to add or 
operations cheap to add, but not both.

### The functional approach

In modern Java the functional counterpart of the Visitor is **exhaustive pattern 
matching over a sealed type**. We first seal the hierarchy:

    public sealed interface Product permits ElectronicProduct, BookProduct, FashionProduct
    {
      ...
    }

An operation is then just a `Function<Product, R>` built on a `switch` that 
deconstructs each record. Because `Product` is sealed, the compiler proves the 
switch is exhaustive - no `default` branch, no double dispatch, no `accept`:

    public static final Function<Product, BigDecimal> VAT = product -> switch (product)
    {
      case BookProduct(String name, String description, BigDecimal price) -> amount(price, "0.055");
      case ElectronicProduct(String name, String description, BigDecimal price) -> amount(price, "0.20");
      case FashionProduct(String name, String description, BigDecimal price) -> amount(price, "0.20");
    };

Being ordinary functions, these operations compose:

    ProductOperations.DISCOUNT.andThen(amount -> "discount=" + amount).apply(fashion);

Between the classic Visitor and pure pattern matching sits an intermediate step: 
the visitor as a *bundle of functions*, one lambda per type, instead of an 
interface with one method per type:

    public record ProductVisitor<R>(
      Function<ElectronicProduct, R> onElectronic,
      Function<BookProduct, R> onBook,
      Function<FashionProduct, R> onFashion)
    {
      public R visit(Product product)
      {
        return switch (product)
        {
          case ElectronicProduct e -> onElectronic.apply(e);
          case BookProduct b -> onBook.apply(b);
          case FashionProduct f -> onFashion.apply(f);
        };
      }
    }

Which makes an operation a value you can assemble on the fly:

    ProductVisitor<BigDecimal> vat = new ProductVisitor<>(
      e -> ..., b -> ..., f -> ...);
    BigDecimal amount = vat.visit(book);

## Project structure

The code is organized as a multi-module Maven project. The product domain lives
in its own `common` module: a `sealed` `Product` interface, the three product
records and the `ProductType` enumerated - which already carries the FP factory
function seen above. Everything that *can* reuse that domain does:

    oop-fp-design-patterns        (parent POM)
    ├── common                    sealed Product, the records, ProductType(+factory)
    ├── factory   (→ common)      ProductFactory (OOP); the FP factory *is* common.ProductType
    └── visitor   (→ common)      FP: operations over the common records (switch + lambda bundle)
                                  OOP: its own element hierarchy (see below)

The FP factory and the FP visitor both operate directly on the `common` records,
so nothing is duplicated there. The one exception is the **object-oriented
Visitor**: it needs an `accept` method on every element (double dispatch), and
since `common.Product` is `sealed` it cannot be extended from another module.
The OOP visitor therefore owns its element hierarchy and reuses only the
`ProductType` enumerated. This asymmetry is not accidental. The classic Visitor
requires every element to expose an `accept` method, which couples the elements to
the visitor abstraction - they must be defined together with it, and so cannot be
the sealed `common` records. The functional approach has no such coupling: it
pattern-matches over the sealed type from the outside, so the elements know nothing
about the operations applied to them and can stay the shared `common` records.

The full code of these examples, including the associated unit tests, 
can be found here.