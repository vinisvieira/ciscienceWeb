package br.com.ciscience.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtil {

	/**
	 * Converte um {@link Object} para String JSON
	 * 
	 * @param object
	 * @return {@link String}
	 */
	public static String objectToJSON(Object object) {
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
		return gson.toJson(object);
	}

}
