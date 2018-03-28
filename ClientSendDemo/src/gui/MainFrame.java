package gui;


import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import socket.Client;
import utils.GeoPoint;
import utils.Utilidades;

/**
*
* @author RARS
*/

public class MainFrame extends JFrame
{

	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	  
	private SimpleDateFormat formatter_fecha = new SimpleDateFormat ("dd/MM/yyyy");
	private SimpleDateFormat formatter_hora = new SimpleDateFormat ("hh:mm:ss aa");
	private String today_fecha;
	private String today_hora;
	private Date currentDate;
	private FileWriter archivo_log;
	private PrintWriter pw;
	
	private JLabel label_titulo;
	private static String titulo = "Client Sender";
	private JLabel label_dispositivo_id;
	private JTextField text_dispositivo_id;
	private JLabel label_latitud;
	private JTextField text_latitud;
	private JLabel label_longitud;
	private JTextField text_longitud;
	
	private JLabel label_velocidad;
	private JTextField text_velocidad;
	private JComboBox cb_unidad_velocidad;
	private JLabel label_tiempo;
	private JTextField text_tiempo;
	private JComboBox cb_unidad_tiempo;
	
	private JScrollPane scroll_text_log;
	private JLabel label_text_log;
	private JTextArea textarea_log;
	private JButton button_salir;
	private JButton button_enviar;
	private JButton button_generar;
	//private JButton button_reconectar;
	
	Client client = null;
	Utilidades utils = new Utilidades();
	boolean configuraciones_ok = false;
	
	//Colores
	//Titulo
	String color_panel_title_brackground = "#242021";//( 36, 32, 33 )
	String color_panel_title_border = "#821919";//( 130, 25, 25 )
	String color_panel_title_foreground = "#e0e2e2";//( 224, 226, 226 )
	//Panel
	String color_panel_background = "#2b2b2b";//( 43, 43, 43 )
	String color_panel_label_foreground = "#e0e2e2";//( 224, 226, 226 )
	String color_panel_textarea_background = "#404040";//( 64, 64, 64 )
	String color_panel_button_background = "#bababa";//( 186, 186, 186 )
	String color_panel_button_foreground = "#000000";//( 0, 0, 0 )
	String color_panel_border = "#821919";//( 130, 25, 25 )
	String color_panel_scroll_background = "#111111";// ( 17, 17, 17 )
	String color_panel_scroll_bar_background = "#242021";//( 36, 32, 33 )
	String color_panel_scroll_bar_border = "#bababa";//( 186, 186, 186 )
	String color_panel_cursor_color = "#ffffff";//( 0, 0, 0 )
	
	public MainFrame( ) 
	{
		super( titulo );
		String path = System.getProperty("user.dir");
	    System.out.println("JAVA run as: " +path);
		
		utils.setPath(path);
		
		setSize( 1000, 430 );
		setLocationRelativeTo( null );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setResizable( false );    
		this.setLayout( null );
		
		getContentPane().setBackground( hex2Rgb(color_panel_background) );
		label_titulo = new JLabel();
		ImageIcon logo_icon = new ImageIcon(new ImageIcon( utils.IMAGE_PATH + "logo.png" ).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		label_titulo.setIcon( logo_icon );
		label_titulo.setText(titulo);
		label_titulo.setBounds( 0, 0, 970, 30 );
		label_titulo.setForeground( hex2Rgb(color_panel_title_foreground) );
		
		button_enviar = new JButton( "Enviar" );
		button_enviar.setBounds( 665, 35, 100, 60 ); 
		button_enviar.setBackground( hex2Rgb(color_panel_button_background) );
		button_enviar.setForeground( hex2Rgb(color_panel_button_foreground) );
		button_enviar.addActionListener( 
			new ActionListener() 
			{
				@Override
				public void actionPerformed( ActionEvent e ) 
				{
					if(configuraciones_ok)
					{
						String t_id_dispotivo =  text_dispositivo_id.getText();
						String t_latitud =  text_latitud.getText();
						String t_longitud =  text_longitud.getText();
						
						String t_velocidad =  text_velocidad.getText();
						String t_unidad_velocidad =  (String)cb_unidad_velocidad.getSelectedItem();
						String t_tiempo =  text_tiempo.getText();
						String t_unidad_tiempo =  (String)cb_unidad_tiempo.getSelectedItem();;
						
						boolean error = false;
						String error_mensaje = "";
						int ID_dispositivo = 0;
						double latitud = 0.0;
						double longitud = 0.0;
						double velocidad = 0.0;
						double tiempo = 0.0;
						try 
						{
							ID_dispositivo = Integer.parseInt(t_id_dispotivo);
						} 
						catch (NumberFormatException e2) 
						{
						    //e2.printStackTrace();
						    error = true;
						    error_mensaje += "El ID del dispositivo es incorrecto\n";
						}
						try 
						{
							latitud = Double.parseDouble(t_latitud);
						} 
						catch (NumberFormatException e1) 
						{
						    //e1.printStackTrace();
						    error = true;
						    error_mensaje += "La latidud es incorrecta\n";
						}
						try 
						{
							longitud = Double.parseDouble(t_longitud);
						} 
						catch (NumberFormatException e2) 
						{
						    //e2.printStackTrace();
						    error = true;
						    error_mensaje += "La longidud es incorrecta\n";
						}
						try 
						{
							velocidad = Double.parseDouble(t_velocidad);
						} 
						catch (NumberFormatException e2) 
						{
						    //e2.printStackTrace();
						    error = true;
						    error_mensaje += "La velocidad es incorrecta\n";
						}
						try 
						{
							tiempo = Double.parseDouble(t_tiempo);
						} 
						catch (NumberFormatException e2) 
						{
						    //e2.printStackTrace();
						    error = true;
						    error_mensaje += "El tiempo es incorrecto\n";
						}
						if(error == true)
						{
							error_mensaje = "Favor de verificar: \n" + error_mensaje;
							mostrarMensajeErrorUsuario(error_mensaje);
						}
						else
						{
							agregarLineaTextLogConFechaHora("Ingresando punto (latitud, longitud ) del dispositivo ["+ ID_dispositivo +"]: ("+ latitud + "," + longitud + ")" );
							GeoPoint p = new GeoPoint(ID_dispositivo, longitud, latitud, velocidad, t_unidad_velocidad, tiempo, t_unidad_tiempo);
							iniciar(p);
						}
					}
				}	
			}
		);	
		
		button_generar = new JButton( "Enviar punto aleatorio" );
		button_generar.setBounds( 775, 35, 205, 60 ); 
		button_generar.setBackground( hex2Rgb(color_panel_button_background) );
		button_generar.setForeground( hex2Rgb(color_panel_button_foreground) );
		button_generar.addActionListener( 
			new ActionListener() 
			{
				@Override
				public void actionPerformed( ActionEvent e ) 
				{
					if(configuraciones_ok)
					{
						GeoPoint random_point = utils.generateRandomPoint();
						int ID_dispositivo = 0;
						if(!(text_dispositivo_id.getText().equals("")))
						{
							String t_id_dispotivo =  text_dispositivo_id.getText();
							try 
							{
								ID_dispositivo = Integer.parseInt(t_id_dispotivo);
							} 
							catch (NumberFormatException e2) 
							{
							    //e2.printStackTrace();
							}
						}
						else
						{
							ID_dispositivo = (int)(Math.random()*(10-1+1)+1);
						}
						random_point.setDispositivoId(ID_dispositivo);
						Double velocidad = (Math.random()*(100-10+1)+10);
						Double tiemṕo = (Math.random()*(100-10+1)+10);
						random_point.setVelocidad(velocidad);
						random_point.setTiempo(tiemṕo);
						int unidad_velocidad_index = ((Double)(Math.random()*((cb_unidad_velocidad.getItemCount()-1)-1+1)+1)).intValue();
						String unidad_velocidad = (String) cb_unidad_velocidad.getItemAt(unidad_velocidad_index);
						random_point.setUnidadVelocidad(unidad_velocidad);
						int unidad_tiempo_index = ((Double)(Math.random()*((cb_unidad_tiempo.getItemCount()-1)-1+1)+1)).intValue();
						String unidad_tiempo = (String) cb_unidad_tiempo.getItemAt(unidad_tiempo_index);
						random_point.setUnidadTiempo(unidad_tiempo);
						double latitud = random_point.getLatitud();
						double longitud = random_point.getLongitud();
						agregarLineaTextLogConFechaHora("Ingresando punto (latitud, longitud) del dispositivo ["+ ID_dispositivo +"]: ("+latitud + "," + longitud + ")" );
						iniciar(random_point);
						text_latitud.setText( String.valueOf(latitud) );
						text_longitud.setText( String.valueOf(longitud) );
						text_dispositivo_id.setText( String.valueOf(ID_dispositivo) );
						text_velocidad.setText( String.valueOf(velocidad) );
						cb_unidad_velocidad.setSelectedIndex(unidad_velocidad_index);
						text_tiempo.setText( String.valueOf(tiemṕo) );
						cb_unidad_tiempo.setSelectedIndex(unidad_tiempo_index);
					}
				}	
			}
		);	
		
		/*button_reconectar = new JButton( "Reconectar" );
		button_reconectar.setBounds( 425, 390, 150, 30 ); 
		button_reconectar.setBackground( hex2Rgb(color_panel_button_background) );
		button_reconectar.setForeground( hex2Rgb(color_panel_button_foreground) );
		button_reconectar.addActionListener( 
			new ActionListener() 
			{
				@Override
				public void actionPerformed( ActionEvent e ) 
				{
					//Reintentar
					if ( iniciarConfiguraciones() )
					{  
						iniciar();
					}
					else
					{
						mostrarMensajeErrorUsuario("Favor de verificar el archivo de configuración e intente reconectar: \n" + utils.ARCHIVO_CONFIGURACION);
					}
				}	
			}
		);	*/
		
		button_salir = new JButton( new ImageIcon( utils.IMAGE_PATH + "close.gif" ) );
		button_salir.setRolloverIcon( new ImageIcon( utils.IMAGE_PATH + "close_hov.gif" ) );
		button_salir.setBorder( null );
		button_salir.setOpaque( false );
		button_salir.setContentAreaFilled( false );
		button_salir.setBorderPainted( false );
		button_salir.setBounds( 971, 5, 19, 19 ); 
		button_salir.addActionListener( 
			new ActionListener() 
			{
			  @Override
			  public void actionPerformed( ActionEvent e ) 
			  {
				  //Salir
				  dispose(); 
				  System.exit(0); 
			  }	
			}
		);	
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout( null );
		titlePanel.setBackground( hex2Rgb(color_panel_title_brackground) );
		titlePanel.setBorder(BorderFactory.createLineBorder(  hex2Rgb(color_panel_title_border), 3 ) );
		titlePanel.setBounds( 0, 0, 1000, 30 );
		titlePanel.add( label_titulo );
		titlePanel.add( button_salir );
		titlePanel.addMouseListener( 
			new MouseAdapter()
			{ 		
				//Mouse preciosando			
				@Override
				public void mousePressed( MouseEvent e ) 
				{
					x = e.getX();
					y = e.getY();
				}
			} 
		);
		titlePanel.addMouseMotionListener( 
			new MouseMotionAdapter() 
			{ 
				//Arrastrar Mouse 
				@Override
				public void mouseDragged(  MouseEvent e  )
				{
					Point point = MouseInfo.getPointerInfo().getLocation();
			    	setLocation( point.x - x, point.y - y );
				}	
			} 
		);
		add(titlePanel);
		
		label_dispositivo_id = new JLabel( "ID dispositivo: " );
		label_dispositivo_id.setForeground( hex2Rgb(color_panel_label_foreground) );
		label_dispositivo_id.setBounds( 10, 40, 110, 20);
		
		text_dispositivo_id= new JTextField();
		text_dispositivo_id.setBackground( hex2Rgb(color_panel_textarea_background) );
		text_dispositivo_id.setForeground( hex2Rgb(color_panel_label_foreground) );
		text_dispositivo_id.setBorder( BorderFactory.createLineBorder( hex2Rgb(color_panel_border), 1 ) );
		text_dispositivo_id.setBounds( 120, 40, 50, 20);
		text_dispositivo_id.setCaretColor( hex2Rgb(color_panel_cursor_color) );
		
		label_longitud = new JLabel( "Longitud: " );
		label_longitud.setForeground( hex2Rgb(color_panel_label_foreground) );
		label_longitud.setBounds( 180, 40, 75, 20);
		
		text_longitud = new JTextField();
		text_longitud.setBackground( hex2Rgb(color_panel_textarea_background) );
		text_longitud.setForeground( hex2Rgb(color_panel_label_foreground) );
		text_longitud.setBorder( BorderFactory.createLineBorder( hex2Rgb(color_panel_border), 1 ) );
		text_longitud.setBounds( 265, 40, 150, 20);
		text_longitud.setCaretColor( hex2Rgb(color_panel_cursor_color) );
		
		label_latitud = new JLabel( "Latitud: " );
		label_latitud.setForeground( hex2Rgb(color_panel_label_foreground) );
		label_latitud.setBounds( 425, 40, 75, 20);
		
		text_latitud = new JTextField();
		text_latitud.setBackground( hex2Rgb(color_panel_textarea_background) );
		text_latitud.setForeground( hex2Rgb(color_panel_label_foreground) );
		text_latitud.setBorder( BorderFactory.createLineBorder( hex2Rgb(color_panel_border), 1 ) );
		text_latitud.setBounds( 500, 40, 150, 20);
		text_latitud.setCaretColor( hex2Rgb(color_panel_cursor_color) );
		
		label_velocidad = new JLabel( "Velocidad: " );
		label_velocidad.setForeground( hex2Rgb(color_panel_label_foreground) );
		label_velocidad.setBounds( 10, 70, 85, 20);
		
		text_velocidad = new JTextField();
		text_velocidad.setBackground( hex2Rgb(color_panel_textarea_background) );
		text_velocidad.setForeground( hex2Rgb(color_panel_label_foreground) );
		text_velocidad.setBorder( BorderFactory.createLineBorder( hex2Rgb(color_panel_border), 1 ) );
		text_velocidad.setBounds( 95, 70, 150, 20);
		text_velocidad.setCaretColor( hex2Rgb(color_panel_cursor_color) );
		
		cb_unidad_velocidad = new JComboBox(new String[]{"Km/h", "m/s", "mi/h"});
		cb_unidad_velocidad.setBackground( hex2Rgb(color_panel_textarea_background) );
		cb_unidad_velocidad.setForeground( hex2Rgb(color_panel_label_foreground) );
		cb_unidad_velocidad.setBorder( BorderFactory.createLineBorder( hex2Rgb(color_panel_border), 1 ) );
		cb_unidad_velocidad.setBounds( 255, 70, 80, 20);
		
		label_tiempo = new JLabel( "Tiempo: " );
		label_tiempo.setForeground( hex2Rgb(color_panel_label_foreground) );
		label_tiempo.setBounds( 345, 70, 75, 20);
		
		text_tiempo = new JTextField();
		text_tiempo.setBackground( hex2Rgb(color_panel_textarea_background) );
		text_tiempo.setForeground( hex2Rgb(color_panel_label_foreground) );
		text_tiempo.setBorder( BorderFactory.createLineBorder( hex2Rgb(color_panel_border), 1 ) );
		text_tiempo.setBounds( 405, 70, 150, 20);
		text_tiempo.setCaretColor( hex2Rgb(color_panel_cursor_color) );
		
		cb_unidad_tiempo = new JComboBox(new String[]{"min.", "hrs.", "segs."});
		cb_unidad_tiempo.setBackground( hex2Rgb(color_panel_textarea_background) );
		cb_unidad_tiempo.setForeground( hex2Rgb(color_panel_label_foreground) );
		cb_unidad_tiempo.setBorder( BorderFactory.createLineBorder( hex2Rgb(color_panel_border), 1 ) );
		cb_unidad_tiempo.setBounds( 560, 70, 80, 20);
		
		label_text_log = new JLabel( "Log" );
		label_text_log.setForeground( hex2Rgb(color_panel_label_foreground) );
		label_text_log.setBounds( 10, 55, 200, 20);
		textarea_log = new JTextArea( "" );
		textarea_log.setBackground( hex2Rgb(color_panel_textarea_background) );
		textarea_log.setForeground( hex2Rgb(color_panel_label_foreground) );
		textarea_log.setBorder( BorderFactory.createLineBorder( hex2Rgb(color_panel_border), 1 ) );
		textarea_log.setEditable(false);
		
		scroll_text_log = new JScrollPane( textarea_log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_text_log.setBounds( new Rectangle( 5, 105, 980, 320 ) );
		scroll_text_log.setBackground( hex2Rgb(color_panel_scroll_background) );
		scroll_text_log.setBorder( BorderFactory.createLineBorder( hex2Rgb(color_panel_border), 1 ) );
		scroll_text_log.getVerticalScrollBar().setBackground( hex2Rgb(color_panel_scroll_bar_background) );
		scroll_text_log.getVerticalScrollBar().setBorder( BorderFactory.createLineBorder(  hex2Rgb(color_panel_scroll_bar_border), 1 )  );
		scroll_text_log.getHorizontalScrollBar().setBackground( hex2Rgb(color_panel_scroll_bar_background)  );
		scroll_text_log.getHorizontalScrollBar().setBorder( BorderFactory.createLineBorder( hex2Rgb(color_panel_scroll_bar_border), 1 )  );  
		
		add( label_dispositivo_id );
		add( text_dispositivo_id );
		add( label_longitud );
		add( text_longitud );
		add( label_latitud );
		add( text_latitud );
		
		add( label_velocidad );
		add( text_velocidad );
		add( cb_unidad_velocidad );
		add( label_tiempo );
		add( text_tiempo);
		add( cb_unidad_tiempo );
		
		add( button_enviar );
		add( button_generar );
		//add( label_text_log );
		add( scroll_text_log );
		//add( button_reconectar );
		
		setUndecorated( true );
		setVisible( true );  
		
		if ( iniciarConfiguraciones() == false)
		{  
			mostrarMensajeErrorUsuario("Favor de verificar el archivo de configuración e intente reconectar: \n" + utils.ARCHIVO_CONFIGURACION);
		}
		else
		{
			configuraciones_ok = true;
		}
		text_dispositivo_id.setEnabled(configuraciones_ok);
		text_longitud.setEnabled(configuraciones_ok);
		text_latitud.setEnabled(configuraciones_ok);
		button_enviar.setEnabled(configuraciones_ok);
		button_generar.setEnabled(configuraciones_ok);
	}  
	
	private boolean leerXml()
	{   
	    SAXBuilder builder = new SAXBuilder();
	    File xmlFile = new File( utils.ARCHIVO_CONFIGURACION );
	    
	    try 
	    {      
	      Document document = ( Document ) builder.build( xmlFile );
	      Element rootNode = document.getRootElement();
	      java.util.List<Element> list = rootNode.getChildren( "configuraciones" );
	 
	      for ( int i = 0; i < list.size(); i++) 
	      {
	        Element node = (Element) list.get(i);
	        if(node.getChildren().size() == utils.getTagsCount()) 
	        {
	        	for (Map.Entry<String, Object> tag : utils.getTags().entrySet()) 
	        	{
	        	    //System.out.println("clave=" + tag.getKey() + ", valor=" + tag.getValue());
	        		utils.setTagValue( tag.getKey(),  node.getChildText( utils.getTagName(tag.getKey()) ) );
	        	}
	        	utils.printTagsValues();
	        	return true;
	        }
	      }
	    }
	    catch (IOException io) 
	    {
	      System.out.println(io.getMessage());
	      return false;
		} 
	    catch (JDOMException jdomex) 
	    {
	      System.out.println(jdomex.getMessage());
	      return false;
		}
	    return false;
	}
	
	private boolean iniciarConfiguraciones()
	{
    	return leerXml();
	}
	  
	private void newDate()
  	{
	    currentDate = new Date();
	    today_fecha = formatter_fecha.format(currentDate);
	    today_hora = formatter_hora.format(currentDate);
  	}
     
    public void agregarLineaTextLogConFechaHora(String linea)
    {
    	agregarLineaTextLog(linea, true);
    }
    
    public void agregarLineaTextLog(String linea)
    {
    	agregarLineaTextLog(linea, false);
    }
    
    public void agregarLineaTextLog(String linea, boolean fecha_hora)
    {
    	newDate();
    	if(fecha_hora)
    	{
    		linea = today_fecha + " " + today_hora + " - " + linea;
    	}
    	System.out.println(linea);
    	textarea_log.append( linea + "\n" );
    	try 
    	{
    		textarea_log.setCaretPosition( textarea_log.getLineStartOffset( textarea_log.getLineCount() - 1 ) );
    	} 
    	catch (BadLocationException ex) 
    	{
    	}
    	String s_archivo_log = utils.generaNombreArchivoLog();
    	try 
    	{
			archivo_log = new FileWriter( s_archivo_log, true );
			pw = new PrintWriter( archivo_log );
		  	pw.println( linea );
		  	pw.close();
		} 
    	catch (IOException e) 
    	{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
    }
    
    private void iniciar(GeoPoint punto)
  	{
    	client = new Client(this, utils.getTagValue("SERVER_IP").toString(), (int)utils.getTagValue("PUERTO"), punto);
    	client.start();
  	} 
    
    /*private void detener()
    {
      //client.setSalir( true );
      ////client.setReconectar( true );
      //client.desconectar();
      client.interrupt();
      client = null;
      System.out.println("detenido");
    }*/
    
    private void mostrarMensajeAdvertenciaUsuario(String mensaje)
    {
    	mostrarMensajeUsuario( mensaje,	"Advertencia", JOptionPane.WARNING_MESSAGE );
    }
    
    private void mostrarMensajeErrorUsuario(String mensaje)
    {
    	mostrarMensajeUsuario( mensaje,	"Error", JOptionPane.ERROR_MESSAGE );
    }
    
    private void mostrarMensajePlanoUsuario(String titulo, String mensaje)
    {
    	mostrarMensajeUsuario( mensaje,	titulo, JOptionPane.PLAIN_MESSAGE );
    }
    
    private void mostrarMensajeInformacionrUsuario(String mensaje)
    {
    	mostrarMensajeUsuario( mensaje,	"Información", JOptionPane.INFORMATION_MESSAGE );
    }
    
    private void mostrarMensajeUsuario(String mensaje, String titulo, int tipo_mensaje)
    {
    	JOptionPane.showMessageDialog( new Frame(), mensaje, titulo, tipo_mensaje );
    	System.out.println( mensaje );
    }
    
    public Color hex2Rgb(String color_hex) 
    {
    	return utils.hex2Rgb(color_hex);
    }
	  
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		MainFrame frame = new MainFrame();
	}

}

