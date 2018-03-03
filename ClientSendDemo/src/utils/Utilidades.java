package utils;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*
* @author RARS
*/

public class Utilidades 
{
  public static String PATH = "";
  public static String IMAGE_PATH = PATH + "/images/";
  public static String CONFIGURACIONES_XML = "config.xml";
  
  public static String DIRECTORIO_EJECUCION;
  public static String ARCHIVO_CONFIGURACION;  
  //Config tags
  //public static int TIEMPO_ESPERA_SEGUNDOS_VERIFICADOR_EQUIPOS;
  //public static int TIEMPO_ESPERA_SEGUNDOS_VERIFICADOR_BD;
  //public static int PUERTO;
  //public static String ARCHIVO_EQUIPOS;
  //public static String BASE_DATOS
  private static Map<String,Object> tags_values = new HashMap<String,Object>() 
  {
	{
		put( 
			"SERVER_IP", 
			new HashMap<String,Object>() 
			{
				{
				    put("tag_name", "server_ip");
				    put("tag_type", "String");
				    put("tag_value", "");
			  	}
			}
		);
		put( 
			"PUERTO", 
			new HashMap<String,Object>() 
			{
				{
				    put("tag_name", "puerto");
				    put("tag_type", "int");
				    put("tag_value", "");
			  	}
			}
		);
		put( 
			"ARCHIVO_LOG", 
			new HashMap<String,Object>() 
			{
				{
				    put("tag_name", "archivo_log");
				    put("tag_type", "String");
				    put("tag_value", "");
			  	}
			}
		);
	}
  };

  public void Utilidades(String path)
  {
    this.PATH = path;
  }
  
  public void setPath(String path)
  {
	  PATH = path;
	  DIRECTORIO_EJECUCION = path;
	  IMAGE_PATH = PATH + "/images/";
	  ARCHIVO_CONFIGURACION = PATH + "/" + CONFIGURACIONES_XML;
  }
  
  public Map<String, Object> getTags()
  {
	  return tags_values;
  }
  
  public void setTagValue(String tag, Object value) 
  {
	  ((Map<String, Object>) tags_values.get(tag)).replace("tag_value", value);
  }
  
  public Object getTagValue(String tag)
  {
	  String type = ((Map<String, Object>) tags_values.get(tag)).get("tag_type").toString();
	  switch (type) {
		case "int":
			return Integer.parseInt(((Map<String, Object>) tags_values.get(tag)).get("tag_value").toString());
		case "String":
			return ((Map<String, Object>) tags_values.get(tag)).get("tag_value").toString();
		case "double":
			return Double.parseDouble(((Map<String, Object>) tags_values.get(tag)).get("tag_value").toString());
		default:
			return ((Map<String, Object>) tags_values.get(tag)).get("tag_value");
	  }
	  
  }
  
  public String getTagName(String tag)
  {
	  return ((Map<String, Object>) tags_values.get(tag)).get("tag_name").toString();
  }
  
  public int getTagsCount() 
  {
	  return tags_values.size(); 
  }
  
  int num_tabs = 0;
  public String getStringTabs(int num_t)
  {
      String tabs = "";
      for (int tab = 1; tab <= num_t; tab++)
      {
          tabs = tabs + "\t";
      }
      return tabs;
  }
  
  public void printTagsValues()
  {
	  printMap(tags_values);
  }
  
  public void printMap(Map<String,Object> ts)
  {
	
	for (Map.Entry<String, Object> t : ts.entrySet()) 
  	{
		boolean is_dictionary = t.getValue() instanceof Map;
		boolean is_list = t.getValue() instanceof List; 
		System.out.println(getStringTabs(num_tabs) + t.getKey() + ":" + (is_dictionary || is_list ? "" : t.getValue().toString()));
		if(is_dictionary)
		{
			num_tabs++;
	        System.out.println(getStringTabs(num_tabs - 1) + "{");
	        printMap((Map<String, Object>) t.getValue());
	        System.out.println(getStringTabs(num_tabs - 1) + "}");
	        num_tabs--;
		}
		if (is_list)
		{
	      for( Map<String,Object> tt : (List<Map<String,Object>>)t.getValue() )
	      {
	          num_tabs++;
	          System.out.println(getStringTabs(num_tabs - 1) + "[");
	          printMap(tt);
	          System.out.println(getStringTabs(num_tabs - 1) + "]");
			  num_tabs--;
	      }
		}
  	}
  }
  
  public Color hex2Rgb(String color_hex) 
  {
	return new Color
	(
	    Integer.valueOf( color_hex.substring( 1, 3 ), 16 ),
	    Integer.valueOf( color_hex.substring( 3, 5 ), 16 ),
	    Integer.valueOf( color_hex.substring( 5, 7 ), 16 ) 
	);
  }
  
  public GeoPoint generateRandomPoint() {
	  int min_lng = 95;
	  int max_lng = 100;
	  int min_lat = 18;
	  int max_lat = 20;
	  double lat = (Math.random()*(max_lat-min_lat+1)+min_lat);
	  double lng = Math.random()*(max_lng-min_lng+1)+min_lng*(-1);
	  GeoPoint point = new GeoPoint(lng, lat);
	  return point;  
  }
  
  public String fillZeroesLeft(int number)
  {
	  return fillZeroesLeft(2, String.valueOf(number) );
  }
  
  public String fillZeroesLeft(String str)
  {
	  return fillZeroesLeft(2, str);
  }
  
  public String fillZeroesLeft(int digits, String str)
  {
      while (str.length() < digits)
      {
          str = "0" + str;
      }
      return str;
  }
  
  public String generaNombreArchivoLog()
  {
	  Calendar fecha = Calendar.getInstance();
	  return( 
    		PATH + "/" + 
    		getTagValue("ARCHIVO_LOG") + "_" +
    		fecha.get(Calendar.YEAR) + 
    		fillZeroesLeft(fecha.get(Calendar.MONTH) + 1) +
    		fillZeroesLeft(fecha.get(Calendar.DAY_OF_MONTH)) + 
    		".log" 
	  );
  }

}