package ssd;

public interface SSD {
    public void write(int index, String value);
    public String read(int index);
}
