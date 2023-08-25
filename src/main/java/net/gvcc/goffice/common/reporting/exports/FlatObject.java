package net.gvcc.goffice.common.reporting.exports;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * <p>
 * The <code>FlatObject</code> class
 * </p>
 * <p>
 * Classe che rappresenta l' oggetto finale restituto dal processo di flattening. Un FlatObject è composto da:
 * <ul>
 * <li>elements di tipo <code>List&lt;Map&lt;String, Object&gt;&gt;</code> che costituisce la rappresentazione tabellare di un qualsiasi oggetto Java</li>
 * <li>headers di tipo <code>Set&lt;String&gt;</code> che rappresenta le intestazioni della tabella</li>
 * </ul>
 * 
 * @author marco mancuso
 * @version 1.0
 * 
 * @see "Disegno Architetturale Moduli Core: Sistema di esposizione dati a software di aziende terze"
 */

@Data
@AllArgsConstructor
public class FlatObject {

	List<Map<String, Object>> elements;
	Set<String> headers;
}
