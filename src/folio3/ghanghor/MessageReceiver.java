package folio3.ghanghor;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;
import com.google.gson.Gson;

/**
 * Created by sarimj on 2/19/2016.
 */
public class MessageReceiver {
    private final static String DOWNLOADER_QUEUE = "ghanghor_downloader_queue";
    private final static String SCANNER_QUEUE = "ghanghor_scanner_queue";
    private final static String ENFORCER_QUEUE = "ghanghor_enforcer_queue";

    private static Message messageObj = null;

    public void mainExecute() throws IOException,InterruptedException,TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //Channel channel = connection.createChannel();
        //Channel channel = connection.createChannel();1

        handleQueue(DOWNLOADER_QUEUE, channel);
        handleQueue(SCANNER_QUEUE,channel);
        handleQueue(ENFORCER_QUEUE,channel);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }

    private static void handleQueue(String queue, Channel channel) throws IOException{
        channel.queueDeclare(queue, false, false, false, null);
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws UnsupportedEncodingException , IOException{
                String message = new String(body, "UTF-8");
                Gson gson = new Gson();
                messageObj = gson.fromJson(message,Message.class);
                System.out.println("Got hello msg");
                if(queue == DOWNLOADER_QUEUE){
                    System.out.println("Performing downloader queue task: ");
                    BoxClient boxClient = new BoxClient();
                    String downloadedFileName = boxClient.downloadFile(messageObj.getFileId());
                    if(downloadedFileName != "") {
                        messageObj.setFileName(downloadedFileName);
                        channel.basicPublish("",SCANNER_QUEUE,null,messageObj.toJSON().getBytes());
                    }
                }

                if(queue == SCANNER_QUEUE){
                    System.out.println("Performing scanner queue task: ");
                    try {
                        Thread.sleep(3000);
                    } catch(InterruptedException e){
                    } finally {
                        System.out.println("File scanned successfully: ");
                        channel.basicPublish("", ENFORCER_QUEUE, null, messageObj.toJSON().getBytes());
                    }
                }

                if(queue == ENFORCER_QUEUE){
                    System.out.println("Performing enforcer queue task: ");
                    BoxClient boxClient = new BoxClient();
                    if(!messageObj.getFileName().contains("secure")){
                        boxClient.deleteFile(messageObj.getFileId());
                        System.out.println("File Deleted Successfully..");
                    }
                }
                //System.out.println("Queue Name: "+ queue + ", Received :'" + message + "'");
            }
        };
        channel.basicConsume(queue, true, consumer);
    }
}
