package net.gvcc.goffice.common.reporting;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.opensagres.xdocreport.core.XDocReportException;
import lombok.Getter;
import net.gvcc.goffice.common.reporting.exceptions.ReportEngineException;

/**
 *
 * <p>
 * The <code>GofficeReportXDOC</code> class
 * </p>
 * <p>
 * Classe concreta che rappresenta il Report in tutte le fasi del suo cicplo di vita. Implementa le interfacce:
 * <ul>
 * <li>{@link net.gvcc.goffice.common.reporting.ReportBuilder}</li>
 * <li>{@link net.gvcc.goffice.common.reporting.ReportBuilderWithTemplate}</li>
 * <li>{@link net.gvcc.goffice.common.reporting.GofficeReport}</li>
 * <li>{@link net.gvcc.goffice.common.reporting.GofficeReportPdf}</li>
 * </ul>
 * <p>
 * Il design di tale classe è finalizzato ad offrire un API semantica che rende il più trasparente possibile il protocollo di utilizzo. L’elaborazione del report e la generazione del PDF sono delegate
 * alla classe {@link net.gvcc.goffice.common.reporting.ReportingEngine}
 * 
 * @author marco mancuso
 * @version 1.0
 */
public class GofficeReportXDOC implements ReportBuilder, ReportBuilderWithTemplate, GofficeReport, GofficeReportPdf {
	@Getter
	InputStream template;
	Map<String, Object> contextMap;
	@Getter
	List<String> fieldsMetadata;

	byte[] reportBytes;
	byte[] pdfBytes;
	ReportingEngine engine;

	public Map<String, Object> getContextMap() {
		return new HashMap<>(contextMap);
	}

	public GofficeReport build() throws ReportEngineException {
		engine = new ReportingEngine();
		try {
			reportBytes = engine.generateReport(this);
		} catch (IOException | XDocReportException e) {
			throw new ReportEngineException("Unable to generate the report", e);
		}
		return this;
	}

	public byte[] getReportBytes() {
		return reportBytes == null ? null : Arrays.copyOf(reportBytes, reportBytes.length);
	}

	public static ReportBuilder builder() {
		GofficeReportXDOC report = new GofficeReportXDOC();
		report.contextMap = new HashMap<>();
		return report;
	}

	@Override
	public ReportBuilderWithTemplate addContextVariable(String name, Object value) {
		contextMap.put(name, value);
		return this;
	}

	@Override
	public ReportBuilderWithTemplate setTemplate(InputStream template) {
		this.template = template;
		return this;
	}

	@Override
	public GofficeReportPdf generatePdf() throws ReportEngineException {
		try {
			pdfBytes = engine.generatePdf(this);
		} catch (Exception e) {
			throw new ReportEngineException("Unable to generate the pdf", e);
		}
		return this;
	}

	@Override
	public byte[] getPdfBytes() {
		return pdfBytes;
	}

	@Override
	public ReportBuilderWithTemplate setContext(Map<String, Object> contextMap) {
		this.contextMap = contextMap;
		return this;
	}
	@Override
	public ReportBuilderWithTemplate addFieldsMetadata(List<String> metadata) {
		this.fieldsMetadata = metadata;
		return this;
	}
}
