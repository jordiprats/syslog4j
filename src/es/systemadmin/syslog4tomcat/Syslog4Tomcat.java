package es.systemadmin.syslog4tomcat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.catalina.valves.AccessLogValve;

public class Syslog4Tomcat extends AccessLogValve
{
	protected static final String pattern="([^:]*):?([0-9]*)[\n\t ]*$";
	protected static final String info = "es.systemadmin.syslog4j";
	protected InetAddress address=null;
	protected int port=514;

	public void log(String message)
	{	
		if(address==null) return;
		
		byte[] messbytes=message.getBytes();

		DatagramPacket packet = new DatagramPacket(messbytes, messbytes.length, address, this.port);

		try 
		{
			DatagramSocket dsocket = new DatagramSocket();
			dsocket.send(packet);
			dsocket.close();
		} 
		catch (IOException e) {	e.printStackTrace(); }
	}

	public String getInfo()
	{
		return info;
	}

	protected void open()
	{
		try 
		{
			BufferedReader in= new BufferedReader(
					new FileReader(
							System.getProperty("catalina.base") + "/conf/syslog"));
			
		    String line = in.readLine();
		    
		    while((line==null)||(line.compareTo("")==0))
		    	line=in.readLine();
			
			RegexParser regexp=new RegexParser(line, Syslog4Tomcat.pattern);
			String fileip=regexp.getMatch(1);
			String fileport=regexp.getMatch(2);
			
			if((fileip==null)||(fileport==null))
				return;
			
			if(fileport.compareTo("")!=0)
				port=Integer.parseInt(fileport);
		
			address = InetAddress.getByName(fileip);
		} 
		catch (UnknownHostException e) { address=null; }
		catch (IOException x) { address=null; }
		catch (NumberFormatException ne) { address=null; } 
	}
	
}