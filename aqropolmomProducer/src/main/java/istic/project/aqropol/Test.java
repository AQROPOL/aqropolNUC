package istic.project.aqropol;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Test {

    public Test() {

            try {
                TopicProducer.TopicProducerFactory factory = new TopicProducer.TopicProducerFactory();
                TopicProducer prod = factory.build();

                boolean loop = true;
                while(loop) {
                    prod.send("topic", "message de test.");
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
