package gpl

sealed trait Free[F[_], A] {
  def flatMap[B](f: A => Free[F, B]): Free[F, B] =
    FlatMap(this, f)

  def map[B](f: A => B): Free[F, B] =
    flatMap(f andThen (Return(_)))

  def runFree[G[_]](t: F ~> G)(implicit G: Monad[G]): G[A]
  def step[G[_]](implicit G: Monad[G]): Free[F, A]
}

trait Suspend[F[_], A] extends Free[F, A] {
  val self: F[A]
  def runFree[G[_]](t: F ~> G)(implicit G: Monad[G]): G[A] = t(self)
  def step[G[_]](implicit G: Monad[G]): Free[F, A] = this
}

case class Return[F[_], A](a: A) extends Free[F, A] {
  def runFree[G[_]](t: F ~> G)(implicit G: Monad[G]): G[A] = G.unit(a)
  def step[G[_]](implicit G: Monad[G]): Free[F, A] = this
}

case class FlatMap[F[_], A, B](val freeA: Free[F, A],
                               f: A => Free[F, B]) extends Free[F, B] {
  def runFree[G[_]](t: F ~> G)(implicit G: Monad[G]): G[B] = {
    step match {
      case FlatMap(freeA, f) => {
        val s = freeA.asInstanceOf[Suspend[F, A]]
        G.flatMap(t(s.self))((a: A) => f(a).runFree(t))
      }
    }
  }
  def step[G[_]](implicit G: Monad[G]): Free[F, B] = _step(this)

  @annotation.tailrec
  private def _step[F[_], A](a: Free[F, A]): Free[F, A] = a match {
    case FlatMap(FlatMap(x, f), g) => _step(x flatMap (a => f(a) flatMap g))
    case FlatMap(Return(x), f)     => _step(f(x))
    case _                         => a
  }
}
