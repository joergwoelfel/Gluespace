package gpl

trait Monad[F[_]] extends Functor[F] {
  def unit[A](a: => A): F[A]

  def flatMap[A, B](a: F[A])(f: A => F[B]): F[B]
  def map[A, B](a: F[A])(f: A => B): F[B] = flatMap(a)(a => unit(f(a)))
}

