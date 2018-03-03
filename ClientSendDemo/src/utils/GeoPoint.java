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
	
	public void setLatitud(Double lat)
	{
		latitud = lat;
	}
	
	public void setLongitud(Double lon)
	{
		longitud = lon;
	}
	
	public void setDispositivoId(int id)
	{
		dispositivo_id = id;
	}
}
