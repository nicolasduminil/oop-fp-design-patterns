package fr.simplex_software.workshop.oop_design_patterns.visitor;

public interface ProductVisitor<R>
{
  R visit(ElectronicProduct product);
  R visit(BookProduct product);
  R visit(FashionProduct product);
}
