package net.gvcc.goffice.common.reporting;

import java.io.InputStream;

/**
 *
 * <p>
 * The <code>ReportBuilder</code> interface
 * </p>
 * <p>
 * Interfaccia che rappresenta un Report nel suo stato iniziale, prima che vi sia collegato un Template. Permette appunto di assegnare un template al Report.
 * 
 * Per ottenere un <code>ReportBuilder</code> si deve utilizzare {@link GofficeReportXDOC#builder()}
 * </p>
 * 
 * @author marco mancuso
 * @version 1.0
 */
public interface ReportBuilder {
	ReportBuilderWithTemplate setTemplate(InputStream template);
}