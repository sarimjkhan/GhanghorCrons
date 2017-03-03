package folio3.ghanghor;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by sarimj on 2/22/2016.
 */
public class Worker {
    Connection connection;
    Channel channel;

    public void start(String host,String DOWNLOADER_QUEUE,String SCANNER_QUEUE, String ENFORCER_QUEUE) throws IOException,InterruptedException,TimeoutException{
        MessageReceiver messageReceiver = new MessageReceiver();
        messageReceiver.mainExecute();

        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost(host);

        connection = connectionFactory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(DOWNLOADER_QUEUE, false, false, false, null);
        channel.queueDeclare(SCANNER_QUEUE, false, false, false, null);
        channel.queueDeclare(ENFORCER_QUEUE, false, false, false, null);

        Message[] messages = new Message[6];
        for(int i=0 ;i <6 ; i++){
            messages[i] = new Message();
            messages[i].setId(i);
            messages[i].setOperation(Operation.DELETE);
        }
        messages[0].setFileId("54248641669");
        messages[1].setFileId("54247976277");
        messages[2].setFileId("54247979909");
        messages[3].setFileId("54247983357");
        /*messages[3].setFileId("54243390625");
        messages[4].setFileId("54243392845");
        messages[5].setFileId("54243395225");
        messages[1].setFileId("9839384678");
        messages[2].setFileId("54233879885");*/

        System.out.println("Gson String: " + messages[0].toJSON());

        //channel.basicPublish("", DOWNLOADER_QUEUE, null, messages[0].toJSON().getBytes());
        //channel.basicPublish("", DOWNLOADER_QUEUE, null, messages[3].toJSON().getBytes());
        //channel.basicPublish("", DOWNLOADER_QUEUE, null, messages[4].toJSON().getBytes());
        //channel.basicPublish("", DOWNLOADER_QUEUE, null, messages[5].toJSON().getBytes());
        //channel.basicPublish("", SCANNER_QUEUE, null, messages[1].toJSON().getBytes());
        //channel.basicPublish("", ENFORCER_QUEUE, null, messages[2].toJSON().getBytes());

    }

    public void close() throws IOException,TimeoutException{
        channel.close();
        connection.close();
    }
}
