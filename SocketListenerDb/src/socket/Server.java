package socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.SQLException;

import db.BDConnection;
import gui.MainFrame;
import utils.Utilidades;

/**
 *
 * @author RARS
 */

public class Server extends Thread
{
  private MainFrame mframe;
  private BDConnection conection;
	
  private Socket socket_cliente;  
  private DataInputStream entreda; 
  private DataOutputStream salida;
  
  private String ip_equipo_conectado;
		
  public Server( MainFrame _mframe, BDConnection _conection, Socket Socket )
  { 
	this.mframe = _mframe;
    this.socket_cliente = Socket;
    this.conection = _conection;
  }
    
  @Override
  public void run()
  { 
    try 
    { 
      entreda = new DataInputStream( socket_cliente.getInputStream() );
      salida = new DataOutputStream( socket_cliente.getOutputStream() );
      ip_equipo_conectado = socket_cliente.getInetAddress().toString();
      ip_equipo_conectado = ip_equipo_conectado.substring( 1, ip_equipo_conectado.length() );
      mframe.agregarLineaTextLogConFechaHora( "Conectando con: " + ip_equipo_conectado );
	  mframe.agregarLineaTextLogConFechaHora( "Se establecio comuniciación con " + ip_equipo_conectado );
	  String envio = "";

		  BufferedReader brin = new BufferedReader(new InputStreamReader(entreda));
		  envio = brin.readLine();  		        
		  
		  String[] split_envio = envio.split(";");
		  if(split_envio.length == 3)
		  {
			  int id_dispositivo = Integer.parseInt(split_envio[0]);
			  Double longitud = Double.parseDouble(split_envio[1]);
			  Double latitud = Double.parseDouble(split_envio[2]);
			  boolean result_insert = false;
			  try 
			  {
				result_insert = conection.insertHistorialDispositivo(id_dispositivo, longitud, latitud);
				/*System.out.println("+++++++");
				new Utilidades().printList(conection.obtenerTodosPuntosDispositivos());
				System.out.println("+++++++");
				new Utilidades().printList(conection.obtenerPuntosDispositivo(1));
				System.out.println("+++++++");*/
			  } 
			  catch (NumberFormatException | SQLException e) 
			  {
				result_insert = false;
				e.printStackTrace();
			  }
			  if(result_insert)
			  {
				  mframe.agregarLineaTextLogConFechaHora( "Punto Ingresado (latitud, longitud) del dispositivo ["+ id_dispositivo +"]: ("+ latitud + "," + longitud + ")" );
				  salida.writeUTF( "Correcto" );
			  }
			  else
			  {
				  mframe.agregarLineaTextLogConFechaHora( "Error al ingresar (latitud, longitud) del dispositivo ["+ id_dispositivo +"]: ("+ latitud + "," + longitud + ")" );
				  salida.writeUTF( "Error" );
			  }
		  }
		  else
		  {
			  mframe.agregarLineaTextLogConFechaHora("Los parametros son incorrectos");
		  }
		  socket_cliente.close();
		  mframe.agregarLineaTextLogConFechaHora( "Se cerro la comuniciacion con " + ip_equipo_conectado );
	  
	 
  	} 
  	catch( IOException e )
  	{ 
  		e.printStackTrace();
  		try
  		{ 
  			salida.writeUTF( "Acceso Denegado" ); 
  			socket_cliente.close();
  		}
  		catch( IOException e2 )
  		{
  		}  
  	}
  } 
  
}