package gpl

case class StringComponent[S](value: String) extends Component[S, String] {
  val self = new Instance[S, String] {
    def toState = State(s => (s, value))
  }
}

