
package fileserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import desencrypt.iDES;
import desencrypt.iDESImplementation;

public class FileServer {
    
    public static final String[] identity = {"fs", "FileServer", "p20H88zx"};
    public static final String key = "s3rvf1l3";
    
    public static String getRequest(String sesKey, String request){
        iDES d = new iDESImplementation();
        String fix="", conv="";
        String skey = d.DESde(sesKey, d.toBit(key));
        String dev[] = d.devn(64, request);
        for(int i=0;i<dev.length;i++){
            String plain = d.DESde(dev[i], skey);
            conv = d.convertChiper(plain);
            fix+=d.convertChiper(plain);
        }
        return fix;
    }
    
    public static int doRequest(String req){
        int ans=0;
        int a,b;
        if(req.charAt(0)=='a'){
            a = Integer.parseInt(String.valueOf(req.charAt(6)));
            b = Integer.parseInt(String.valueOf(req.charAt(8)));
            ans = a+b;
        }else if(req.charAt(0)=='m'){
            a = Integer.parseInt(String.valueOf(req.charAt(9)));
            b = Integer.parseInt(String.valueOf(req.charAt(11)));
            ans = a*b;
        }
        return ans;
    }
    
    public static void main(String[] args) throws IOException{
        ServerSocket listener = new ServerSocket(9000);
        String r[] = new String[2];
        try {
            System.out.println("File Server ready...");
            while (true) {
                Socket socket = listener.accept();
                try {
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    System.out.println("   Request detected!!!");
                    System.out.println("   Getting request...");
                    for(int i=0;i<2;i++){
                        String auth = input.readLine();
                        r[i] = auth;
                    }
                    String req = getRequest(r[0], r[1]);
                    System.out.println("   Compute request --> "+req);
                    int ans = doRequest(req);
                    System.out.println("   Sending request...");
                    out.println(ans);
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
