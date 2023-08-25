package net.gvcc.goffice.common.reporting;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.gvcc.goffice.common.reporting.exceptions.ReportEngineException;
import net.gvcc.goffice.common.reporting.objects.Developer;
import net.gvcc.goffice.common.reporting.objects.Program;
import net.gvcc.goffice.common.reporting.objects.Project;

public class ReportBuilderTest {

	@Test
	public void generateReportTest() throws IOException {
		InputStream in = this.getClass().getResourceAsStream("/template.odt");
		Project project = new Project("gOffice2 Project");
		// Register developers list
		Program p = new Program();
		p.setName("goffice");
		List<Developer> developers = new ArrayList<Developer>();
		developers.add(new Developer("Marco", "Mancuso", "marco.mancuso@herzum.com", p));
		developers.add(new Developer("Cristian", "Muraca", "cristian.muraca@herzum.com", p));

		try {
			GofficeReport report = GofficeReportXDOC.builder()
					.setTemplate(in)
					.addContextVariable("project", project)
					.addContextVariable("developers", developers)
					.build();			
			OutputStream out = new FileOutputStream(new File("reportEngineOutput.odt"));
			out.write(report.getReportBytes());

			out.close();
		} catch (ReportEngineException e) {
			fail(e);
		}

	}

	@Test
	public void generateReportPdfTest() throws IOException {
		InputStream in = this.getClass().getResourceAsStream("/template2.odt");
		Project project = new Project("gOffice2 Project");
		// Register developers list
		Program p = new Program();
		p.setName("goffice");
		List<Developer> developers = new ArrayList<Developer>();
		developers.add(new Developer("Marco", "Mancuso", "marco.mancuso@herzum.com", p));
		developers.add(new Developer("Cristian", "Muraca", "cristian.muraca@herzum.com", p));
		List<String> metadata = List.of("developers.Name", "developers.LastName", "developers.Mail","developers.Program.Name");
		try {
			GofficeReportPdf report = GofficeReportXDOC.builder()
					.setTemplate(in)
					.addContextVariable("project", project)
					.addContextVariable("developers", developers)
					.build()
					.generatePdf();
			OutputStream out = new FileOutputStream(new File("reportEngineOutput2.pdf"));
			out.write(report.getPdfBytes());
			out.close();
		} catch (ReportEngineException e) {
			fail(e);
		}
	}
	@Test
	public void generateReportPdfForDemoTest() throws IOException {
		InputStream in = this.getClass().getResourceAsStream("/templateTest.odt");
		try {
			GofficeReportPdf report = GofficeReportXDOC.builder()
					.setTemplate(in)
					.addContextVariable("nome", "Marco")
					.addContextVariable("cognome", "Mancuso")
					.addContextVariable("email", "email")
					.addContextVariable("tel", "327487245872")
					.build()
					.generatePdf();
			OutputStream out = new FileOutputStream(new File("reportEngineTest.pdf"));
			out.write(report.getPdfBytes());
			out.close();
		} catch (ReportEngineException e) {
			fail(e);
		}
	}
}
