package net.gvcc.goffice.common.reporting.exports;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
/**
*
* <p>
* The <code>DataUtils</code> class
* </p>
* <p>
* Classe la cui principale responsabilità è di astrarre l’utilizzo di <code>jackson-databind</code> 
* Maschera la conversione di Oggetti Java in <code>Map&lt;String,Object&gt;</code> tramite {@link ObjectMapper#convertValue(Object, com.fasterxml.jackson.databind.JavaType)}
* </p>
* 
* 
* @author marco mancuso
* @version 1.0
* 
* @see "Disegno Architetturale Moduli Core: Sistema di esposizione dati a software di aziende terze"
*/
public class DataUtils {
	public static Map<String, Object> flattenObject(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return mapper.convertValue(object, Map.class);
	}
}
