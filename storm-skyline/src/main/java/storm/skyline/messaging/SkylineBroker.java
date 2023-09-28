package storm.skyline.messaging;

import java.util.Optional;

import javax.jms.ConnectionFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SkylineBroker {

    private static Optional<ApplicationContext> context = Optional.empty();

    private static ConnectionFactory connectionFactory;

    public static void initialize() {
        if (!context.isPresent()) {
            context = Optional.of(new ClassPathXmlApplicationContext("config/jms-activemq.xml"));
        }
        connectionFactory = (ConnectionFactory) context.get().getBean("jmsConnectionFactory");
    }

    public static ApplicationContext getContext() {
        return context.get();
    }

    public static ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public static void close() {
        ((ClassPathXmlApplicationContext) context.get()).close();
    }

}
