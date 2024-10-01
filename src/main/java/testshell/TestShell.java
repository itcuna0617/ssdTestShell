package testshell;

import fileio.FileIOImpl;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Scanner;

public class TestShell {
    String command;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    FileIOImpl io = new FileIOImpl();

    public TestShell(){
        try {
            System.out.println("명령어에 대한 설명이 필요하면 help를 입력하십시오.");

            while (true) {
                System.out.print("TestShell > ");
                this.command = br.readLine();

                String[] temp = command.split(" ");

                if (temp[0].equals("testapp1")) {
                    if(temp.length != 1){
                        System.out.println("명령어를 다시 확인해주세요. testapp1 명령어를 입력하세요.");
                    } else{
                        System.out.println("test1 실행. 채워넣을 데이터를 입력해주세요.");
                        testapp1();
                    }
                } else if (temp[0].equals("testapp2")) {
                    if(temp.length != 1){
                        System.out.println("명령어를 다시 확인해주세요. testapp2 명령어를 입력하세요.");
                    } else{
                        testapp2();
                    }
                } else if (temp[0].equals("exit")) {
                    if(temp.length != 1){
                        System.out.println("명령어를 다시 확인해주세요. break 명령어를 입력하세요.");
                    } else{
                        System.out.println("bye");
                        break;
                    }
                } else if (temp[0].equals("help")) {
                    if(temp.length != 1){
                        System.out.println("명령어를 다시 확인해주세요. help 명령어를 입력하세요.");
                    } else{
                        System.out.println("사용 가능 명령어");
                        System.out.println("- write [LBA num] [value] 데이터를 입력합니다.");
                        System.out.println("- read [LBA num] 데이터를 읽어옵니다.");
                        System.out.println("- exit 테스트를 종료합니다.");
                        System.out.println("- help 명령어를 표시합니다.");
                        System.out.println("- fullwrite 모든 데이터를 입력합니다.");
                        System.out.println("- fullread 모든 데이터를 읽어옵니다.");
                    }
                } else if (temp[0].equals("write")) {
                    if(Integer.parseInt(temp[1]) >= 100 || Integer.parseInt(temp[1]) < 0){
                        System.out.println("index 범위를 벗어났습니다.");
                    } else if(!temp[2].substring(2, temp[2].length()).matches("^[a-fA-F0-9]*$")
                            || !"0x".equals(temp[2].substring(0, 2)) || temp[2].length() != 10){
                        System.out.println("올바르지 않은 입력값입니다.");
                    } else if(temp.length != 3){
                        System.out.println("명령어를 다시 확인해주세요. W [인덱스] [값] 형태로 입력해주세요.");
                    } else {
                        String lba = temp[1];
                        String value = temp[2];
                        writeCommand(lba, value);
                    }
                } else if (temp[0].equals("read")){
                    if(Integer.parseInt(temp[1]) >= 100 || Integer.parseInt(temp[1]) < 0){
                        System.out.println("index 범위를 벗어났습니다.");
                    } else if(temp.length != 2){
                        System.out.println("명령어를 다시 확인해주세요. R [인덱스] 형태로 입력해주세요.");
                    } else{
                        String lba = temp[1];
                        readCommand(lba);
                    }
                } else if (temp[0].equals("fullwrite")){
                    if(temp.length != 1){
                        System.out.println("명령어를 다시 확인해주세요. fullwrite 명령어를 입력하세요.");
                    } else{

                    }
                    fullwrite();
                } else if (temp[0].equals("fullread")){
                    if(temp.length != 1){
                        System.out.println("명령어를 다시 확인해주세요. fullread 명령어를 입력하세요.");
                    } else{
                        fullread();
                    }

                }

                else {
                    System.out.println("INVALID COMMAND");
                    continue;
                }
            }
        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void testapp2() throws IOException, URISyntaxException {
//        String value = "0xAAAABBBB";
        String[] temp;
        boolean check = true;
        String lba = "";
        String result = "";
        for (int i=0; i<30; i++){
            String value = "0xAAAABBBB";
            for (int j=0; j<5; j++){
                lba = Integer.toString(j);
                writeCommand(lba,value);
            }
            System.out.printf("0xAAAABBBB %d번 write 성공!\n", i);
//            try{
//                Thread.sleep(2000);
//            } catch(InterruptedException e){
//                System.out.println("InterruptedException  오류 처리");
//            }
        }

        String second = "0x77777777";
        for (int i=0; i<5; i++){
            lba = Integer.toString(i);
            writeCommand(lba,second);
        }
        System.out.println("0x77777777 overwrite 성공!");

        temp = io.read("nand.txt").split("\\|");

        for (int i=0; i<5; i++){
            lba = Integer.toString(i);
            readCommand(lba);
            if(!temp[0].equals(io.read("result.txt"))) check = false;
            result += io.read("result.txt") + (i == 4 ? "" : "|");
        }

        if(check) System.out.println("이상 없음");
        else System.out.println("일치하지 않는 값 존재");

        io.write("result.txt", result);
    }

    public void testapp1() throws IOException, URISyntaxException {
        fullwrite();
        fullread();
        String fw = io.read("nand.txt");
        String fr = io.read("result.txt");

        if(fw.equals(fr)) System.out.println("fullwrite 후 fullread 실행 -> 이상 없음");
        else System.out.println("기록한 값과 읽은 값이 일치하지 않습니다.");
    }

    public void writeCommand(String lba, String value) throws IOException, URISyntaxException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "ssd.jar","W",lba,value);
        //processBuilder.directory(new File("C:\\ssd_project\\ssd-project\\src\\main\\java\\testshell"));
        Process ps = processBuilder.start();
        try{
            int exitCode = ps.waitFor();
//            System.out.println("프로세스 종료 코드 : " + exitCode);
            ps.destroy();
        } catch(InterruptedException e){
            System.out.println("InterruptedException 처리");
        }
    }
    public void readCommand(String lba) throws IOException, URISyntaxException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "ssd.jar","R",lba);
//        processBuilder.directory(new File("C:\\ssd_project\\ssd-project\\src\\main\\java\\testshell"));
        Process ps = processBuilder.start();
        try{
            int exitCode = ps.waitFor();
//            System.out.println("프로세스 종료 코드 : " + exitCode);
            ps.destroy();
        } catch(InterruptedException e){
            System.out.println("InterruptedException 처리");
        }
    }
    public void fullread() throws IOException, URISyntaxException {
        String[] readValue = io.read("nand.txt").split("\\|");
        String temp = "";
        for (int i=0; i<100; i++){
            temp += readValue[i] + (i == 99 ? "" : "|");
        }
        io.write("result.txt", temp);
    }
    public void fullwrite() throws IOException, URISyntaxException {
        System.out.print("값을 입력하세요 : ");
        String value = br.readLine();

        if(!value.substring(2, value.length()).matches("^[a-fA-F0-9]*$")
                || !"0x".equals(value.substring(0, 2)) || value.length() != 10){
            System.out.println("올바르지 않은 입력값입니다.");
        } else{
            for (int i=0; i<100; i++){
                String lba = Integer.toString(i);
                writeCommand(lba, value);
            }
        }
    }
}
