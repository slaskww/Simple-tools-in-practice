package io;

import java.io.*;
import java.nio.charset.Charset;

public class DataInputStreamTests {

    public static void main(String[] args) throws IOException {

        FileReader fi = new FileReader("inp.txt");
        BufferedReader br = new BufferedReader(fi);

        FileOutputStream fo = new FileOutputStream("out", true);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fo, Charset.defaultCharset()));

        String line;

        while((line = br.readLine()) != null){
            System.out.println(line);
            bw.write(line);
            bw.newLine();
        }
    }

}
