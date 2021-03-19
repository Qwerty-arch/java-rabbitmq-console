package com.oshovskii.rabbitmq.console.consumer;

import com.rabbitmq.client.*;

import java.util.Scanner;

public class ExchangeReceiverApp {
    private static final String EXCHANGE_NAME = "directExchanger";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = channel.queueDeclare().getQueue();
        System.out.println("My queue name: " + queueName);

        // set_topic php
        Scanner in = new Scanner(System.in);
        System.out.print("Input the title of article: ");
        String input = in.nextLine();
        String[] arr = input.split(" ",2);
        String firstWord = arr[0];
        String theRest = arr[1];

        channel.queueBind(queueName, EXCHANGE_NAME, theRest);

        System.out.println(" [*] Waiting for messages");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
//            System.out.println(Thread.currentThread().getName());
            System.out.println(" [x] Received '" + message + "'");
        };

//        System.out.println(Thread.getAllStackTraces().keySet());
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
//        Thread.sleep(3000);
//        System.out.println(Thread.getAllStackTraces().keySet());

        // channel.queueBind(queueName, EXCHANGE_NAME, "c++");
    }
}

