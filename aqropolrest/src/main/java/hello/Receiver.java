package hello;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @author VinYarD
 * created : 14/03/2018, 05:53
 */

@Component
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}