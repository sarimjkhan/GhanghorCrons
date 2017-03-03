package folio3.ghanghor;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    private final static String host = "localhost";

    private final static String DOWNLOADER_QUEUE = "ghanghor_downloader_queue";
    private final static String SCANNER_QUEUE = "ghanghor_scanner_queue";
    private final static String ENFORCER_QUEUE = "ghanghor_enforcer_queue";

    private static int noOfDownloaderMessages = 1;
    private static int noOfScannerMessages = 1;
    private static int noOfEnforcerMessages = 3;

    public static void main(String[] args) throws IOException,InterruptedException,TimeoutException{
        Worker worker = new Worker();
        worker.start(host, DOWNLOADER_QUEUE, SCANNER_QUEUE, ENFORCER_QUEUE);

        /*
        Downloader downloader = new Downloader();
        try {
            downloader.mainExecute();
        } catch(Exception e) {}

        Scanner scanner = new Scanner();
        try {
            scanner.mainExecute();
        } catch(Exception e) {}

        Enforcer enforcer = new Enforcer();
        try {
            enforcer.mainExecute();
        } catch(Exception e) {}
        */
    }

}
