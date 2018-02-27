package istic.project.aqropol;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Test {

    public Test() {

            try {
                RoutingProducer.RoutingProducerFactory factory = new RoutingProducer.RoutingProducerFactory();
                RoutingProducer prod = factory.build();

                boolean loop = true;
                while(loop) {
                    prod.send("message de test.");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
    }

    public static void main(String[] args) {
        new Test();
    }
}
