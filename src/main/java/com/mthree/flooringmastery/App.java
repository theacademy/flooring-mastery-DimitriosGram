package com.mthree.flooringmastery;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.mthree.flooringmastery.controller.FlooringController;

public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println("Beans loaded: " + ctx.getBeanDefinitionCount());
        FlooringController controller = ctx.getBean("controller", FlooringController.class);
        controller.run();
    }
}
