package part2oop

import java.io.FileNotFoundException

object Exceptions {

  val aNumber = 43
  // exception = an application crash

  // 1 - throw exceptions
  val aWeirdValue: Int = throw new ArithmeticException
  //                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ expression which returns Nothing

  /*
    Hierarchy of exceptions
    Throwable (trait == "interface")
      Exception - "crashes" that you can recover from
        ... a bunch of other exceptions
        RuntimeException
          ArithmeticException
      Error - you cannot recover from
        StackOverflowError
        OutOfMemoryError (OOM)
   */

  // 2 - define our own exception types besides what's there
  class MyException(reason: String) extends RuntimeException {
    def explainYourself(): Unit = println(reason)
  }

  /*
    try:
      code
    except SomeError:
      "catch code"
    except SomeOtherError:
      "other catch code"
   */
  // 3 - catch exceptions
  val potentialFail = try {
    // code that might fail
    aNumber / 0
  } catch { // exceptions get tested if the try block throws an exception
    case npe: NullPointerException => -1 // <- another expression if the exception was caught
    case fnf: FileNotFoundException => -2
    case ae: ArithmeticException => 0
    case e: RuntimeException => -4
    case _ => -999 // matches everything
  // general structure:
  // case name: ExceptionType =>
  // can add multiple cases in the catch clause
  // first matching case will dictate the returned value
  // if no case matches, program crashes
  } finally {
    // executed no matter what
    // useful for "cleanup"
  }

  def main(args: Array[String]): Unit = {

  }
}
