package net.gvcc.goffice.common.reporting.exports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * <p>
 * The <code>HorizontalFlattener</code> class
 * </p>
 * <p>
 * Permette la trasformazione di oggetti annidati in forma tabellare espandendo la tabella orizzontalmente, per ogni ivello di nesting, tramite l’aggiunta di nuove colonne e al tempo stesso calcola
 * l’insieme finale degli headers.
 * 
 * @author marco mancuso
 * @version 1.0
 * 
 * @see "Disegno Architetturale Moduli Core: Sistema di esposizione dati a software di aziende terze"
 * @see "Disegno Architetturale Moduli Core: Sistema di esposizione dati a software di aziende terze - Implementazione prototipale - Strategie di flattening"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HorizontalFlattener implements Flattener {
	private int level = -1;

	private static void expandHorizontally(String currentKey, Map<String, Object> object, Map<String, Object> currentObject, List<Map<String, Object>> objects, Set<String> headers) {
		for (Entry<String, Object> field : object.entrySet()) {
			if (field.getValue() == null) {
				currentObject.put(currentKey + field.getKey(), null);
			} else if (field.getValue().getClass().equals(ArrayList.class)) {
				int counter = 0;
				for (Map<String, Object> objectMap : ((ArrayList<Map<String, Object>>) field.getValue())) {
					expandHorizontally(currentKey + field.getKey() + "[" + counter++ + "].", objectMap, currentObject, objects, headers);
				}
			} else if (field.getValue().getClass().equals(LinkedHashMap.class)) {
				expandHorizontally(currentKey + field.getKey() + ".", (Map<String, Object>) field.getValue(), currentObject, objects, headers);
			} else {
				currentObject.put(currentKey + field.getKey(), field.getValue());
				headers.add(currentKey + field.getKey());
			}
		}
		if ("".equals(currentKey)) {
			objects.add(currentObject);
		}
	}

	@Override
	public FlatObject flatten(Object o, int level) {
		return flatten(o);
	}

	@Override
	public FlatObject flatten(Object o) {
		Set<String> headers = new HashSet<String>();
		Map<String, Object> currentObject = new HashMap<>();
		List<Map<String, Object>> objects = new ArrayList<>();
		expandHorizontally("", DataUtils.flattenObject(o), currentObject, objects, headers);
		FlatObject result = new FlatObject(objects, headers);
		return result;
	}

}
