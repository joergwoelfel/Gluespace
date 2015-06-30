package gpl

  case class Function1Component[S, T, R](f: (S, T) => (S, R))
    extends Function1[Component[S, T], Component[S, R]]
    with Component[S, Function1[T, (S, R)]] {

    val self = new Instance[S, T => (S, R)] {
      def toState = State((s: S) => (s, (v1: T) => f(s, v1)))
    }

    def apply(v1: Component[S, T]): Component[S, R] = new Component[S, R] {
      val self = new Instance[S, R] {
        def toState: State[S, R] = State[S, R] {
          s =>
            {
              val (_s, _v1): (S, T) = v1.self.toState.run(s)
              f(_s, _v1)
            }
        }
      }
    }
  }

