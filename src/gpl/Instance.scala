package gpl

trait Instance[S, A] {
  def toState: State[S, A]
}

