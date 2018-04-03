package istic.project.aqropol.mom_consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;

/**
 * @author VinYarD
 * created : 15/03/2018, 00:47
 */

public class Receiver {

    @RabbitListener(queues = "#{autoDeleteQueue.name}")
    public void receive(byte[] in) throws InterruptedException {
        receive(new String(in), 1);
    }

    public void receive(String in, int receiver) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println("instance " + receiver + " [x] Received '" + in + "'");
        //doWork(in);
        watch.stop();
        System.out.println("instance " + receiver + " [x] Done in " +
                watch.getTotalTimeSeconds() + "s");
    }

    private void doWork(String in) throws InterruptedException {
        for (char ch : in.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }

}