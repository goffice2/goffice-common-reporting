package net.gvcc.goffice.common.reporting.exports;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.gvcc.goffice.common.reporting.exceptions.GofficeExportException;

/**
 *
 * <p>
 * The <code>CSVExporter</code> class
 * </p>
 * <p>
 * Classe che permette l’esportazione di un {@link net.gvcc.goffice.common.reporting.exports.GofficeExport} in formato CSV. La classe permette di ottenere la rappresentazione del suddetto CSV nei
 * seguenti formati:
 * <ul>
 * <li>{@link String}</li>
 * <li>{@link String} Base64</li>
 * <li>{@link byte[]}</li>
 * </ul>
 * 
 * @author marco mancuso
 * @version 1.0
 * 
 * @see "Disegno Architetturale Moduli Core: Sistema di esposizione dati a software di aziende terze"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CSVExporter implements Exporter {

	Flattener flattener = new VerticalFlattener();

	@Override
	public byte[] exportBytes(GofficeExport export) throws GofficeExportException {
		return exportString(export).getBytes();
	}

	@Override
	public String exportBase64(GofficeExport export) throws GofficeExportException {
		return Base64.encodeBase64String(exportString(export).getBytes());
	}

	@Override
	public String exportString(GofficeExport export) throws GofficeExportException {
		try (StringWriter writer = new StringWriter()) {
			export.flatten(flattener, flattener.getLevel());
			csvWriter(export.getFlatMapString(), writer);
			return writer.toString();
		} catch (IOException e) {
			throw new GofficeExportException("Unable to create CSV", e);
		}
	}

	private void csvWriter(List<Map<String, String>> map, Writer writer) throws IOException {
		CsvSchema schema = null;
		CsvSchema.Builder schemaBuilder = CsvSchema.builder();
		if (map != null && !map.isEmpty()) {
			for (String col : map.get(0).keySet()) {
				schemaBuilder.addColumn(col);
			}
			schema = schemaBuilder.build().withLineSeparator(System.lineSeparator()).withHeader();
		}
		CsvMapper mapper = new CsvMapper();
		mapper.writer(schema).writeValues(writer).writeAll(map);
		writer.flush();
	}
}
