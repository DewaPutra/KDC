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
            fix+=chiper;       
        } 
        return fix;
    }
    
        
    public static void main(String[] args) throws IOException{
        System.out.println("Client ready...");
        String serverKDC = "localhost";
        String fileServer = "localhost";
        ArrayList<String> nIdentity = new ArrayList<String>();
        nIdentity.add(identity[0]);
        nIdentity.add(identity[1]);
        nIdentity.add(identity[2]);
        System.out.print("Type your request: ");
        Scanner in = new Scanner(System.in);
        String offer = in.nextLine();
        nIdentity.add(offer);
        String enIdentity[] = new String[4];
        for(int i=0; i<nIdentity.size();i++){
            if(i==0) enIdentity[i] = nIdentity.get(i);
            else
            {
                enIdentity[i] = logOn(nIdentity.get(i), key);
            }
        }
            
                Socket s = new Socket(serverKDC, 9090);
                Socket s1 = new Socket(fileServer, 9000);
                try {
                    PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    PrintWriter out1 = new PrintWriter(s1.getOutputStream(), true);
                    BufferedReader input1 = new BufferedReader(new InputStreamReader(s1.getInputStream()));
                    //send identity and request
                    for(int i=0; i<enIdentity.length;i++){
                       out.println(enIdentity[i]);
                    }
                    //get ticket or denied
                    for(int i=0;i<2;i++){
                        String t = input.readLine();
                        if(t==null) break;
                        out1.println(t);
                    }
                    String ans = input1.readLine();
                    System.out.println("answer from fileserver: "+ans);
                   } finally {
                    s.close();
                    s1.close();
                }
        
            }
            
  }

