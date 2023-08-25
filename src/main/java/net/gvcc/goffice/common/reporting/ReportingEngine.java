package net.gvcc.goffice.common.reporting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.opensagres.odfdom.converter.pdf.PdfConverter;
import fr.opensagres.odfdom.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 *
 * <p>
 * The <code>ReportingEngine</code> class
 * </p>
 * <p>
 * Classe che si occupa della vera e propria elaborazione del report e della produzione dei file PDF. E’ l’unica classe che interagisce e astrae l’utilizzo delle librerie opensource utilizzate
 * </p>
 * 
 * @author marco mancuso
 * @version 1.0
 * 
 * @see "Disegno Architetturale Moduli Core: Modulo di reportistica dinamica"
 */
public class ReportingEngine {
	private static Logger LOGGER = LoggerFactory.getLogger(ReportingEngine.class);

	private TemplateEngineKind templateEngine = TemplateEngineKind.Velocity;

	public ReportingEngine(TemplateEngineKind templateEngine) {
		this.templateEngine = templateEngine;
	}

	public ReportingEngine() {
	}

	public byte[] processTemplate(Map<String, Object> contextMap, InputStream templateIS, List<String> metadata) throws IOException, XDocReportException {
		LOGGER.debug("processTemplate - START");

		IXDocReport report = XDocReportRegistry.getRegistry().loadReport(templateIS, templateEngine);
		FieldsMetadata fieldsMetadata = report.createFieldsMetadata();
		IContext context = report.createContext();
		for (Entry<String, Object> entry : contextMap.entrySet()) {
			if (entry.getValue() instanceof Collection<?>) {
				// The generic type is not avalilable at runtime this is why we suppress
				@SuppressWarnings({ "unchecked", "rawtypes" })
				List list = new ArrayList((Collection) entry.getValue());
				@SuppressWarnings("rawtypes")
				Class classz = (list.get(0) != null) ? list.get(0).getClass() : Object.class;
				fieldsMetadata.load(entry.getKey(), classz, true);
			} else {
				fieldsMetadata.load(entry.getKey(), entry.getValue().getClass());
			}

			context.put(entry.getKey(), entry.getValue());
		}
		
		if (metadata != null) {
			metadata.forEach(field -> {
				fieldsMetadata.addFieldAsList(field);
			});
		}
		report.setFieldsMetadata(fieldsMetadata);

		byte[] result = null;
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			report.process(context, out);
			result = out.toByteArray();
		}

		// The responsible who has to close the stream is the caller because he has initialized it
		// templateIS.close();

		LOGGER.debug("processTemplate - END");

		return result;
	}

	public byte[] generateReport(GofficeReportXDOC gReport) throws IOException, XDocReportException {
		return processTemplate(gReport.getContextMap(), gReport.getTemplate(), gReport.getFieldsMetadata());
	}

	public byte[] generatePdf(GofficeReportXDOC gReport) throws Exception {
		LOGGER.debug("generatePdf - START");

		byte[] result = null;

		try (ByteArrayInputStream in = new ByteArrayInputStream(gReport.getReportBytes()); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			OdfTextDocument document = OdfTextDocument.loadDocument(in);
			// 2) Prepare Pdf options
			PdfOptions options = PdfOptions.create();
			// 3) Convert OdfTextDocument to PDF via IText
			PdfConverter.getInstance().convert(document, out, options);
			result = out.toByteArray();
		}

		LOGGER.debug("generatePdf - END");
		
		return result;
	}

	public byte[] processTemplateWithoutFieldsMetadata(Map<String, Object> contextMap, InputStream templateIS) throws IOException, XDocReportException {
		LOGGER.debug("processTemplateWithoutFieldsMetadata - START");

		byte[] result = null;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(templateIS, TemplateEngineKind.Velocity);
			report.process(contextMap, out);
			result = out.toByteArray();
		}

		// The responsible who has to close the stream is the caller
		// because he has initialized it templateIS.close();

		LOGGER.debug("processTemplateWithoutFieldsMetadata - END");

		return result;
	}

}
