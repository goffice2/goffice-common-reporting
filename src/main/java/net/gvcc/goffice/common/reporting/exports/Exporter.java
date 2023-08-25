package net.gvcc.goffice.common.reporting.exports;

import net.gvcc.goffice.common.reporting.exceptions.GofficeExportException;

/**
 *
 * <p>
 * The <code>Exporter</code> interface
 * </p>
 * <p>
 * Interfccia che permette l’esportazione di un {@link net.gvcc.goffice.common.reporting.exports.GofficeExport} nei seguenti formati:
 * <ul>
 * <li>{@link String}</li>
 * <li>{@link String} Base64</li>
 * <li>{@link byte[]}</li>
 * </ul>
 * *
 * 
 * @author marco mancuso
 * @version 1.0
 * 
 * @see "Disegno Architetturale Moduli Core: Sistema di esposizione dati a software di aziende terze"
 */
public interface Exporter {

	byte[] exportBytes(GofficeExport export) throws GofficeExportException;

	String exportBase64(GofficeExport export) throws GofficeExportException;

	String exportString(GofficeExport export) throws GofficeExportException;

}
