package application

import gpl._

object Main extends App {

  val printlnComponent = Function1Component[Unit, String, Unit]((s: Unit, v1: String) => ((), println(v1)))

  implicit def toState[S, A](instance: Instance[S, A]) = instance.toState
  val program = for {
    f <- printlnComponent
    _ <- printlnComponent(StringComponent[Unit]("Hello World IO"))
  } yield f("Hello World")

  val p = program.runFree[({ type F[A] = State[Unit, A] })#F](instanceToState)
  p.run(())
}

case class ReadComponent[S, R](f: S => R)
  extends Component[S, R] {

  val self = new Instance[S, R] {
    def toState = State((s: S) => (s, f(s)))
  }

  def apply(): Component[S, R] = new Component[S, R] {
    val self = new Instance[S, R] {
      def toState: State[S, R] = State[S, R] {
        s =>
          {
            (s, f(s))
          }
      }
    }
  }
}

case class WriteComponent[S, T](f: (S, T) => S)
  extends Function1[Component[S, T], Component[S, Unit]]
  with Component[S, Function1[T, S]] {

  val self = new Instance[S, T => S] {
    def toState = State((s: S) => (s, (v1: T) => f(s, v1)))
  }

  def apply(v1: Component[S, T]): Component[S, Unit] = new Component[S, Unit] {
    val self = new Instance[S, Unit] {
      def toState: State[S, Unit] = State[S, Unit] {
        s =>
          {
            val (_s, _v1): (S, T) = v1.self.toState.run(s)
            (f(_s, _v1), ())
          }
      }
    }
  }
}

