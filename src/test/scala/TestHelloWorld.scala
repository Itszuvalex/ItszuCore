import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by Christopher Harris (Itszuvalex) on 4/11/15.
 */
class TestHelloWorld extends FlatSpec with Matchers {

  "A test" should "run" in {
    System.out.println("Hello World")
  }

}
