package folio3.ghanghor;

/**
 * Created by sarimj on 2/19/2016.
 */

import com.google.gson.Gson;

public class Message {
    private int id;
    private String fileId;
    private Operation operation;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }


    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
