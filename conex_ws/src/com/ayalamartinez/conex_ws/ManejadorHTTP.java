package com.ayalamartinez.conex_ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ManejadorHTTP {

	public static String getData(String uri){
		//uri: uniform resource identifier	
		//metodo estatico que envia una URI para identificar el WS en la web 
		BufferedReader reader = null; 
		//BufferedReader es un miembro del paquete JavaIO, por lo cual hay que importar el paquete 
		try {
			URL url = new URL(uri); 
			//creamos el objeto del tipo URL, y le pasamos el uri que recibe el metodo estatico
			HttpURLConnection con = (HttpURLConnection) url.openConnection(); 
			StringBuilder sb = new StringBuilder(); 
			reader = new BufferedReader(new InputStreamReader(con.getInputStream())); 
			String line = "";
			while ((line = reader.readLine()) != null ) {
				//	el while realiza la verificacion de que cuando el readline llega al final de la linea, detiene el loop 
				sb.append(line + "\n"); 
			}
			return sb.toString(); 
		} catch (Exception e) {
			e.printStackTrace(); 
			return null; 
		}
		finally {
			if (reader != null) {
				try {
					reader.close(); 
				} catch (IOException e) {
					e.printStackTrace(); 
					return null; 
				}
			}
		}

	}

}


