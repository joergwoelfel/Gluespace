# Gluespace

This repository is based upon Chapter 13 "External effects and I/O" of "Functional Programming in Scala" by Paul Chiusano and Rúnar Bjarnason.

While that chapter illustrates crafting an embedded domain-specific language (EDSL), here a very first step is taken towards an embedded general-purpose language (EPSL).

The fundament of a programming language is a function. So we first construct a function, which is lifted to a `Component` - the Result as well as the parameters. So we yield functional composabilty on a lifted level.

`Function1[T, R]` -> `Function1[Component[S, T], Component[S, R]]` see FunctionComponent.scala

`Component[S, T]` corresponds in the book to Console[T]. We have enriched our `Component` by a type parameter `S`, which allows us to abtract the `ConsoleState[A]` of the book to an abitrary `State[S, A]` Monad.

Based upon that fundamental shift, it is planned to develop a complete object orientd general purpose language, which abstract from its persistence implementation. Something like an object relational mapper (ORM) but avoiding all the drawbacks and surpassing its capabilities.

# Start
Just start in your IDE the main routine contained in application/Main.scala.



