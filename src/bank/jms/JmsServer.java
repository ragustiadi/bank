package bank.jms;

import java.io.IOException;
import java.io.Serializable;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import bank.commands.Command;


public class JmsServer {
	
	private static bank.Bank bank;
	private static JMSProducer updateTopic;
	private static JMSConsumer operationsQueue;
	private static JMSContext context;

	public static void main(String[] args) throws NamingException {
		final Context jndiContext = new InitialContext();
		final ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
		final Queue queue = (Queue) jndiContext.lookup("BANK");
		final Topic topic = (Topic) jndiContext.lookup("BANK.LISTENER");
		
		System.out.println("JMS-Resources bound.");
		
		bank = new bank.common.ServerBank();
		
		System.out.println("Bank initialized.");
		
		context = factory.createContext();
		updateTopic = factory.createContext().createProducer();
		operationsQueue = factory.createContext().createConsumer(queue);
		
		System.out.println("Producer & consumer initialized.\nServer ready.");
		
		ObjectMessage message, response;
		Command cmd;
		String handlerID;
		
		while (true) {
			message = (ObjectMessage) operationsQueue.receive();
			try {
				handlerID = message.getJMSCorrelationID();
				cmd = (Command) message.getObject();
				Serializable o = (Serializable) cmd.execute(bank);
				response = context.createObjectMessage(o);
				response.setJMSCorrelationID(handlerID);
				updateTopic.send(topic, response);
			} catch (JMSException e) {
				System.err.println("Not a valid command received.");
			} catch (IOException e) {
				System.err.println("Command could not be executed.");
			}
		}
		
	}

}
