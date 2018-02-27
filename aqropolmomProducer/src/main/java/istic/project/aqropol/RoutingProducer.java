package istic.project.aqropol;

import com.rabbitmq.client.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class RoutingProducer {

    private final static Logger logger = Logger.getLogger(RoutingProducer.class);

    private final String exchanger, route, encoding, queue;
    private final boolean durable, exclusive, autodelete;
    private final Channel channel;

    private RoutingProducer(Channel channel, String exchanger, String route, String encoding, String queue, boolean durable, boolean exclusive, boolean autodelete) {
        this.exchanger = exchanger;
        this.route = route;
        this.encoding = encoding;
        this.channel = channel;
        this.queue = queue;
        this.durable = durable;
        this.exclusive = exclusive;
        this.autodelete = autodelete;

        try {
            this.channel.exchangeDeclare(this.exchanger, BuiltinExchangeType.DIRECT, this.durable);

        } catch (IOException e) {
            logger.error("Can't declare exchange \"" + this.exchanger + "\" : " + e.getMessage(), e);
        }

        try {
            this.channel.queueDeclare(this.queue, this.durable, this.exclusive, this.autodelete, null);
        } catch (IOException e) {
            logger.error("Can't declare queue \"" + this.queue + "\" : " + e.getMessage(), e);
        }

        try {
            this.channel.queueBind(this.queue, this.exchanger, this.route);
        } catch (IOException e) {
            logger.error("Can't bind queue \"" + this.queue + "\" : " + e.getMessage(), e);
        }


        if(logger.isDebugEnabled()) {
            this.channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> logger.debug("handleReturn(int replyCode = " + replyCode + ", String replyText = " + replyText + ", String exchange = " + exchange + ", String routingKey = " + routingKey + ", AMQP.BasicProperties properties = " + properties.toString() + ", byte[] body = " + new String(body) +";"));
        }

        this.channel.addShutdownListener(cause -> logger.warn("RabbitMQ Server has shutdown : " + cause));
    }

    public void send(String message) throws IOException {
        this.channel.basicPublish(this.exchanger, this.route, true, null, message.getBytes(this.encoding));
    }

    public void addReturnListener(ReturnListener listener) {
        this.channel.addReturnListener(listener);
    }

    public void addConfirmListener(ConfirmListener listener) {
        this.channel.addConfirmListener(listener);
    }

    public void addShutdownListener(ShutdownListener listener) {
        this.channel.addShutdownListener(listener);
    }

    public Channel getChannel() {
        return this.channel;
    }

    public String getRoute() {
        return this.route;
    }

    public String getExchangeName() {
        return this.exchanger;
    }

    public static class RoutingProducerFactory {

        private Properties config;
        private String exchanger, route, encoding, queue;
        private boolean durable, exclusive, autodelete;

        public RoutingProducerFactory() {

            InputStream isConfig = RoutingProducer.class.getResourceAsStream("/config.properties");
            this.config = new Properties();

            try {
                config.load(isConfig);
                isConfig.close();
            } catch (IOException e) {
                logger.error("Can't load config " + e.getMessage(), e);
            }

            this.exchanger = config.getProperty("aqropol.amqp.channel.exchanger", "");
            this.route = config.getProperty("aqropol.amqp.channel.route", "");
            this.encoding = config.getProperty("project.build.sourceEncoding", "UT-8");

            this.queue = config.getProperty("aqropol.amqp.queue", "");

            this.durable = Boolean.valueOf(config.getProperty("aqropol.ampq.queue.durable"));
            this.exclusive = Boolean.valueOf(config.getProperty("aqropol.amqp.queue.exclusive"));
            this.autodelete = Boolean.valueOf(config.getProperty("aqropol.amqp.queue.autodelete"));
        }

        public RoutingProducer build() throws IOException, TimeoutException {
            return new RoutingProducer(ProducerClient.getInstance().newChannel(), exchanger, route, encoding, queue, durable, exclusive, autodelete);
        }

        public void setExchanger(String exchanger) {
            this.exchanger = exchanger;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }
    }
}
