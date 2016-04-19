package client;

import java.util.*;
import java.io.*;
import java.net.*;
import desencrypt.iDES;
import desencrypt.iDESImplementation;

public class Client {
    
    public static final String[] identity = {"c", "AliceClient1", "5xw8zp47", "adder(2+5)"};
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
            fix+=chiper;       
        } 
        return fix;
    }
    
        
    public static void main(String[] args) throws IOException{
        String serverKDC = "localhost";
        String fileServer = "localhost";
        String enIdentity[] = new String[4];
        for(int i=0; i<identity.length;i++){
            if(i==0) enIdentity[i] = identity[i];
            else
            {
                enIdentity[i] = logOn(identity[i], key);
            }
        }
        
        Socket s = new Socket(serverKDC, 9090);
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        for(int i=0; i<enIdentity.length;i++){
           out.println(enIdentity[i]);
        }
        String ticket[] = new String[2];
        for(int i=0;i<2;i++){
            String t = input.readLine();
            System.out.println(t+" "+t.length());
            ticket[i] = t;
        }
        
    }
    
}
