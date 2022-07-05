package Server.src;

public class Myfile {
    private int id;
    private String name;
    private byte[] data;
    private String filextension;

    public Myfile(int id, String name, byte[] data, String filextension) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.filextension = filextension;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFilextension() {
        return filextension;
    }

    public void setFilextension(String filextension) {
        this.filextension = filextension;
    }
    
}
