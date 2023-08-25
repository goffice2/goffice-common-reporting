package net.gvcc.goffice.common.reporting;

import net.gvcc.goffice.common.reporting.exceptions.ReportEngineException;

/**
 *
 * <p>
 * The <code>GofficeReport</code> interface
 * </p>
 * <p>
 * Interfaccia che rappresenta un Report già elaborato. Permette l’ottenimento dei bytes del report ODT e la generazione di un PDF
 * </p>
 * <p>
 * Si ottiene invocando il metodo {@link ReportBuilderWithTemplate#build()}.
 * </p>
 * 
 * @author marco mancuso
 * @version 1.0
 */
public interface GofficeReport {
	byte[] getReportBytes();

	GofficeReportPdf generatePdf() throws ReportEngineException;
}
