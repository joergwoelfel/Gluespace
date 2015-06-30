package object gpl {
  implicit def stateMonad[S] = new Monad[({ type F[A] = State[S, A] })#F] {
    def unit[A](a: => A) = State[S, A](s => (s, a))
    def flatMap[A, B](ra: State[S, A])(f: A => State[S, B]) = ra flatMap f
  }

  // return either a `Suspend`, a `Return`, or a right-associated `FlatMap`
  @annotation.tailrec
  def step[F[_], A](a: Free[F, A]): Free[F, A] = a match {
    case FlatMap(FlatMap(x, f), g) => step(x flatMap (a => f(a) flatMap g))
    case FlatMap(Return(x), f)     => step(f(x))
    case _                         => a
  }

  type ~>[F[_], G[_]] = Translate[F, G] // gives us infix syntax `F ~> G` for `Translate[F,G]`

  def instanceToState[S] = new (({ type F[A] = Instance[S, A] })#F ~> ({ type F[A] = State[S, A] })#F) {
    def apply[A](a: Instance[S, A]) = a.toState
  }

  type Component[S, A] = Suspend[({ type F[A] = Instance[S, A] })#F, A]

  type UnitComponent[S] = Component[S, Unit]

}