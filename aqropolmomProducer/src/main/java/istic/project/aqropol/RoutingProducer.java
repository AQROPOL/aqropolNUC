package istic.project.aqropol;

import com.rabbitmq.client.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class RoutingProducer {

    private final static Logger logger = Logger.getLogger(RoutingProducer.class);

    private final String exchanger, route, encoding;
    private final Channel channel;

    private RoutingProducer(Channel channel, String exchanger, String route, String encoding) {
        this.exchanger = exchanger;
        this.route = route;
        this.encoding = encoding;
        this.channel = channel;

        try {
            this.channel.exchangeDeclare(this.exchanger, BuiltinExchangeType.DIRECT, true);
        } catch (IOException e) {
            logger.error("Can't declare exchange " + e.getMessage(), e);
        }

        if(logger.isDebugEnabled()) {
            this.channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> logger.debug("handleReturn(int replyCode = " + replyCode + ", String replyText = " + replyText + ", String exchange = " + exchange + ", String routingKey = " + routingKey + ", AMQP.BasicProperties properties = " + properties.toString() + ", byte[] body = " + new String(body) +";"));
        }

        this.channel.addShutdownListener(cause -> logger.warn("RabbitMQ Server has shutdown : " + cause));
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

    public class RoutingProducerFactory {

        private Properties config = new Properties();
        private String exchanger;
        private String route;
        private String encoding;

        public RoutingProducerFactory() {

            InputStream isConfig = RoutingProducer.class.getResourceAsStream("/config.properties");
            this.config = new Properties();

            try {
                config.load(isConfig);
                isConfig.close();
            } catch (IOException e) {
                logger.error("Can't load config " + e.getMessage(), e);
            }

            this.exchanger = config.getProperty("aqropol.amqp.exchange", "");
            this.route = config.getProperty("aqropol.amqp.route", "");
            this.encoding = config.getProperty("project.build.sourceEncoding", "UT-8");
        }

        public RoutingProducer build() throws IOException, TimeoutException {
            return new RoutingProducer(ProducerClient.getInstance().newChannel(), exchanger, route, encoding);
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
