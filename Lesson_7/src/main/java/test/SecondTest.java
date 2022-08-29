package test;

import interfaces.BeforeSuite;
import interfaces.Test;

public class SecondTest {
    @BeforeSuite
    void beforeTestFirst(){
        System.out.println("interfaces.BeforeSuite First");
    }

    @BeforeSuite
    void beforeTestSecond(){
        System.out.println("interfaces.BeforeSuite Second");
    }

    @Test
    void priorityDefaultFirst(){
        System.out.println("priorityDefault First");
    }

    //    @interfaces.Test
    void priorityDefaultSecond(){
        System.out.println("priorityDefault Second");
    }

    @Test (priority = 10)
    void priority10(){
        System.out.println("priority 10");
    }

    @Test(priority = 1)
    void priority1(){
        System.out.println("priority 1");
    }
}
