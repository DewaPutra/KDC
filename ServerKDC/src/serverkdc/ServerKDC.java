package serverkdc;

import desencrypt.iDES;
import desencrypt.iDESImplementation;
import java.util.*;
import java.io.*;
import java.net.*;

public class ServerKDC {
    
    public static String username[] = {"AliceClient1", "ServerKDC", "FileServer"};
    public static String password[] = {"5xw8zp47", "83bjskdo", "p20H88zx"};
    public static String dbKey[] = {"5655676x", "s1e2r3v4", "s3rvf1l3"};
    private static Object thread;
    
    public static String reqAuth(String user, String clientUser, String clientPass, String req){
        String status = "";
        iDES d = new iDESImplementation();
        String key="";
        
        if(user.equalsIgnoreCase("c")) key = dbKey[0];
        else if(user.equalsIgnoreCase("kdc")) key = dbKey[1];
        else if(user.equalsIgnoreCase("fs")) key = dbKey[2];
        
        String dev[]= d.devn(64, clientUser);
        String getUser="";
        String k = d.toBit(key);
        for (int i=0;i<dev.length;i++){
            String plain = d.DESde(dev[i], k);
            getUser+=d.convertChiper(plain);
        }
        System.out.println("   Authenticating client: "+getUser);
        String dev1[]= d.devn(64, req);
        String k1 = d.toBit(key);
        for (int i=0;i<dev1.length;i++){
            String plain = d.DESde(dev1[i], k1);
            status+=d.convertChiper(plain);
        }
        
        String getPass = d.convertChiper(d.DESde(clientPass, k));
        int s = Arrays.asList(username).indexOf(getUser.trim());
        int s1 = Arrays.asList(password).indexOf(getPass);
        if(s>-1 && s1>-1) {
            System.out.println("   "+getUser.trim() + " auth success!!!");
        } else
        {
            status = "You are not member of this network!!!";
        }
        return status;
    }
    
    public static String[] ticketGen(String fsKey, String kdcKey, String req){
        iDES d = new iDESImplementation();
        String deReq="", enReq="", enkdcKey="", k="";
        String ticket[]=new String[2];
        
        //encrypted request
        k=d.toBit(kdcKey);
        String dev[] = d.devn(8, req);
        for(int i=0;i<dev.length;i++){
            dev[i]=d.addNULL(d.toBit(dev[i]));
            String chiper = d.DESen(dev[i], k);
            enReq+=chiper;       
        }
        
        //encrypted session key
        k=d.toBit(fsKey);
        enkdcKey = d.DESen(d.toBit(kdcKey), k);
        ticket[0] = enkdcKey;
        ticket[1] = enReq;
        return ticket;
    }
    
    public static void main(String[] args) throws IOException{
        String iden[] = new String[4];
        ServerSocket listener = new ServerSocket(9090);
        try {
            System.out.println("Server ready...");
            while (true) {
                Socket socket = listener.accept();
                try {
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    for(int i=0;i<4;i++){
                        String auth = input.readLine();
                        iden[i] = auth;
                    }
                    
                    String s = reqAuth(iden[0], iden[1], iden[2], iden[3]);
                    if(s!=null){
                        System.out.println("   Generate & Sending ticket...");
                        String[] ticket = ticketGen(dbKey[2], dbKey[1], s);
                        for(int i=0;i<ticket.length;i++){
                            out.println(ticket[i]);
                        }
                        System.out.println("   Ticket sent!");
                    }else{
                        out.println(s);
                    }
                } finally {
                    socket.close();
                }
            }
        }
        finally {
            listener.close();
        }
    }
    
}
