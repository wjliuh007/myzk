package zk.console.client;

import org.apache.zookeeper.data.Stat;

public class Node {
    
    private String path;
    
    private String data;
    
    private Stat stat;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }

}
