package net.gvcc.goffice.common.reporting.exports;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.gvcc.goffice.common.reporting.exceptions.GofficeExportException;

/**
 *
 * <p>
 * The <code>XmlExporter</code> class
 * </p>
 * <p>
 * Classe che permette l’esportazione di un {@link net.gvcc.goffice.common.reporting.exports.GofficeExport} in formato XML. La classe permette di ottenere la rappresentazione del suddetto XML nei
 * seguenti formati:
 * <ul>
 * <li>{@link String}</li>
 * <li>{@link String} Base64</li>
 * <li>{@link byte[]}</li>
 * </ul>
 *
 * <p>
 * La classe astrae inoltre l’utilizzo di <code>jackson-dataformat-xml</code> al fine di consentire l’espostazione in formato XML.
 *
 * @author marco mancuso
 * @version 1.0
 * 
 * @see "Disegno Architetturale Moduli Core: Sistema di esposizione dati a software di aziende terze"
 */
@Data
@AllArgsConstructor
public class XmlExporter implements Exporter {

	@Override
	public byte[] exportBytes(GofficeExport export) throws GofficeExportException {
		return exportString(export).getBytes();

	}

	@Override
	public String exportBase64(GofficeExport export) throws GofficeExportException {
		try {
			return Base64.encodeBase64String(exportString(export).getBytes());
		} catch (GofficeExportException e) {
			throw new GofficeExportException("Unable to create XML", e);
		}

	}

	@Override
	public String exportString(GofficeExport export) throws GofficeExportException {
		XmlMapper mapper = new XmlMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		String xml;
		try {
			xml = mapper.writeValueAsString(export.getContent());
			System.out.println(xml);
			return xml;
		} catch (JsonProcessingException e) {
			throw new GofficeExportException("Unable to create XML", e);
		}

	}
}
