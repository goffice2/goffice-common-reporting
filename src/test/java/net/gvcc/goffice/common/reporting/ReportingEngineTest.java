package net.gvcc.goffice.common.reporting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.event.implement.ReportInvalidReferences;
import org.junit.jupiter.api.Test;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import fr.opensagres.odfdom.converter.pdf.PdfConverter;
import fr.opensagres.odfdom.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import freemarker.template.Configuration;
import freemarker.template.Template;
import net.gvcc.goffice.common.reporting.objects.Developer;
import net.gvcc.goffice.common.reporting.objects.Program;
import net.gvcc.goffice.common.reporting.objects.Project;

public class ReportingEngineTest {

	@Test
	public void reportingEngineTest() throws XDocReportException, FileNotFoundException, IOException {
		ReportingEngine reporting = new ReportingEngine();
		InputStream in = this.getClass().getResourceAsStream("/template.odt");
		Map<String, Object> context = new HashMap<>();
		Project project = new Project("gOffice2 Project");
		context.put("project", project);
		// Register developers list
		Program p = new Program();
		p.setName("goffice");
		List<Developer> developers = new ArrayList<Developer>();
		developers.add(new Developer("Marco", "Mancuso", "marco.mancuso@herzum.com", p));
		developers.add(new Developer("Cristian", "Muraca", "cristian.muraca@herzum.com", p));
		context.put("developers", developers);
		List<String> metadata = List.of("developers.Name", "developers.LastName", "developers.Mail","developers.Program.Name");
		byte[] result = reporting.processTemplate(context, in,metadata);
		OutputStream out = new FileOutputStream(new File("reportEngineOutput.odt"));
		out.write(result);
		out.close();
	}

	@Test
	public void odt2PdfTest() throws Exception {
		// 1) Load ODT into ODFDOM OdfTextDocument
		InputStream in = this.getClass().getResourceAsStream("/report.odt");
		OdfTextDocument document = OdfTextDocument.loadDocument(in);
		// 2) Prepare Pdf options
		PdfOptions options = PdfOptions.create();
		// 3) Convert OdfTextDocument to PDF via IText
		OutputStream out = new FileOutputStream(new File("report.pdf"));
		PdfConverter.getInstance().convert(document, out, options);
	}

}
