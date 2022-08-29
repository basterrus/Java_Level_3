import test.FirstTest;
import test.SecondTest;
import test.TestingClass;

public class MainTestStart {
    public static void main(String[] args) {
        TestingClass.start(FirstTest.class);
        TestingClass.start(SecondTest.class);
    }
}
