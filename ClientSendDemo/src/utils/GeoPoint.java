package utils;

/**
*
* @author RARS
*/

public class GeoPoint 
{
	private int dispositivo_id;
	private Double latitud;
	private Double longitud;
	private Double velocidad;
	private String unidad_velocidad;
	private Double tiempo;
	private String unidad_tiempo;
	
	public GeoPoint()
	{
		latitud = 0.0;
		longitud = 0.0;
	}
	
	public GeoPoint(Double lon, Double lat )
	{
		latitud = lat;
		longitud = lon;
	}
	
	public GeoPoint(int id_dis, Double lon, Double lat)
	{
		dispositivo_id = id_dis;
		latitud = lat;
		longitud = lon;
	}
	
	public GeoPoint(int id_dis, Double lon, Double lat, Double vel, String u_vel, Double tie, String u_tie)
	{
		dispositivo_id = id_dis;
		latitud = lat;
		longitud = lon;
		velocidad = vel;
		unidad_velocidad = u_vel;
		tiempo = tie;
		unidad_tiempo = u_tie;
	}
	
	public Double getLatitud()
	{
		return latitud;
	}
	
	public Double getLongitud()
	{
		return longitud;
	}
	
	public int getDispositivoId()
	{
		return dispositivo_id;
	}
	
	public Double getVelocidad()
	{
		return velocidad;
	}
	
	public String getUnidadVelocidad()
	{
		return unidad_velocidad;
	}
	
	public Double getTiempo()
	{
		return tiempo;
	}
	
	public String getUnidadTiempo()
	{
		return unidad_tiempo;
	}
	
	public void setLatitud(Double lat)
	{
		latitud = lat;
	}
	
	public void setLongitud(Double lon)
	{
		longitud = lon;
	}
	
	public void setVelocidad(Double vel)
	{
		velocidad = vel;
	}
	
	public void setUnidadVelocidad(String u_vel)
	{
		unidad_velocidad = u_vel;
	}
	
	public void setTiempo(Double tie)
	{
		tiempo = tie;
	}
	
	public void setUnidadTiempo(String u_tie)
	{
		unidad_tiempo = u_tie;
	}
	
	public void setDispositivoId(int id)
	{
		dispositivo_id = id;
	}
}
