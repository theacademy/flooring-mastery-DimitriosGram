package com.mthree.flooringmastery;

import com.mthree.flooringmastery.controller.FlooringController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        FlooringController controller = ctx.getBean("controller", FlooringController.class);
        System.out.println("Beans loaded: " + ctx.getBeanDefinitionCount());

        controller.run();
    }
}
