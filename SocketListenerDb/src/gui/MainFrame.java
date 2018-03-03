package gui;

import java.io.*;
import java.sql.SQLException;
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
import socket.SocketServer;
import utils.Utilidades;
import db.BDConnection;

/**
*
* @author RARS
*/

public class MainFrame extends JFrame
{
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
	private static String titulo = "Server listener";
	private JScrollPane scroll_text_log;
	private JLabel label_text_log;
	private JTextArea textarea_log;
	private JButton button_salir;
	private JButton button_reconectar;
	
	private SocketServer socket_server = null;
	
	Utilidades utils = new Utilidades();
	
	BDConnection conection = null;
	
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
		
		button_reconectar = new JButton( "Reconectar" );
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
		);	
		
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
		
		label_text_log = new JLabel( "Log" );
		label_text_log.setForeground( hex2Rgb(color_panel_label_foreground) );
		textarea_log = new JTextArea( "" );
		textarea_log.setBackground( hex2Rgb(color_panel_textarea_background) );
		textarea_log.setForeground( hex2Rgb(color_panel_label_foreground)  );
		textarea_log.setBorder( BorderFactory.createLineBorder( hex2Rgb(color_panel_border), 1 ) );
		textarea_log.setEditable(false);
		
		label_text_log.setBounds( 10, 30, 300, 20);
		scroll_text_log = new JScrollPane( textarea_log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_text_log.setBounds( new Rectangle( 5, 50, 980, 320 ) );
		scroll_text_log.setBackground( hex2Rgb(color_panel_scroll_background) );
		scroll_text_log.setBorder( BorderFactory.createLineBorder( hex2Rgb(color_panel_border), 1 ) );
		scroll_text_log.getVerticalScrollBar().setBackground( hex2Rgb(color_panel_scroll_bar_background) );
		scroll_text_log.getVerticalScrollBar().setBorder( BorderFactory.createLineBorder(  hex2Rgb(color_panel_scroll_bar_border), 1 )  );
		scroll_text_log.getHorizontalScrollBar().setBackground( hex2Rgb(color_panel_scroll_bar_background)  );
		scroll_text_log.getHorizontalScrollBar().setBorder( BorderFactory.createLineBorder( hex2Rgb(color_panel_scroll_bar_border), 1 )  );  
		
		add( label_text_log );
		add( scroll_text_log );
		add( button_reconectar );
		
		setUndecorated( true );
		setVisible( true );  
		
		if ( iniciarConfiguraciones() )
		{  
			iniciar();
		}
		else
		{
			mostrarMensajeErrorUsuario("Favor de verificar el archivo de configuración e intente reconectar: \n" + utils.ARCHIVO_CONFIGURACION);
		}
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
	        	//utils.printTagsValues();
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
	    if( socket_server != null )
	    {
	    	detener();
	    }
    	//Leer Archivo de Configuraciones
    	boolean resultado_leer = leerXml();
    	boolean resultado_bd = true;
    	try 
    	{
			conection = new BDConnection(
				utils.getTagValue("DATABASE_HOST").toString(), 
				utils.getTagValue("DATABASE_NAME").toString(), 
				utils.getTagValue("DATABASE_USER").toString(), 
				utils.getTagValue("DATABASE_PASS").toString()
			);
		} 
    	catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultado_bd = false;
		}
    	return resultado_leer && resultado_bd;
	}
	  
	private void newDate()
  	{
	    currentDate = new Date();
	    today_fecha = formatter_fecha.format(currentDate);
	    today_hora = formatter_hora.format(currentDate);
  	}
  
  	private void iniciar()
  	{
  		agregarLineaTextLogConFechaHora( "Iniciando servicio" );
  		socket_server = new SocketServer( this, conection, (int)utils.getTagValue("PUERTO") );
  		socket_server.start();
  	} 
  
  	private void detener()
  	{
    	agregarLineaTextLogConFechaHora( "Deteniendo servicio");
    	socket_server.detener();
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
