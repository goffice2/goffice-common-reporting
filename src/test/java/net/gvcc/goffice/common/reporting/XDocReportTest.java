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

public class XDocReportTest {

	@Test
	public void generateMetadataTest() throws XDocReportException, FileNotFoundException, IOException {
	    // 1) Create FieldsMetadata by setting Velocity as template engine
	    FieldsMetadata fieldsMetadata = new FieldsMetadata(TemplateEngineKind.Velocity.name());
			
	    // 2) Load fields metadata from Java Class
	    fieldsMetadata.load("project", Project.class);
	    // Here load is called with true because model is a list of Developer.
	    fieldsMetadata.load("developers", Developer.class, true);
	    
	    // 3) Generate XML fields in the file "project.fields.xml".
	    // Extension *.fields.xml is very important to use it with MS Macro XDocReport.dotm
	    // FieldsMetadata#saveXML is called with true to indent the XML.
	    File xmlFieldsFile = new File("project.fields.xml");
	    fieldsMetadata.saveXML(new FileOutputStream(xmlFieldsFile), true);
	}
	
	@Test
	public void odtVelocityTemplateTest() throws Exception {
	// 1) Load ODT file by filling Velocity template engine and cache
	// it to the registry
		
	InputStream in = this.getClass().getResourceAsStream("/template.odt");
	IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,
			TemplateEngineKind.Velocity);

	// 2) Create fields metadata to manage lazy loop (#forech velocity)
	// for table row.
	// 1) Create FieldsMetadata by setting Velocity as template engine
	FieldsMetadata fieldsMetadata = report.createFieldsMetadata();
	// 2) Load fields metadata from Java Class
	fieldsMetadata.load("project", Project.class);
	// Here load is called with true because model is a list of Developer.
	fieldsMetadata.load("developers", Developer.class, true);

	// 3) Create context Java model
	IContext context = report.createContext();
	Project project = new Project("gOffice2 Project");
	context.put("project", project);
	// Register developers list
	Program p = new Program();
	p.setName("goffice");
	List<Developer> developers = new ArrayList<Developer>();
	developers.add(new Developer("Marco", "Mancuso", "marco.mancuso@herzum.com", p));
	developers.add(new Developer("Cristian", "Muraca", "cristian.muraca@herzum.com", p));
	context.put("developers", developers);

	// 4) Generate report by merging Java model with the ODT
	OutputStream out = new FileOutputStream(new File("report.odt"));
	report.process(context, out);
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
