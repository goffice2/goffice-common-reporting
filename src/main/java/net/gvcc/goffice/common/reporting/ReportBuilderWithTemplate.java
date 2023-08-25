package net.gvcc.goffice.common.reporting;

import java.util.List;
import java.util.Map;

import net.gvcc.goffice.common.reporting.exceptions.ReportEngineException;

/**
 *
 * <p>
 * The <code>ReportBuilderWithTemplate</code> interface
 * </p>
 * <p>
 * Interfaccia che rappresenta un Report con un template collegato. Si ottiene invocando il metodo {@link ReportBuilder#setTemplate(java.io.InputStream)}. Permette di aggiungere variabili di contesto
 * e di effettuare l’elaborazione del report.
 * 
 * </p>
 * 
 * @author marco mancuso
 * @version 1.0
 */
public interface ReportBuilderWithTemplate {
	public ReportBuilderWithTemplate addContextVariable(String name, Object value);
	public ReportBuilderWithTemplate addFieldsMetadata(List<String> metadata);

	public ReportBuilderWithTemplate setContext(Map<String, Object> contextMap);

	public GofficeReport build() throws ReportEngineException;
}