package net.gvcc.goffice.common.reporting;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import net.gvcc.goffice.common.reporting.exceptions.ReportEngineException;

public class GofficeComplexReport {

	private static Logger LOGGER = LoggerFactory.getLogger(GofficeComplexReport.class);

	Map<InputStream, Map<Integer, Integer>> templateMapping = new HashMap<>();
	Map<Integer, Map<Integer, GofficeReportPdf>> pageMapping = new HashMap<>();

	public GofficeComplexReport addPage(InputStream template, int sourcePage, int destinationPage) {
		LOGGER.debug("addPage - START");

		Map<Integer, Integer> sourceDestinationMapping = templateMapping.get(template);
		if (sourceDestinationMapping == null) {
			sourceDestinationMapping = new HashMap<Integer, Integer>();
			templateMapping.put(template, sourceDestinationMapping);
		}
		sourceDestinationMapping.put(sourcePage, destinationPage);

		LOGGER.debug("addPage - END");

		return this;
	}

	public GofficeComplexReport builder() {
		return new GofficeComplexReport();
	}

	public GofficeComplexReport generateReports(Map<String, Object> contextMap) throws ReportEngineException {
		LOGGER.debug("generateReports - START");

		for (var template : templateMapping.entrySet()) {
			GofficeReportPdf report;
			report = GofficeReportXDOC.builder().setTemplate(template.getKey()).setContext(contextMap).build().generatePdf();
			templateMapping.get(template.getKey()).forEach((source, destination) -> {
				Map<Integer, GofficeReportPdf> map = pageMapping.get(destination);
				if (map == null) {
					map = new HashMap<>();
				}
				map.put(source, report);
			});
		}

		LOGGER.debug("generateReports - END");

		return this;
	}

	public GofficeComplexReport createPdf(OutputStream outputStream, boolean paginate) {
		LOGGER.debug("createPdf - START");

		Document document = new Document();
		try {
			var pages = pageMapping.keySet();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF

			Map<Integer, PdfReader> pdfReaders = new HashMap<>();
			List<Integer> pagesSorted = new ArrayList<>(pages);
			Collections.sort(pagesSorted);
			var totalPages = pagesSorted.size();
			for (var destinationPageNum : pagesSorted) {
				Map<Integer, GofficeReportPdf> map = pageMapping.get(destinationPageNum);
				document.newPage();
				for (var entry : map.entrySet()) {
					var sourcePageNum = entry.getKey();
					var stream = new ByteArrayInputStream(entry.getValue().getPdfBytes());
					PdfReader pdfReader = pdfReaders.get(destinationPageNum);
					if (pdfReader == null) {
						pdfReader = new PdfReader(stream);
						pdfReaders.put(destinationPageNum, pdfReader);
					}
					PdfImportedPage page = writer.getImportedPage(pdfReader, sourcePageNum);
					cb.addTemplate(page, 0, 0);
					if (paginate) {
						cb.beginText();
						cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + destinationPageNum + " of " + totalPages, 520, 5, 0);
						cb.endText();
					}
				}
			}
			pdfReaders.values().forEach(PdfReader::close);
			outputStream.flush();

			// the following line is present in the finally block
			// document.close();

			// The responsible who has to close the stream is the caller
			// because he has initialized the variable outputStream
			// outputStream.close();
		} catch (Exception e) {
			LOGGER.info("createPdf", e);
		} finally {
			if (document.isOpen()) {
				document.close();
			}

			// The responsible who has to close the stream is the caller because he has initialized it
			// try {
			// if (outputStream != null)
			// outputStream.close();
			// } catch (IOException ioe) {
			// ioe.printStackTrace();
			// }
		}

		cleanup();

		LOGGER.debug("createPdf - END");

		return this;
	}

	private void cleanup() {
		// TODO Auto-generated method stub
	}
}
