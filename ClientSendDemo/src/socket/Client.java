package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import gui.MainFrame;
import utils.GeoPoint;

/**
*
* @author RARS
*/

public class Client extends Thread
{
	private MainFrame mframe;
	
	private int puerto;
  	private Socket conexion = null; 
  	private GeoPoint punto = null;
  	private String ip_server; 
	  
	public Client( MainFrame _mframe, String _ip_server, int _puerto, GeoPoint _punto )
	{ 
		conexion = null; 
		ip_server = _ip_server;
		puerto = _puerto;
		punto = _punto;
		this.mframe = _mframe;
	}
	 	    
	@Override
	public void run()
	{ 
		DataOutputStream salida; 
		DataInputStream entrada; 
		try
		{ 
			String newLine = System.getProperty("line.separator");
			mframe.agregarLineaTextLogConFechaHora( "Intentando conectarse con el Servidor [" + ip_server + ":" + puerto + "]" );  
			conexion = new Socket( ip_server, puerto );
			salida = new DataOutputStream( conexion.getOutputStream() );
			entrada = new DataInputStream( conexion.getInputStream() );

			if(punto != null)
			{ 
				mframe.agregarLineaTextLogConFechaHora("Se ha establecido comunicacion con el Servidor" );   
				String lon = punto.getLongitud().toString();
				String lat = punto.getLatitud().toString();
				String id = String.valueOf(punto.getDispositivoId());
				salida.writeBytes( id + ";" + lon + ";" + lat ); 
				salida.writeBytes(newLine);
				
				String mensaje_confirmación = entrada.readUTF();
				if(mensaje_confirmación.equals("Correcto"))
				{
					mframe.agregarLineaTextLogConFechaHora( "Punto Ingresado (latitud, longitud) del dispositivo ["+ id +"]: ("+ lat + "," + lon + ")" );
				}
				else //if(mensaje_confirmación.equals("Error"))
				{
					mframe.agregarLineaTextLogConFechaHora( "Error al ingresar (latitud, longitud) del dispositivo ["+ id +"]: ("+ lat + "," + lon + ")" );
				}		
				
				salida.writeBytes( "close" ); 
				salida.writeBytes(newLine);
				conexion.close();
				mframe.agregarLineaTextLogConFechaHora("Se cerro la comunicación con el Servidor" ); 
			}
			else
			{
				mframe.agregarLineaTextLogConFechaHora( "El Servidor no ha otorgado acceso" );
				conexion.close();
			}
		}
		catch( IOException ex )  
		{ 
			mframe.agregarLineaTextLogConFechaHora( "Error al Conectar" );	
			try 
			{
				conexion.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

}
