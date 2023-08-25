package net.gvcc.goffice.common.reporting;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import net.gvcc.goffice.common.reporting.exceptions.GofficeExportException;
import net.gvcc.goffice.common.reporting.exports.CSVExporter;
import net.gvcc.goffice.common.reporting.exports.ExcelExporter;
import net.gvcc.goffice.common.reporting.exports.GofficeExport;
import net.gvcc.goffice.common.reporting.exports.HorizontalFlattener;
import net.gvcc.goffice.common.reporting.exports.VerticalFlattener;
import net.gvcc.goffice.common.reporting.exports.XmlExporter;
import net.gvcc.goffice.common.reporting.utils.DataUtils;
import net.gvcc.goffice.common.reporting.utils.DataUtils.Dev;

public class ExporterServiceTest {

	@Test
	public void testGetXml() {
		List<Dev> dev = DataUtils.getTestDevs();
		GofficeExport tr = new GofficeExport();
		tr.addContent(dev);
		XmlExporter xmlExp = new XmlExporter();
		String result;
		try {
			result = xmlExp.exportString(tr);
			System.out.println(result);
		} catch (GofficeExportException e) {
			fail("Unable to produce XML", e);
		}
	}

	@Test
	public void testGetCsvString() {
		List<Dev> dev = DataUtils.getTestDevs();
		GofficeExport tr = new GofficeExport();
		tr.addContent(dev);
		CSVExporter csv = new CSVExporter(new HorizontalFlattener(4));
		String result;
		try {
			result = csv.exportString(tr);
			System.out.println(result);
		} catch (GofficeExportException e) {
			fail("Unable to produce XML", e);
		}
	}

	@Test
	public void testExportExcel() {
		List<Dev> dev = DataUtils.getTestDevs();
		GofficeExport tr = new GofficeExport();
		tr.addContent(dev);
		ExcelExporter csv = new ExcelExporter(new VerticalFlattener(5));
		String result;
		try {
			result = csv.exportString(tr);
			byte[] bytes = Base64.decodeBase64(result);
			FileUtils.writeByteArrayToFile(new File("target/export.xls"), bytes);
			System.out.println(result);
		} catch (GofficeExportException | IOException e) {
			fail("Unable to produce XML", e);
		}
	}

	@Test
	public void testSubsequentsExports() {
		List<Dev> dev = DataUtils.getTestDevs();
		GofficeExport tr = new GofficeExport();
		tr.addContent(dev);
		ExcelExporter xls = new ExcelExporter(new HorizontalFlattener(5));
		String result;
		try {
			result = xls.exportString(tr);
			byte[] bytes = Base64.decodeBase64(result);
			FileUtils.writeByteArrayToFile(new File("target/export.xls"), bytes);
		} catch (GofficeExportException | IOException e) {
			fail("Unable to produce XML", e);
		}
		CSVExporter csv = new CSVExporter();
		try {
			result = csv.exportString(tr);
			System.out.println(result);
		} catch (GofficeExportException e) {
			fail("Unable to produce XML", e);
		}
	}
}
