package socket;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import db.BDConnection;
import gui.MainFrame;

/**
*
* @author RARS
*/

public class SocketServer extends Thread
{
  private MainFrame mframe;
  private static int puerto;  
  private BDConnection conection;
  private Map<String,Socket> skClientes = new HashMap<>();
  //private Socket skCliente = null;
  private ServerSocket skServidor = null;
  private Server server;
  private boolean server_activo = false;
  
  public SocketServer(MainFrame _mframe, BDConnection _conection, int _puerto) 
  { 
	this.mframe = _mframe;
    puerto = _puerto;
    conection = _conection;
    //skCliente = null;
    skServidor = null;
    //skCliente = new Socket();
    skClientes = new HashMap<>();
  } 
  
  public void detener()
  {
	try 
	{
	  //skCliente.close();
	  for (Map.Entry<String, Socket> skc : skClientes.entrySet()) 
	  {
		((Socket)(skc.getValue())).close();
	  }
	  skServidor.close();
	} 
	catch (IOException ex) 
	{
	}
	server_activo = false;
	//skCliente = null;
	skClientes = new HashMap<>();
	skServidor = null;
	server = null;
	mframe.agregarLineaTextLogConFechaHora("Servidor Detenido" );
  }
  
  private String getIp() 
  {
    try 
    {
    	String os = System.getProperty("os.name").toLowerCase();
    	if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) 
    	{
    		Enumeration e = NetworkInterface.getNetworkInterfaces();
    		while (e.hasMoreElements()) 
    		{
				NetworkInterface n = (NetworkInterface) e.nextElement();
				NetworkInterface ni = NetworkInterface.getByName(n.getName());
				Enumeration<InetAddress> ias = ni.getInetAddresses();
				InetAddress iaddress;
				do 
				{
					iaddress = ias.nextElement();
				} 
				while (!(iaddress instanceof Inet4Address));
				return iaddress.getHostAddress().toString();
    		}
    	}
      return InetAddress.getLocalHost().getHostAddress().toString();  // for Windows and OS X
    } 
    catch(Exception ex) 
    {
      System.out.println("Error : " + ex.getMessage());
      return "-----";
    }
  }
  
  
  @Override
  public void run()
  {
    try
    {
      sleep( 1000 );
    }
    catch(Exception e)
    {
    }	
    server_activo = true;
	try 
	{ 
		while ( true && server_activo)
      	{  
			skServidor = new ServerSocket( puerto ); 
			mframe.agregarLineaTextLogConFechaHora(  "Servidor Iniciado [" + getIp() + ":" + puerto + "]" );
			while(true)
			{ 
				Socket sc = skServidor.accept();
				skClientes.put(sc.getInetAddress().toString(), sc);
				//skCliente = skServidor.accept();
				//server = new Server( mframe, skCliente );
				server = new Server( mframe, conection, sc );
				server.start();
			}
      	}
 	} 
	catch( IOException e )
	{ 
		server_activo = false;
		System.out.println("Error :" + e);
	}
}
  
}