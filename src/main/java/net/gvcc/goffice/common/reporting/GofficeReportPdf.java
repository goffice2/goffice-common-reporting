package net.gvcc.goffice.common.reporting;

/**
 *
 * <p>
 * The <code>GofficeReportPdf</code> interface
 * </p>
 * <p>
 * Interfaccia che rappresenta un ReportPdf già elaborato e permette l’ottenimento dei bytes del report. Si ottiene invocando {@link GofficeReport#generatePdf()}.
 * </p>
 * 
 * @author marco mancuso
 * @version 1.0
 */
public interface GofficeReportPdf {
	byte[] getPdfBytes();
}
