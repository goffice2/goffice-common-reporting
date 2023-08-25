package net.gvcc.goffice.common.reporting.exports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * <p>
 * The <code>GofficeExport</code> class
 * </p>
 * <p>
 * Classe necessaria per l'ottenimento di un export.
 * <p>
 * Offre le seguenti funzionalità:
 * <ul>
 * <li>aggiunta della lista da esportare tramite {@link #addContent(List)}</li>
 * <li>configurazione delle intestazioi della tabella e delle colonne da esportare tramite {@link #addHeader(String, String)}</li>
 * </ul>
 * <p>
 * Gli {@link net.gvcc.goffice.common.reporting.exports.Exporter} che effettueranno il vero e proprio export utilizzano il metodo {@link #getFlatMapString()}
 * </p>
 * 
 * @author marco mancuso
 * @version 1.0
 * 
 * @see "Disegno Architetturale Moduli Core: Sistema di esposizione dati a software di aziende terze"
 */
public class GofficeExport {

	FlattenerService flattenerService;

	List<String> headers = new ArrayList<>();
	Map<String, String> headersMap = new HashMap<>();
	List<Object> content = new ArrayList<>();

	public List<Object> getContent() {
		return content;
	}

	FlatObject flatContent;

	public GofficeExport() {
		Map<String, String> map = new HashMap<>();
	}

	public GofficeExport addContent(List<? extends Object> objects) {
		this.content.addAll(objects);
		return this;
	}

	public GofficeExport flatten(Flattener flattener, int level) {
		this.flattenerService = new FlattenerService(flattener);
		this.flatContent = flattenerService.flatten(content, level);
		return this;
	}

	public GofficeExport flatten() {
		this.flattenerService = new FlattenerService(new VerticalFlattener());
		this.flatContent = flattenerService.flatten(content);
		return this;
	}

	public GofficeExport addHeader(String headerName, String objField) {
		headers.add(headerName);
		headersMap.put(headerName, objField);
		return this;
	}

	public List<Map<String, String>> getFlatMapString() {
		return getFlatMapString(flatContent.getElements(), flatContent.getHeaders());
	}

	private List<Map<String, String>> getFlatMapString(List<Map<String, Object>> objects, Set<String> allHeaders) {
		return objects.stream().map(ele -> {
			Map<String, String> objMap = new HashMap<>();
			if (headers != null && !headers.isEmpty()) {
				for (String headerName : headers) {
					String objField = headersMap.get(headerName);
					String objValue = ele.get(objField).toString();
					objMap.put(headerName, objValue);
				}
			} else {
				allHeaders.stream().forEach(header -> {
					objMap.put(header, (ele.get(header) != null) ? ele.get(header).toString() : "NULL");
				});
			}
			return objMap;
		}).toList();
	}
}
