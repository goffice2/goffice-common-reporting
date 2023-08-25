package net.gvcc.goffice.common.reporting.exports;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * <p>
 * The <code>FlattenerService</code> class
 * </p>
 * <p>
 * Rappresenta la classe che esegue il flattening degli oggetti, offrendo la possibilità di trasformare qualsiasi lista di oggetti in un {@link net.gvcc.goffice.common.reporting.exports.FlatObject}.
 * <p>
 * Lavora con un {@link net.gvcc.goffice.common.reporting.exports.Flattener} che deve essere fornito in fase di instanziazione del servizio (passato come parametro del costruttore).
 * </p>
 * 
 * @author marco mancuso
 * @version 1.0
 * 
 * @see "Disegno Architetturale Moduli Core: Sistema di esposizione dati a software di aziende terze"
 * @see "Disegno Architetturale Moduli Core: Sistema di esposizione dati a software di aziende terze - Implementazione prototipale - Strategie di flattening"
 */

@Data
@AllArgsConstructor
public class FlattenerService {

	Flattener flattener;

	public FlatObject flatten(List<? extends Object> elements, int level) {
		Set<String> headers = new HashSet<String>();
		List<FlatObject> flats = elements.stream().map(ele -> {
			var flattened = flattener.flatten(ele);
			return flattened;
		}).toList();
		List<Map<String, Object>> elems = flats.stream().map(flat -> {
			return flat.getElements();
		}).flatMap(List::stream).toList();
		for (FlatObject flat : flats) {
			headers.addAll(flat.getHeaders());
		}
		FlatObject result = new FlatObject(elems, headers);
		return result;
	}

	public FlatObject flatten(List<? extends Object> elements) {
		return flatten(elements, -1);
	}

}
