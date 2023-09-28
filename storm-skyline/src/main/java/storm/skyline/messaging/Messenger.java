package storm.skyline.messaging;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.storm.jms.JmsProvider;

import com.skyline.model.SkyTuple;

import storm.skyline.config.QueueConfig;
import storm.skyline.producer.JmsSkyTupleProducer;

public class Messenger implements Serializable {

    private static final long serialVersionUID = -3113004358443562944L;

    private Connection connection;

    private Session session;

    private MessageConsumer messageConsumer;

    private MessageProducer messageProducer;

    private JmsProvider jmsProvider;

    private JmsSkyTupleProducer jmsSkyTupleProducer;

    private boolean autoAck;

    private boolean jmsTransactional;

    private int jmsAcknowledgeMode;

    private QueueConfig queueConfig;

    public Messenger(QueueConfig queueConfig) {
        setQueueConfig(queueConfig);
    }

    public void initialize() throws Exception {
        ConnectionFactory connectionFactory = jmsProvider.connectionFactory();
        setConnection(connectionFactory.createConnection(queueConfig.getUsername(), queueConfig.getPassword()));
        setSession(connection.createSession(jmsTransactional, jmsAcknowledgeMode));
        setMessageProducer(session.createProducer(jmsProvider.destination()));
        setMessageConsumer(session.createConsumer(jmsProvider.destination()));
        connection.start();
    }

    public void send(SkyTuple tuple) throws JMSException {
        messageProducer.send(session.createObjectMessage(tuple));
    }

    public void listen(MessageListener listener) throws JMSException {
        messageConsumer.setMessageListener(listener);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public MessageConsumer getMessageConsumer() {
        return messageConsumer;
    }

    public void setMessageConsumer(MessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    public MessageProducer getMessageProducer() {
        return messageProducer;
    }

    public void setMessageProducer(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    public JmsProvider getJmsProvider() {
        return jmsProvider;
    }

    public void setJmsProvider(JmsProvider jmsProvider) {
        this.jmsProvider = jmsProvider;
    }

    public JmsSkyTupleProducer getJmsSkyTupleProducer() {
        return jmsSkyTupleProducer;
    }

    public void setJmsSkyTupleProducer(JmsSkyTupleProducer jmsSkyTupleproducer) {
        this.jmsSkyTupleProducer = jmsSkyTupleproducer;
    }

    public boolean isAutoAck() {
        return autoAck;
    }

    public void setAutoAck(boolean autoAck) {
        this.autoAck = autoAck;
    }

    public boolean isJmsTransactional() {
        return jmsTransactional;
    }

    public void setJmsTransactional(boolean jmsTransactional) {
        this.jmsTransactional = jmsTransactional;
    }

    public int getJmsAcknowledgeMode() {
        return jmsAcknowledgeMode;
    }

    public void setJmsAcknowledgeMode(int jmsAcknowledgeMode) {
        this.jmsAcknowledgeMode = jmsAcknowledgeMode;
    }

    public QueueConfig getQueueConfig() {
        return queueConfig;
    }

    public void setQueueConfig(QueueConfig config) {
        this.queueConfig = config;
    }

}