package client;

import java.util.*;
import java.io.*;
import java.net.*;
import desencrypt.iDES;
import desencrypt.iDESImplementation;

public class Client {
    
    public static final String[] identity = {"c", "AliceClient1", "5xw8zp47"};
    public static final String key = "5655676x";
    
    public static String logOn(String plain, String key){
        iDES c = new iDESImplementation();
        String dev[]= c.devn(8, plain);
        String conv="";
        String fix="";
        String k = c.toBit(key);
        for (int i=0;i<dev.length;i++){
            dev[i]=c.addNULL(c.toBit(dev[i]));
            String chiper = c.DESen(dev[i], k);
            conv = c.convertChiper(chiper);
            fix+=conv;       
        } 
        return fix;
    }
    
    public static void main(String[] args) throws IOException{
        String serverAddress = "localhost";
//        Socket s = new Socket(serverAddress, 9090);
//        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
//        BufferedReader input =
//            new BufferedReader(new InputStreamReader(s.getInputStream()));
//        String answer = input.readLine();
//        System.out.println(answer);
    }
    
}
