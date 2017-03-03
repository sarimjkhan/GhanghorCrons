/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package folio3.ghanghor;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.box.sdk.ProgressListener;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mfahad
 */
public class BoxClient {

    /**
     * @param args the command line arguments
     */

    private BoxAPIConnection connection;

    /*
    public static void main(String[] args) {
        // TODO code application logic here
        
        BoxClient client = new BoxClient();
        client.createConnection();
        //client.downloadFile("9839384678");
        client.deleteFile("9839384678");
        
        
    }*/

    public BoxClient(){
        createConnection();
    }

    private void createConnection()
    {
        this.connection = new BoxAPIConnection("HoeAld0t5UqWvLeUKOM20uB9old8Ti7V");
    }

    public void listContents()
    {
        BoxFolder rootFolder = BoxFolder.getRootFolder(this.connection);
        for (BoxItem.Info itemInfo : rootFolder) {
            System.out.format("[%s] %s\n", itemInfo.getID(), itemInfo.getName());
        }
    }
    public void deleteFile(String fileId)
    {
        BoxFile file = new BoxFile(this.connection, fileId);
        file.delete();
    }

    public String downloadFile(String fileId)
    {
        String fileName = "";
        FileOutputStream stream = null;
        try {
            BoxFile file = new BoxFile(this.connection, fileId);
            BoxFile.Info info = file.getInfo();

            stream = new FileOutputStream("BoxTemp/" + info.getName());
            fileName = info.getName();
            //file.download(stream);
            file.download(stream, new ProgressListener() {
                @Override
                public void onProgressChanged(long l, long l1) {
                    if (l == l1) {
                        System.out.println("File Downloaded Successfully");
                        try {
                            FileInputStream inputStream = new FileInputStream("BoxTemp/" + file.getInfo().getName());
                            FileChannel inputChannel = inputStream.getChannel();
                            FileOutputStream outputStream = new FileOutputStream("Box/" + file.getInfo().getName());
                            FileChannel outputChannel = outputStream.getChannel();
                            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                        } catch (FileNotFoundException e) {
                        } catch (IOException e) {
                        }

                        //System.out.println("l: " + l + ",l1: " + l1);
                    }
                }
            });
            stream.close();
            return fileName;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BoxClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(BoxClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
        }
        return fileName;
    }

}
