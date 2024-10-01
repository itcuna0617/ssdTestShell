package fileio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileIOImpl implements FileIO {
    @Override
    public String read(String fileName) throws IOException {
        // 파일 경로를 프로젝트 폴더 아래로 설정
        FileInputStream fis = new FileInputStream(fileName);

        StringBuilder sb = new StringBuilder();
        int data;
        while ((data = fis.read()) != -1) {
            sb.append((char) data);
        }

        fis.close();
        return sb.toString();
    }

    @Override
    public void write(String fileName, String value) throws IOException {
        // 파일 경로를 프로젝트 폴더 아래로 설정
        FileOutputStream fos = new FileOutputStream(fileName);

        byte[] bytes = value.getBytes(); // 문자열을 바이트 배열로 변환
        fos.write(bytes);

        fos.flush();
        fos.close();
    }
}