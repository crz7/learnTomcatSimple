package learnTomcat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {
	public static void main(String[] args) {
		int port;
		ServerSocket serverSocket;
		
		try {
			port=Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("port=8080(默认)");
			port = 8080;
			e.printStackTrace();
		}
		
		try {
			serverSocket=new ServerSocket(port);
			System.out.println("服务器正在监听端口："+serverSocket.getLocalPort());
			
			while(true) {
				try {
					final Socket socket=serverSocket.accept();
					System.out.println("建立了与客户的一个新的TCP连接，该客户的地址为："+socket.getInetAddress()
					+":"+socket.getPort());
					service(socket);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void service(Socket socket) throws Exception {
		/*读取HTTP请求信息*/
		InputStream socketIn=socket.getInputStream();//获得输入流
		Thread.sleep(5000);
		int size=socketIn.available();
		byte[] buffer=new byte[size];
		socketIn.read(buffer);
		String request=new String(buffer);
		System.out.println(request);
		
		/*解析HTTP请求*/
		//获取HTTP请求的第一行
		String firstLineOfRequest=request.substring(0,request.indexOf("\r\n"));
		//解析HTTP请求的第一行
		String[] parts=firstLineOfRequest.split(" ");
		String uri=parts[1]; //获得HTTP请求中的URL
		
		/*决定HTTP响应正文的类型*/
		String contentType;
		if(uri.indexOf("html")!=-1||uri.indexOf("htm")!=-1)
			contentType="text/html";
		else if(uri.indexOf("jpg")!=-1||uri.indexOf("jpeg")!=-1)
			contentType="image/jpeg";
		else if(uri.indexOf("gif")!=-1)
			contentType="image/gif";
		else
			contentType="application/octet-stream";//字节流类型
		
		
		/*创建HTTP响应*/
		//HTTP响应的第一行
		String responseFirstLine="HTTP/1.1 200 OK\r\n";
		//HTTP响应头
		String responseHeader="Content-Type:"+contentType+"\r\n\r\n";
		//获得读取响应正文数据的输入流
		InputStream in=HTTPServer.class.getResourceAsStream("root/"+uri);
		
		/*发送HTTP响应结果*/
		OutputStream socketOut=socket.getOutputStream();//获得输出流
		//发送HTTP响应的第一行
		socketOut.write(responseFirstLine.getBytes());
		socketOut.write(responseHeader.getBytes());
		//发送HTTP响应正文
		int len=0;
		buffer=new byte[128];
		while((len=in.read(buffer))!=-1) {}
		socketOut.write(buffer,0,len);
		
		Thread.sleep(1000);//睡眠1s,等待客户接受响应结果
		socket.close();
		
		
	}
}
