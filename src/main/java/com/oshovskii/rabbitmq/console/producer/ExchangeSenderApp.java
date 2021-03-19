package com.oshovskii.rabbitmq.console.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;


public class ExchangeSenderApp {
    private static final String EXCHANGE_NAME = "directExchanger";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            // php some message
            Scanner in = new Scanner(System.in);
            System.out.print("Input the title of article and info: ");
            String message = in.nextLine();
            String[] arr = message.split(" ",2);
            String firstWord = arr[0];
            String theRest = arr[1];

            channel.basicPublish(EXCHANGE_NAME, firstWord, null, theRest.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}