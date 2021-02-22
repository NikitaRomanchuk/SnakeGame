package ru.itis.client;

public class Message {
    private byte type;

    private byte[] data;

    public Message(byte type, byte[] data){
        this.type = type;
        this.data = data;
    }

    public Message(byte type) {
        this.type = type;
        this.data = new byte[0];
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getContentLength() {
        return data.length;
    }

    public String getDataAsString(){
        return new String(data);
    }
}
