package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 *
 * @author Roberto Alonso De la Garza Mendoza
 */
public class BDConnection {

    private String url = "";
    private String user = "";
    private String password = "";
    private Connection connection = null;
    
    //Tables Base de datos
    private static String key_table_dispositivo = "DISPOSITIVO";
    private static String key_table_historial_dispositivo = "HISTORIAL_DISPOSITIVO";
    
    private Map<String,Object> tables = new HashMap<String,Object>() 
    {
	  	{
	  		//Dispositivos
	  		put( 
	  			key_table_dispositivo, 
	  			new HashMap<String,Object>() 
	  			{
	  				{
	  				    put("nombre", "ss_dispositivo");
	  				    put(
	  				    	"columnas", 
	  				    	new ArrayList<Map<String,Object>>() {
	  				    		{
		  				    	    add(
		  				    	    	new HashMap<String,Object>() 
		  				    	  		{
		  				    	  			{
		  				    	  			    put("columna_nombre", "nombre");
		  				    	  			    put("columna_tipo", "String");
		  				    	  		  	}
		  				    	  		}	
		  				    	    );
		  				    	    add(
		  				    	    	new HashMap<String,Object>() 
		  				    	  		{
		  				    	  			{
		  				    	  			    put("columna_nombre", "opciones");
		  				    	  			    put("columna_tipo", "String");
		  				    	  		  	}
		  				    	  		}	
		  				    	    );
	  				    		}
	  				    	}
	  				    );
	  			  	}
	  			}
	  		);
	  		//Historial dispositivos
	  		put( 
	  				key_table_historial_dispositivo, 
		  			new HashMap<String,Object>() 
		  			{
		  				{
		  				    put("nombre", "ss_historial_dispositivo");
		  				    put(
		  				    	"columnas", 
		  				    	new ArrayList<Map<String,Object>>() {
		  				    		{
			  				    	    add(
			  				    	    	new HashMap<String,Object>() 
			  				    	  		{
			  				    	  			{
			  				    	  			    put("columna_nombre", "geopunto");
			  				    	  			    put("columna_tipo", "Point");
			  				    	  		  	}
			  				    	  		}	
			  				    	    );
			  				    	    add(
			  				    	    	new HashMap<String,Object>() 
			  				    	  		{
			  				    	  			{
			  				    	  			    put("columna_nombre", "dispositivo_m2o_id");
			  				    	  			    put("columna_tipo", "int");
			  				    	  		  	}
			  				    	  		}	
			  				    	    );
			  				    	    add(
			  				    	    	new HashMap<String,Object>() 
			  				    	  		{
			  				    	  			{
			  				    	  			    put("columna_nombre", "opciones");
			  				    	  			    //put("columna_tipo", "String");
			  				    	  			    put("columna_tipo", "Json");
			  				    	  			    put
			  				    	  			    (
			  				    	  			       "json_elements",
			  				    	  			       new HashMap<Integer,Object>() 
						  				    	  	   {
						  				    	  	     {
						  				    	  	    	put(
						  				    	  	    		 1,
						  				    	  	          	 new HashMap<String,Object>() 
								  				    	  	   	 {
						  				    	  	        	  	{
						  				    	  	        	  		put("name_element","velocidad");
						  				    	  	        	  		put("tipo","Float");
								  				    	  	     	}
								  				    	  	     }
						  				    	  	    	);
						  				    	  	    	
						  				    	  	    	put(
						  				    	  	    		 2,
						  				    	  	          	 new HashMap<String,Object>() 
								  				    	  	   	 {
						  				    	  	        	  	{
						  				    	  	        	  		put("name_element","unidad_velocidad");
						  				    	  	        	  		put("tipo","String");
						  				    	  	        	  		put("default","Km/h");
								  				    	  	     	}
								  				    	  	     }
						  				    	  	    	);
						  				    	  	    	put(
						  				    	  	    		 3,
						  				    	  	          	 new HashMap<String,Object>() 
								  				    	  	   	 {
						  				    	  	        	  	{
						  				    	  	        	  		put("name_element","tiempo");
						  				    	  	        	  		put("tipo","Float");
								  				    	  	     	}
								  				    	  	     }
						  				    	  	    	);
						  				    	  	    	put(
						  				    	  	    		 4,
						  				    	  	          	 new HashMap<String,Object>() 
								  				    	  	   	 {
						  				    	  	        	  	{
						  				    	  	        	  		put("name_element","unidad_tiempo");
						  				    	  	        	  		put("tipo","String");
						  				    	  	        	  		put("default","min.");
								  				    	  	     	}
								  				    	  	     }
						  				    	  	    	);
						  				    	  	     }
						  				    	  	   }
			  				    	  			    );
			  				    	  		  	}
			  				    	  		}	
			  				    	    );
			  				    	    add(
			  				    	    	new HashMap<String,Object>() 
			  				    	  		{
			  				    	  			{
			  				    	  			    put("columna_nombre", "fecha_registro");
			  				    	  			    put("columna_tipo", "Datetime");
			  				    	  		  	}
			  				    	  		}	
			  				    	    );
		  				    		}
		  				    	}
		  				    );
		  			  	}
		  			}
		  		);
	  	}
    };

    /**
     *
     * @param host the host location (locahost:port)
     * @param db_name the name of the database
     * @param user the user that can make the conection
     * @param password the password´s user
     * @throws SQLException if the conection can´t be made or one of more
     * arguments are wrong
     * @throws java.lang.ClassNotFoundException
     */
    public BDConnection(
            String host,
            String db_name,
            String user,
            String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.user = user;
        this.password = password;
        this.url = String.format("jdbc:mysql://%s/%s", host, db_name);
        this.connection = getConnection();
    }
    
    /**
     * Make the connection to the database
     *
     * @return Connection Object used for sql queries
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Close the actual connection to the datbase
     *
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        this.connection.close();
    }
    
    public String getTablaNombre( String key_table ) 
    {
    	HashMap<String,Object> table = (HashMap<String,Object>)(tables.get(key_table));
    	return table.get("nombre").toString();
    }
   
    public List<Map<String,Object>> getTablaColumas( String key_table ) 
    {
    	HashMap<String,Object> table = (HashMap<String,Object>)(tables.get(key_table));
    	return (List<Map<String,Object>>)(table.get("columnas"));
    }
    
    public String getTipoCampo( String key_table, String campo ) 
    {
    	HashMap<String,Object> table = (HashMap<String,Object>)(tables.get(key_table));
    	List<Map<String,Object>> columnas = (ArrayList<Map<String,Object>>)table.get("columnas");
    	for (Map<String, Object> c : columnas) {
			if( c.get("columna_nombre").equals(campo) )
			{
				return c.get("columna_tipo").toString();
			}
		}
    	return "";
    }

    //--------------------------------------------------------------------------
    /**
     * Basic CRUD
     * @throws SQLException 
     */
    
    public boolean insertHistorialDispositivo(int id_dispositivo, double longitud, double latitud, String opciones) throws SQLException
    {
    	Map<String,Object> values = new HashMap<>();
    	values.put("geopunto", longitud + "," + latitud);
    	values.put("dispositivo_m2o_id", id_dispositivo);
    	
    	String opts[] = opciones.split(",");
    	int cont_opts = 0;
    	JSONObject json = new JSONObject();
    	for (Map<String,Object> columna : getTablaColumas(key_table_historial_dispositivo)) 
    	{
			String tipo = columna.get("columna_tipo").toString();
			if(tipo.equals("Json"))
			{
				HashMap<Integer,Object> json_elements = (HashMap<Integer,Object>)columna.get("json_elements");
				for (Map.Entry<Integer, Object> e : json_elements.entrySet()) 
				{
					HashMap<String,Object> values_e = (HashMap<String,Object>)e.getValue();
					String _name = values_e.get("name_element").toString();
					String _tipo = values_e.get("tipo").toString();
					json.put(_name, opts[cont_opts]);
					cont_opts++;
				}
			}
		}
    	String json_opciones = json.toJSONString();
    	values.put("opciones", json_opciones );
    	
    	return insert(key_table_historial_dispositivo, values);
    }
    
    public boolean insertHistorialDispositivoWithRandomOptios(int id_dispositivo, double longitud, double latitud) throws SQLException
    {
    	Map<String,Object> values = new HashMap<>();
    	values.put("geopunto", longitud + "," + latitud);
    	values.put("dispositivo_m2o_id", id_dispositivo);
    	
    	JSONObject json = new JSONObject();
    	for (Map<String,Object> columna : getTablaColumas(key_table_historial_dispositivo)) 
    	{
			String tipo = columna.get("columna_tipo").toString();
			if(tipo.equals("Json"))
			{
				HashMap<Integer,Object> json_elements = (HashMap<Integer,Object>)columna.get("json_elements");
				for (Map.Entry<Integer, Object> e : json_elements.entrySet()) 
				{
					HashMap<String,Object> values_e = (HashMap<String,Object>)e.getValue();
					String _name = values_e.get("name_element").toString();
					String _tipo = values_e.get("tipo").toString();
					String _default = "";
					switch(_tipo) 
					{
						case "String":
							_default = values_e.containsKey("default") ?  values_e.get("default").toString() : "";
							break;
						case "Float":
							int min = 10;
							int max = 100;
							_default = (Math.random()*(max-min+1)+min) + "";
							break;
					}
					json.put(_name, _default);
				}
			}
		}
    	String json_opciones = json.toJSONString();
    	values.put("opciones", json_opciones );
    	
    	return insert(key_table_historial_dispositivo, values);
    }
    
    //Insert
    private boolean insert(
            String key_table,
            Map<String,Object> values
    ) throws SQLException 
    {
    	String columnas = "";
    	String valores = "";
    	for (Map<String,Object> columna : getTablaColumas(key_table)) 
    	{
			columnas += (columnas.equals("") ? "" : ",") + columna.get("columna_nombre").toString();
			String tipo = columna.get("columna_tipo").toString();
			valores += (valores.equals("") ? "" : ",") + ( tipo.equals("Datetime") ? "NOW()" : ( tipo.equals("Point") ? "POINT(?,?)" : "?" ));
		}
        PreparedStatement statement = connection.prepareStatement(
        	"INSERT INTO " + getTablaNombre(key_table) + " " +
        	"(" + columnas + ")"
        + " VALUES ("+ valores + ")"
        );
        int count = 1;
        for (Map<String,Object> columna : getTablaColumas(key_table)) 
    	{
        	String nombre = columna.get("columna_nombre").toString();
			String tipo = columna.get("columna_tipo").toString();
			switch (tipo) 
			{
				case "Point":
					String value_lotlan = values.get(nombre).toString();
					String[] split = value_lotlan.split(",");
					statement.setDouble(count, Float.parseFloat(split[0]) );
					count++;
					statement.setDouble(count, Float.parseFloat(split[1]) );
					break;
				case "int":
					statement.setInt(count, Integer.parseInt( values.get(nombre).toString() ) );
					break;
				case "float":
					statement.setFloat(count, Float.parseFloat( values.get(nombre).toString() ) );
					break;
				case "Double":
					statement.setDouble(count, Double.parseDouble( values.get(nombre).toString() ) );
					break;
				case "Datetime":
					//statement.setDate(count, new Date().new );
					break;
				case "Json":
					statement.setString(count, values.get(nombre).toString() );
					break;
				case "String":
				default:
					statement.setString(count, values.get(nombre).toString() );
					break;
			}
			count++;
		}
        int rows_added = statement.executeUpdate();
        statement.close();
        return rows_added > 0;
    }
    
    public List<JSONObject> obtenerTodosPuntosDispositivos() throws SQLException
    {
    	
    	return select(key_table_historial_dispositivo, new ArrayList<String>() );
    }
    
    public List<JSONObject> obtenerPuntosDispositivo(int dispositivo_id) throws SQLException
    {
    	ArrayList<Map<String,Object>> where = new ArrayList<Map<String,Object>>();
    	where.add(
    			new HashMap<String, Object>(){
    				{
    					put("where_campo", "dispositivo_m2o_id");
    					put("where_valor", dispositivo_id);
    					put("where_operador", "=");
    				}
    			}
    	);	
    	return select(key_table_historial_dispositivo, new ArrayList<String>(), where , "");
    }
    
    private List<JSONObject> select(String key_table, List<String> select_elements) throws SQLException
    {
    	return select(key_table, select_elements, new ArrayList<Map<String,Object>>(), "" );
    }
    
    private List<JSONObject> select(String key_table, List<String> select_elements, ArrayList<Map<String,Object>> where, String where_operator) throws SQLException
    {
    	List<JSONObject> tracks_list = new ArrayList<>();
        //JSONParser parser = new JSONParser();
        String string_select = "";
        if(select_elements.isEmpty())
        {
        	string_select = "*";
        	for (Map<String,Object> columna : getTablaColumas(key_table)) 
        	{
        		String tipo = getTipoCampo(key_table, columna.get("columna_nombre").toString() );
        		if(tipo.equals("Point"))
        		{
        			string_select += ", X("+ columna.get("columna_nombre").toString() + "), Y("+ columna.get("columna_nombre").toString() +")";
        		}

        	}
        }
        else
        {
        	for (Map<String,Object> columna : getTablaColumas(key_table)) 
        	{
        		if( select_elements.contains( columna.get("columna_nombre").toString() ) )
        		{
            		String tipo = getTipoCampo(key_table, columna.get("columna_nombre").toString() );
            		if(tipo.equals("Point"))
            		{
            			string_select += (string_select.equals("") ? "" : ",") + "X("+ columna.get("columna_nombre").toString() + "), Y("+ columna.get("columna_nombre").toString() +")";
            		}
            		else
            		{
            			string_select += (string_select.equals("") ? "" : ",") + columna.get("columna_nombre").toString();
            		}
        		}
    		}
        }
        String string_where = "";
        List<Map<String, Object>> where_valores = new ArrayList<>();
        for (Map<String,Object> w : where) 
        {
        	String campo = w.get("where_campo").toString();
        	String operador = w.get("where_operador").toString();
        	String valor = w.get("where_valor").toString();
        	String tipo = getTipoCampo(key_table, campo);
        	where_valores.add(
    			new HashMap<String, Object>() {
    				{
    					put("valor", valor );
    					put("tipo", tipo);
    				}
    			}
        	);
        	string_where += (string_where.equals("") ? "" : where_operator) + ( campo + operador + "?" );
		}
        PreparedStatement statement = connection.prepareStatement(
			"SELECT " + 
			string_select + " " +
	        "FROM " + getTablaNombre(key_table) + " " +
			(string_where.equals("") == false ? "WHERE " + string_where : "" )
        );
       
        int count = 1;
        for (Map<String, Object> v : where_valores) 
        {
			String tipo = v.get("tipo").toString();
			switch (tipo) 
			{
				case "Point":
					String value_lotlan = v.get("valor").toString();
					String[] split = value_lotlan.split(",");
					statement.setDouble(count, Float.parseFloat(split[0]) );
					count++;
					statement.setDouble(count, Float.parseFloat(split[1]) );
					break;
				case "int":
					statement.setInt(count, Integer.parseInt( v.get("valor").toString() ) );
					break;
				case "float":
					statement.setFloat(count, Float.parseFloat( v.get("valor").toString() ) );
					break;
				case "Double":
					statement.setDouble(count, Double.parseDouble( v.get("valor").toString() ) );
					break;
				case "Datetime":
					statement.setDate(count, Date.valueOf( v.get("valor").toString() ));
					break;
				case "String":
				default:
					statement.setString(count, v.get("valor").toString() );
					break;
			}
			count++;
		}
        ResultSet queryresult = statement.executeQuery();
        ResultSetMetaData md = queryresult.getMetaData();
        int columns = md.getColumnCount();
        while (queryresult.next()) 
        {
        	Map<String, Object> row = new HashMap<String, Object>();
        	for (int i = 1; i <= columns; i++) 
        	{
        		row.put(md.getColumnLabel(i).toLowerCase(), queryresult.getObject(i));
        	}
        	tracks_list.add(new JSONObject(row));
        }
        statement.close();
        queryresult.close();

        return tracks_list;
    }
    
    /**
     * Get a list of Tracks from the database
     *
     * @return a track´s list from database
     * @throws SQLException
     */
    public List<JSONObject> getTracks() throws SQLException {
        List<JSONObject> tracks_list = new ArrayList<>();
        JSONParser parser = new JSONParser();
        PreparedStatement statement = connection.prepareStatement("SELECT "
                + "ID,"
                + "id_mision,"
                + "id_usuario,"
                + "tipo_dato,"
                + "origen,"
                + "X(geoposicion),"
                + "Y(geoposicion),"
                + "elevacion,"
                + "fecha_creacion,"
                + "nombre,"
                + "descripcion,"
                + "datos_json,"
                + "visibilidad"
                + " FROM dato_tactico "
                + "WHERE id_mision = ? AND "
                + "eliminado = 0");
        ResultSet queryresult = statement.executeQuery();
        while (queryresult.next()) {
            Map<String, Object> track = new HashMap<>();
            track.put("ID", (Object) queryresult.getInt("ID"));
            track.put("id_mision", (Object) queryresult.getInt("id_mision"));
            track.put("id_usuario", (Object) queryresult.getInt("id_usuario"));
            track.put("tipo_dato", (Object) queryresult.getInt("tipo_dato"));
            track.put("origen", (Object) queryresult.getInt("origen"));
            track.put("x_geoposicion", queryresult.getFloat("X(geoposicion)"));
            track.put("y_geoposicion", queryresult.getFloat("Y(geoposicion)"));
            track.put("elevacion", (Object) queryresult.getFloat("elevacion"));
            track.put("fecha_creacion", (Object) queryresult.getTimestamp("fecha_creacion"));
            track.put("nombre", (Object) queryresult.getString("nombre"));
            track.put("descripcion", (Object) queryresult.getString("descripcion"));
            try {
                track.put("datos_json", (Object) parser.parse(queryresult.getString("datos_json")));
            } catch (ParseException ex) {
                track.put("datos_json", initilizeDatosJSON(null));
            }
            track.put("visibilidad", (Object) queryresult.getString("visibilidad"));
            tracks_list.add(new JSONObject(track));
        }

        statement.close();
        queryresult.close();

        return tracks_list;
    }

    /**
     * Get the Track that match with the ID
     *
     * @param ID the ID
     * @return JSONObject with the information of the track or null if there no
     * track that match with the ID
     * @throws java.sql.SQLException
     */
    public JSONObject getTrackbyID(int ID) throws SQLException {
        List<JSONObject> tracks_list = new ArrayList<>();
        JSONParser parser = new JSONParser();
        PreparedStatement statement = connection.prepareStatement("SELECT "
                + "ID,"
                + "id_mision,"
                + "id_usuario,"
                + "tipo_dato,"
                + "origen,"
                + "X(geoposicion),"
                + "Y(geoposicion),"
                + "elevacion,"
                + "fecha_creacion,"
                + "nombre,"
                + "descripcion,"
                + "datos_json,"
                + "visibilidad"
                + " FROM dato_tactico "
                + "WHERE ID = ? AND "
                + "id_mision = ? AND "
                + "eliminado = 0");
        statement.setInt(1, ID);
        ResultSet queryresult = statement.executeQuery();
        while (queryresult.next()) {
            Map<String, Object> track = new HashMap<>();
            track.put("ID", (Object) queryresult.getInt("ID"));
            track.put("id_mision", (Object) queryresult.getInt("id_mision"));
            track.put("id_usuario", (Object) queryresult.getInt("id_usuario"));
            track.put("tipo_dato", (Object) queryresult.getInt("tipo_dato"));
            track.put("origen", (Object) queryresult.getInt("origen"));
            track.put("x_geoposicion", queryresult.getFloat("X(geoposicion)"));
            track.put("y_geoposicion", queryresult.getFloat("Y(geoposicion)"));
            track.put("elevacion", (Object) queryresult.getFloat("elevacion"));
            track.put("fecha_creacion", (Object) queryresult.getTimestamp("fecha_creacion"));
            track.put("nombre", (Object) queryresult.getString("nombre"));
            track.put("descripcion", (Object) queryresult.getString("descripcion"));
            try {
                track.put("datos_json", (Object) parser.parse(queryresult.getString("datos_json")));
            } catch (ParseException ex) {
                track.put("datos_json", initilizeDatosJSON(null));
            }
            track.put("visibilidad", (Object) queryresult.getString("visibilidad"));
            tracks_list.add(new JSONObject(track));
        }

        statement.close();
        queryresult.close();

        if (tracks_list.size() == 1) {
            return tracks_list.get(0);
        }

        return null;
    }

    /**
     * Update the track that have the same ID
     *
     * @param track JSON Object that containt the information track;
     * @return true of false if the track was succesfuly updated
     * @throws java.sql.SQLException
     */
    public boolean updateTrack(JSONObject track) throws SQLException {

        return updateTrack(
                (int) track.get("ID"),
                (int) track.get("id_mision"),
                (int) track.get("id_usuario"),
                (int) track.get("tipo_dato"),
                (int) track.get("origen"),
                (float) track.get("x_geoposicion"),
                (float) track.get("y_geoposicion"),
                (float) track.get("elevacion"),
                (String) track.get("nombre"),
                (String) track.get("descripcion"),
                track.get("datos_json").toString(),
                (String) track.get("visibilidad"));
    }

    /**
     * Update the track that have the same ID
     *
     * @param ID
     * @param id_mision
     * @param id_usuario
     * @param tipo_dato
     * @param origen
     * @param x_geoposicion
     * @param y_geoposicion
     * @param elevacion
     * @param nombre
     * @param descripcion
     * @param datos_json
     * @param visibilidad
     * @return
     * @throws SQLException
     */
    public boolean updateTrack(
            int ID,
            int id_mision,
            int id_usuario,
            int tipo_dato,
            int origen,
            float x_geoposicion,
            float y_geoposicion,
            float elevacion,
            String nombre,
            String descripcion,
            String datos_json,
            String visibilidad) throws SQLException {

        PreparedStatement statement
                = connection.prepareStatement("UPDATE dato_tactico SET "
                        + "id_mision = ?,"
                        + "id_usuario = ?,"
                        + "tipo_dato = ?,"
                        + "origen = ?,"
                        + "geoposicion = POINT(?,?),"
                        + "elevacion = ?,"
                        + "nombre = ?,"
                        + "descripcion = ?,"
                        + "datos_json = ?,"
                        + "visibilidad = ? "
                        + "WHERE ID = ?");
        statement.setInt(1, id_mision);
        statement.setInt(2, id_usuario);
        statement.setInt(3, tipo_dato);
        statement.setInt(4, origen);
        statement.setFloat(5, x_geoposicion);
        statement.setFloat(6, y_geoposicion);
        statement.setFloat(7, elevacion);
        statement.setString(8, nombre);
        statement.setString(9, descripcion);
        statement.setString(10, datos_json);
        statement.setString(11, visibilidad);
        statement.setInt(12, ID);

        int rows_updated = statement.executeUpdate();
        statement.close();
        return rows_updated > 0;
    }

    /**
     * Delete a track that match with the ID
     *
     * @param track
     * @return
     * @throws SQLException
     */
    public boolean deleteTrack(JSONObject track) throws SQLException {
        return deleteTrack((int) track.get("ID"));
    }

    /**
     * Delete a track that match with the ID
     *
     * @param ID
     * @return
     * @throws SQLException
     */
    public boolean deleteTrack(int ID) throws SQLException {
        PreparedStatement statement
                = connection.prepareStatement("UPDATE dato_tactico set eliminado WHERE ID = ?");
        statement.setInt(1, ID);
        int rows_deleted = statement.executeUpdate();
        statement.close();
        return rows_deleted > 0;
    }

    //-------------------------------END-CRUD-----------------------------------
    /**
     * Miscellanies methods
     */
    /**
     * Make a simple json structure to fill the track column
     *
     * @param datos_json A json object to fill with the extra points structure
     * or null<br>
     * if want to create a total new json object
     * @return the JSONObject to fill the track column
     */
    public JSONObject initilizeDatosJSON(JSONObject datos_json) {
        return datos_json != null ? createJSON(datos_json) : createJSON(null);
    }

    /**
     * Make a simple json structure to fill the track column
     *
     * @param json A json object to fill with the extra points structure or
     * null<br>
     * if want to create a total new json object
     * @return the JSONObject to fill the track column
     */
    private JSONObject createJSON(JSONObject json) {
        JSONObject datos_json = json != null ? json : new JSONObject();
        return datos_json;
    }

    /**
     * Insert a new value into the json datos_json
     *
     * @param datos_json
     * @param key
     * @param value
     */
    public void insertValuetoJSON(JSONObject datos_json, Object key, Object value) {
        datos_json.put(key, value);
    }

}