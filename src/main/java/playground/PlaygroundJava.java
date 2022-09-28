package playground;

public class PlaygroundJava {

  public static void main(String[] args) {
    Singleton theSingleton = Singleton.getInstance();
    Singleton theSingleton_2 = Singleton.getInstance();
    System.out.println(theSingleton == theSingleton_2);
  }
}

class Singleton {
  private static Singleton instance = null; // one per class

  private Singleton() {
  }

  public static Singleton getInstance() {
    if (instance == null) {
      instance = new Singleton(); // calling the constructor once
    }
    return instance;
  }
}