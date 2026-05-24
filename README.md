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

The full code of these examples, including the associated unit tests, 
can be found here.