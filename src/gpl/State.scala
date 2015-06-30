package gpl

case class State[S, A](run: S => (S, A)) {
  //  def apply(s: S): (S, A) = run(s)
  def map[B](f: A => B): State[S, B] = State[S, B] {
    s =>
      val (_s, a) = run(s)
      (_s, f(a))
  }
  def flatMap[B](f: A => State[S, B]): State[S, B] = State[S, B] {
    s =>
      val (_s, a) = run(s)
      f(a).run(_s)
  }
}
