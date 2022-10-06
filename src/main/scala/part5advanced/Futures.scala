package part5advanced

import practice.LList

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Random, Success, Try}

object Futures {

  def longComputation(): Unit = {
    LList.make(1, 2000000) // roughly 1s on my machine
  }

  // Future = a computation that runs on another thread
  def computeMeaningOfLife(): Int = {
    longComputation()
    42
  }

  def demoSequentialExecution(): Unit = {
    println(computeMeaningOfLife())
    println(computeMeaningOfLife())
    println(computeMeaningOfLife())
    println(computeMeaningOfLife())
    // prints 42 once per second
  }

  // necessary boilerplate for multithreaded computations
  val executor = Executors.newFixedThreadPool(1) // 16 JVM threads so that we can run some computations in parallel
  implicit val executionContext: ExecutionContext = ExecutionContext.fromExecutorService(executor)

  def demoParallelExecution(): Unit = {
    val future: Future[Int] = Future(computeMeaningOfLife())(executionContext /* <-- the execution context */)
    //                                      ^^^^^^^^^^^^^^^^^^^^^^ the code being run
    val future2: Future[Int] = Future(computeMeaningOfLife())(executionContext /* <-- the execution context */)
    val future3: Future[Int] = Future(computeMeaningOfLife())(executionContext /* <-- the execution context */)
    val future4: Future[Int] = Future(computeMeaningOfLife())(executionContext /* <-- the execution context */)

    val sumFuture = for {
      a <- future
      b <- future2
      c <- future3
      d <- future4
    } yield a + b + c + d // ONE SECOND!
  }

  // Future[Int] = a computation that runs on SOME OTHER THREAD, which completes with an Int
  val aFuture: Future[Int] = Future(computeMeaningOfLife()) // something that will be run on another thread
  // map, flatMap
  val aMappedFuture = aFuture.map(_ * 10) // ANOTHER FUTURE storing 420, when aFuture completes
  //                             ^^^^^^^^ also runs in parallel
  val aFlatMappedFuture = aFuture.flatMap(x => Future(x * computeMeaningOfLife())) // 42 * 42, after 2s
  //                      ^^^^^^^ ~1s          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ then this thing will run, taking ~1s as well
  val aFilteredFuture: Future[Int] = aFuture.filter(_ > 100)
  // WHEN the Future completes, if the value passes the predicate, that Future will be returned; otherwise this will be a failed Future with a NoSuchElementException
  /*
    flatMap returns another Future:
    - when the first future finishes, the function will run
    - that function will return another future
    - that future will be the result
   */

  // map + flatMap => for comprehension
  val chainedFutures = for {
    x <- aFuture
    product <- Future(x * computeMeaningOfLife()) // this Future depends on the previous one finishing
    px10 <- Future(10 * product * computeMeaningOfLife())
  } yield px10 // a Future taking 3s to compute, storing 42 * 42 * 42 * 10
  // deconstructed: aFuture.flatMap(x => Future(x * computeMeaningOfLife()).flatMap(product => Future(10 * product * computeMeaningOfLife()).map(px10 => px10))

  // checking for completion
  aFuture.onComplete { // onComplete + partial functions = <3
    case Success(value) => println(s"parallel value computed: $value")
    case Failure(ex) => println(s"parallel computation failed: $ex")
  } // this whole expression returns Unit
  // ^ partial function (callback) runs on SOME OTHER THREAD

  // obtain the current state of the future
  val getTheValue: Option[Try[Int]] = aFuture.value // inspects the state of the Future RIGHT NOW
  // Option because the Future might not have finished AT THE MOMENT you call .value
  // Try because if the Future has finished, it might have failed

  // fallback methods: recover, recoverWith, fallbackTo
  val aFailedFuture: Future[String] = Future(throw new RuntimeException("FAILING because I can"))
  val recoveredFuture = aFailedFuture.recover {
    case e: RuntimeException => s"Computation failed: $e"
  } // runs this function on the exception that the Future threw on its own thread

  val recoveredFuture_v2 = aFailedFuture.recoverWith {
    case e: RuntimeException => Future(s"Computation failed: $e") // in this case we return ANOTHER Future
  }

  // fallbackTo: an equivalent of `orElse` for Try/Option
  val recoveredFuture_v3 = aFailedFuture.fallbackTo(Future("Alternative solution"))
  /*
    Futures & lambdas passed to map, flatMap, onComplete, ...
    - run on SOME thread
    - you can't know which
    - you don't know WHEN the computation is scheduled for execution
   */

  /*
    TODO: implement a retry function:
    1. invoke the action
    2. if the Future failed, go back to step 1.
    3. if the Future is successful, test if the predicate is satisfied by the value of that Future.
      3.1. if the predicate is satisfied, return that Future.
      3.2. otherwise, go back to step 1.

    Hint: use a version of recover and filter.
   */
  def retryUntil[A](action: () => Future[A], predicate: A => Boolean): Future[A] =
    action().filter(predicate).recoverWith {
      case _ => retryUntil(action, predicate)
    }

  // TODO: a version of retryUntil up to a max number of retries
  // if you've exceeded the number of retries, return a failed Future with a RuntimeException("max retries exceeded")
  def retryN[A](action: () => Future[A], predicate: A => Boolean, maxRetries: Int): Future[A] =
    action().filter(predicate).recoverWith {
      case _ =>
        if (maxRetries > 0) retryN(action, predicate, maxRetries - 1)
        else Future(throw new RuntimeException("max retries exceeded"))
    }

  def retryN_v2[A](action: () => Future[A], predicate: A => Boolean, maxRetries: Int): Future[A] = {
    def retry(counter: Int = 0): Future[A] =
      if (counter >= maxRetries) Future(throw new RuntimeException("max retries exceeded"))
      else action().filter(predicate).recoverWith {
        case _ => retry(counter + 1)
      }

    retry()
  }

  def testRetryUntil(): Unit = {
    val action = () => Future {
      Thread.sleep(1000) // take some time
      val value = Random.nextInt(100) // randomly generated number 0-100
      println(s"Generated $value")
      value
    }

    val predicate = (x: Int) => x < 10

    /*
      "Generated 75"
      action will run again
      "Generated 68"
      action will run again
      "Generated 43"
      action will run again
      "Generated 9"
      9
     */
    val finalFuture: Future[Int] = retryUntil(action, predicate)
    finalFuture.onComplete(result => println(result))
  }

  def main(args: Array[String]): Unit = {
    testRetryUntil()
  }
}
