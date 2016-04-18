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
    
    public static String reqAuth(String user, String clientUser, String clientPass){
        String status = "";
        iDES d = new iDESImplementation();
        String key="";
        
        if(user == "c") key = dbKey[0];
        else if(user == "kdc") key = dbKey[1];
        else if(user == "fs") key = dbKey[2];
        
        String getUser = d.DESde(clientUser, key);
        String getPass = d.DESde(clientPass, key);
        int s = Arrays.asList(username).indexOf(getUser);
        int s1 = Arrays.asList(password).indexOf(getPass);
        if(s>0 && s1>0) {
            System.out.println(getUser + " auth success!!!");
            status = "auth success";
        }
        return status;
    }
    
    public static void main(String[] args) throws IOException{
        ServerSocket listener = new ServerSocket(9090);
        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(new Date().toString());
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
