package net.gvcc.goffice.common.reporting.exports;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.gvcc.goffice.common.reporting.exceptions.GofficeExportException;
/**
 *
 * <p>
 * The <code>ExcelExporter</code> class
 * </p>
 * <p>
 * Classe che permette l’esportazione di un {@link net.gvcc.goffice.common.reporting.exports.GofficeExport} in formato XLS. La classe permette di ottenere la rappresentazione del suddetto XLS nei
 * seguenti formati:
 * <ul>
 * <li>{@link String}</li>
 * <li>{@link String} Base64</li>
 * <li>{@link byte[]}</li>
 * </ul>
 * 
 * 
 * @author marco mancuso
 * @version 1.0
 * 
 * @see "Disegno Architetturale Moduli Core: Sistema di esposizione dati a software di aziende terze"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelExporter implements Exporter {
	private static Logger LOGGER = LoggerFactory.getLogger(ExcelExporter.class);

	Flattener flattener = new VerticalFlattener();

	@Override
	public byte[] exportBytes(GofficeExport export) throws GofficeExportException {
		LOGGER.debug("exportBytes - START");

		byte[] buffer = null;

		try (Workbook wb = new HSSFWorkbook()) {
			export.flatten(flattener, flattener.getLevel());
			List<Map<String, String>> map = export.getFlatMapString();
			String sheetName = "report";
			String safeSheetName = WorkbookUtil.createSafeSheetName(sheetName);
			Sheet sheet = wb.createSheet(safeSheetName);
			CreationHelper createHelper = wb.getCreationHelper();
			// Create a row and put some cells in it. Rows are 0 based.
			int rowCount = 0;
			for (Map<String, String> obj : map) {
				if (rowCount == 0) {
					int headerCount = 0;
					Row row = sheet.createRow(rowCount++);
					for (String prop : obj.keySet()) {
						row.createCell(headerCount++).setCellValue(prop);
					}
				}

				// Create header
				Row row = sheet.createRow(rowCount++);
				int valueCount = 0;
				for (String prop : obj.keySet()) {
					row.createCell(valueCount++).setCellValue(obj.get(prop));
				}
			}

			try (ByteArrayOutputStream fileOut = new ByteArrayOutputStream()) {
				wb.write(fileOut);
				buffer = fileOut.toByteArray();
			} catch (FileNotFoundException e) {
				throw new GofficeExportException("Unable to create EXCEL", e);
			} catch (IOException e) {
				throw new GofficeExportException("Unable to create EXCEL", e);
			}
		} catch (IOException e) {
			throw new GofficeExportException("Unable to close workbook", e);
		}

		LOGGER.debug("exportBytes - END");

		return buffer;
	}

	@Override
	public String exportBase64(GofficeExport export) throws GofficeExportException {
		try {
			return Base64.encodeBase64String(exportBytes(export));
		} catch (GofficeExportException e) {
			throw new GofficeExportException("Unable to create EXCEL", e);
		}
	}

	@Override
	public String exportString(GofficeExport export) throws GofficeExportException {
		return exportBase64(export);
	}
}
