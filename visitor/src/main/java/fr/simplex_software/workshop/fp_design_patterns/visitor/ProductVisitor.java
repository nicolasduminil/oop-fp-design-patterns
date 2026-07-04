package fr.simplex_software.workshop.fp_design_patterns.visitor;

import fr.simplex_software.workshop.common.*;

import java.util.function.*;

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
