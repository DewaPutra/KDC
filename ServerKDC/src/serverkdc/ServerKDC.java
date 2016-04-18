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
    
    public static void reqAuth(String user, String clientUser, String clientPass){
        iDES d = new iDESImplementation();
        String key="";
        if(user == "c") key = dbKey[0];
        else if(user == "kdc") key = dbKey[1];
        else if(user == "fs") key = dbKey[2];
        String getUser = d.DESde(clientUser, key);
        String getPass = d.DESde(clientPass, key);
    }
    
    public static void main(String[] args) {
    }
    
}
