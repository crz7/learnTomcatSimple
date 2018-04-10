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
			System.out.println("port=8080(Ĭ��)");
			port = 8080;
			e.printStackTrace();
		}
		
		try {
			serverSocket=new ServerSocket(port);
			System.out.println("���������ڼ����˿ڣ�"+serverSocket.getLocalPort());
			
			while(true) {
				try {
					final Socket socket=serverSocket.accept();
					System.out.println("��������ͻ���һ���µ�TCP���ӣ��ÿͻ��ĵ�ַΪ��"+socket.getInetAddress()
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
		/*��ȡHTTP������Ϣ*/
		InputStream socketIn=socket.getInputStream();//���������
		Thread.sleep(5000);
		int size=socketIn.available();
		byte[] buffer=new byte[size];
		socketIn.read(buffer);
		String request=new String(buffer);
		System.out.println(request);
		
		/*����HTTP����*/
		//��ȡHTTP����ĵ�һ��
		String firstLineOfRequest=request.substring(0,request.indexOf("\r\n"));
		//����HTTP����ĵ�һ��
		String[] parts=firstLineOfRequest.split(" ");
		String uri=parts[1]; //���HTTP�����е�URL
		
		/*����HTTP��Ӧ���ĵ�����*/
		String contentType;
		if(uri.indexOf("html")!=-1||uri.indexOf("htm")!=-1)
			contentType="text/html";
		else if(uri.indexOf("jpg")!=-1||uri.indexOf("jpeg")!=-1)
			contentType="image/jpeg";
		else if(uri.indexOf("gif")!=-1)
			contentType="image/gif";
		else
			contentType="application/octet-stream";//�ֽ�������
		
		
		/*����HTTP��Ӧ*/
		//HTTP��Ӧ�ĵ�һ��
		String responseFirstLine="HTTP/1.1 200 OK\r\n";
		//HTTP��Ӧͷ
		String responseHeader="Content-Type:"+contentType+"\r\n\r\n";
		//��ö�ȡ��Ӧ�������ݵ�������
		InputStream in=HTTPServer.class.getResourceAsStream("root/"+uri);
		
		/*����HTTP��Ӧ���*/
		OutputStream socketOut=socket.getOutputStream();//��������
		//����HTTP��Ӧ�ĵ�һ��
		socketOut.write(responseFirstLine.getBytes());
		socketOut.write(responseHeader.getBytes());
		//����HTTP��Ӧ����
		int len=0;
		buffer=new byte[128];
		while((len=in.read(buffer))!=-1) {}
		socketOut.write(buffer,0,len);
		
		Thread.sleep(1000);//˯��1s,�ȴ��ͻ�������Ӧ���
		socket.close();
		
		
	}
}
