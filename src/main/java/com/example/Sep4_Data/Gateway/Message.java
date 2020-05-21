package com.example.Sep4_Data.Gateway;

public class Message {
    private String cmd;
    private String EUI;
    private long ts;
    private boolean ack;
    private int fcnt;
    private int port;
    private String data;
    private int freq;
    private String dr;

    public Message(String cmd,String EUI,long ts,boolean ack,int fcnt,int port,String data,int freq,String dr){
        this.cmd=cmd;
        this.EUI=EUI;
        this.ts=ts;
        this.ack=ack;
        this.fcnt=fcnt;
        this.port=port;
        this.data=data;
        this.freq=freq;
        this.dr=dr;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getEUI() {
        return EUI;
    }

    public void setEUI(String EUI) {
        this.EUI = EUI;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public boolean isAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }

    public int getFcnt() {
        return fcnt;
    }

    public void setFcnt(int fcnt) {
        this.fcnt = fcnt;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }
}