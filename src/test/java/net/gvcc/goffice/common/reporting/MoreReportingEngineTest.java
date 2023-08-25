package net.gvcc.goffice.common.reporting;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

import net.gvcc.goffice.common.reporting.exceptions.ReportEngineException;
import net.gvcc.goffice.common.reporting.objects.Developer;
import net.gvcc.goffice.common.reporting.objects.Ente;
import net.gvcc.goffice.common.reporting.objects.Program;
import net.gvcc.goffice.common.reporting.objects.Project;
import net.gvcc.goffice.common.reporting.utils.Report;

public class MoreReportingEngineTest {

	@Test
	public void whengenerateTextdocumentReport() throws IOException {
		InputStream in = this.getClass().getResourceAsStream("/example1.odt");
		Project project = new Project("gOffice2 Project");
		// Register developers list
		Program p = new Program();
		p.setName("goffice");
		List<Developer> developers = new ArrayList<Developer>();
		developers.add(new Developer("Marco", "Mancuso", "marco.mancuso@herzum.com", p));
		developers.add(new Developer("Cristian", "Muraca", "cristian.muraca@herzum.com", p));

		try {
			GofficeReport report = GofficeReportXDOC.builder().setTemplate(in).addContextVariable("project", project).addContextVariable("developers", developers).build();
			OutputStream out = new FileOutputStream(new File("reportExample1.odt"));
			out.write(report.getReportBytes());

			out.close();
		} catch (ReportEngineException e) {
			fail(e);
		}

	}

	@Test
	public void whengeneratePdfReport() throws IOException {
		InputStream in = this.getClass().getResourceAsStream("/example1.odt");
		Project project = new Project("gOffice2 Project");
		// Register developers list
		Program p = new Program();
		p.setName("goffice");
		List<Developer> developers = new ArrayList<Developer>();
		developers.add(new Developer("Marco", "Mancuso", "marco.mancuso@herzum.com", p));
		developers.add(new Developer("Cristian", "Muraca", "cristian.muraca@herzum.com", p));

		try {
			GofficeReportPdf report = GofficeReportXDOC.builder().setTemplate(in).addContextVariable("project", project).addContextVariable("developers", developers).build().generatePdf();
			OutputStream out = new FileOutputStream(new File("reportexample1.pdf"));
			out.write(report.getPdfBytes());
			out.close();
		} catch (ReportEngineException e) {
			fail(e);
		}
	}

	@Test
	public void whengenerateTextdocumentDetailsReport() throws IOException {
		InputStream in = this.getClass().getResourceAsStream("/details.odt");
		Project project = new Project("gOffice2 Project");
		Map<String, Object> map = new HashMap<>();
		map.put("project", project);

		String queryNames[] = new String[] { "developers" };
		List<?> queryList = new ArrayList<>(Arrays.asList(queryNames));
		for (Object obj : queryList) {
			String queryName = obj.toString();
			String queryDataFile = "/" + queryName + ".json";
			InputStream queryData = this.getClass().getResourceAsStream(queryDataFile);
			BufferedReader bR = new BufferedReader(new InputStreamReader(queryData));
			String line = "";
			StringBuilder responseStrBuilder = new StringBuilder();

			while ((line = bR.readLine()) != null) {
				responseStrBuilder.append(line);
			}
			queryData.close();
			final String json = responseStrBuilder.toString();
			System.out.println("datamap json: " + json);
			final ObjectMapper mapper = new ObjectMapper();
			final MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
			final Map<String, Object> data = mapper.readValue(json, type);
			@SuppressWarnings("unchecked")
			List<Map<String, String>> list = (List<Map<String, String>>) data.get(queryName);
			System.out.println("datamap list: " + list);
			map.put(queryName, list);
		}

		try {
			GofficeReport report = GofficeReportXDOC.builder().setTemplate(in).setContext(map).build();
			OutputStream out = new FileOutputStream(new File("reportDetails.odt"));
			out.write(report.getReportBytes());
			out.close();
		} catch (ReportEngineException e) {
			fail(e);
		}
	}

	@Test
	public void whengeneratePDFDetailsReport() throws IOException {
		InputStream in = this.getClass().getResourceAsStream("/details.odt");
		Project project = new Project("gOffice2 Project");
		Map<String, Object> map = new HashMap<>();
		map.put("project", project);

		String queryNames[] = new String[] { "developers" };
		List<?> queryList = new ArrayList<>(Arrays.asList(queryNames));
		for (Object obj : queryList) {
			String queryName = obj.toString();
			String queryDataFile = "/" + queryName + ".json";
			InputStream queryData = this.getClass().getResourceAsStream(queryDataFile);
			BufferedReader bR = new BufferedReader(new InputStreamReader(queryData));
			String line = "";
			StringBuilder responseStrBuilder = new StringBuilder();

			while ((line = bR.readLine()) != null) {
				responseStrBuilder.append(line);
			}
			queryData.close();
			final String json = responseStrBuilder.toString();
			System.out.println("datamap json: " + json);
			final ObjectMapper mapper = new ObjectMapper();
			final MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
			final Map<String, Object> data = mapper.readValue(json, type);
			@SuppressWarnings("unchecked")
			List<Map<String, String>> list = (List<Map<String, String>>) data.get(queryName);
			System.out.println("datamap list: " + list);
			map.put(queryName, list);
		}

		try {
			GofficeReportPdf report = GofficeReportXDOC.builder().setTemplate(in).setContext(map).build().generatePdf();
			OutputStream out = new FileOutputStream(new File("reportDetails.pdf"));
			out.write(report.getPdfBytes());
			out.close();
		} catch (ReportEngineException e) {
			fail(e);
		}
	}

	@Test
	public void whengenerateRHVTextdocumentReport() throws IOException {
		InputStream in = this.getClass().getResourceAsStream("/RevisionRHVBericht.odt");
		Ente ente = new Ente();
		ente.setNameDE("Aldein");
		ente.setNameIT("Aldino");
		Report report = new Report();
		report.setYearFrom("2023");
		report.setYearTo("2025");
		report.setReportYear("2023");
		report.setReportYearPlus1("2024");
		report.setReportYearPlus2("2025");
		report.setReportYearMin1("2022");
		report.setReportYearMin2("2021");
		report.setReportYearMin3("2020");
		//
		Map<String, Object> map = new HashMap<>();
		map.put("ente", ente);
		map.put("report", report);

		List<String> queryList = getQueryList("/querylist.txt");
		for (String queryName : queryList) {
			System.out.println(queryName);
			String queryDataFile = "/RHVData/" + queryName + ".json";
			InputStream queryData = this.getClass().getResourceAsStream(queryDataFile);
			BufferedReader bR = new BufferedReader(new InputStreamReader(queryData));
			String line = "";
			StringBuilder responseStrBuilder = new StringBuilder();
			while ((line = bR.readLine()) != null) {
				responseStrBuilder.append(line);
			}
			queryData.close();
			final String json = responseStrBuilder.toString();
			System.out.println("datamap json: " + json);
			final ObjectMapper mapper = new ObjectMapper();
			final MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
			final Map<String, Object> data = mapper.readValue(json, type);
			@SuppressWarnings("unchecked")
			List<Map<String, String>> list = (List<Map<String, String>>) data.get(queryName);
			System.out.println("datamap list: " + list);
			map.put(queryName, list);
		}

		try {
			GofficeReport outreport = GofficeReportXDOC.builder().setTemplate(in).setContext(map).build();
			OutputStream out = new FileOutputStream(new File("reportRevisionRHVBericht.odt"));
			out.write(outreport.getReportBytes());
			out.close();
		} catch (ReportEngineException e) {
			fail(e);
		}
	}

	@Test
	public void whengenerateRHVPdfReport() throws IOException {
		InputStream in = this.getClass().getResourceAsStream("/RevisionRHVBericht.odt");
		Ente ente = new Ente();
		ente.setNameDE("Aldein");
		ente.setNameIT("Aldino");
		Report report = new Report();
		report.setYearFrom("2023");
		report.setYearTo("2025");
		report.setReportYear("2023");
		report.setReportYearPlus1("2024");
		report.setReportYearPlus2("2025");
		report.setReportYearMin1("2022");
		report.setReportYearMin2("2021");
		report.setReportYearMin3("2020");
		//
		Map<String, Object> map = new HashMap<>();
		map.put("ente", ente);
		map.put("report", report);
		//
		List<String> queryList = getQueryList("/querylist.txt");
		for (String queryName : queryList) {
			String queryDataFile = "/RHVData/" + queryName + ".json";
			InputStream queryData = this.getClass().getResourceAsStream(queryDataFile);
			BufferedReader bR = new BufferedReader(new InputStreamReader(queryData));
			String line = "";
			StringBuilder responseStrBuilder = new StringBuilder();
			while ((line = bR.readLine()) != null) {
				responseStrBuilder.append(line);
			}
			queryData.close();
			final String json = responseStrBuilder.toString();
			System.out.println("datamap json: " + json);
			final ObjectMapper mapper = new ObjectMapper();
			final MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
			final Map<String, Object> data = mapper.readValue(json, type);
			@SuppressWarnings("unchecked")
			List<Map<String, String>> list = (List<Map<String, String>>) data.get(queryName);
			System.out.println("datamap list: " + list);
			map.put(queryName, list);
		}

		try {
			GofficeReportPdf outreport = GofficeReportXDOC.builder().setTemplate(in).setContext(map).build().generatePdf();
			OutputStream out = new FileOutputStream(new File("reportRevisionRHVBericht.pdf"));
			out.write(outreport.getPdfBytes());
			out.close();
		} catch (ReportEngineException e) {
			fail(e);
		}
	}

	@Test
	public void whengenerateDeliberaTextdocumentReport() throws IOException {
		// Import model
		InputStream in = this.getClass().getResourceAsStream("/modello_delibera.odt");
		// Import data
		String queryDataFile = "/" + "modello_delibera_data" + ".json";
		InputStream queryData = this.getClass().getResourceAsStream(queryDataFile);
		BufferedReader bR = new BufferedReader(new InputStreamReader(queryData));
		String line = "";
		StringBuilder responseStrBuilder = new StringBuilder();
		while ((line = bR.readLine()) != null) {
			responseStrBuilder.append(line);
		}
		queryData.close();
		final String json = responseStrBuilder.toString();
		//
		Map<String, Object> map = new HashMap<>();
		String queryNames[] = new String[] { "GenericData", "Members", "Verpflichtungsdaten" };
		List<?> queryList = new ArrayList<>(Arrays.asList(queryNames));
		for (Object obj : queryList) {
			String queryName = obj.toString();
			final ObjectMapper mapper = new ObjectMapper();
			final MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
			final Map<String, Object> data = mapper.readValue(json, type);
			if (data.get(queryName) instanceof List<?>) {
				@SuppressWarnings("unchecked")
				List<Map<String, String>> list = (List<Map<String, String>>) data.get(queryName);
				map.put(queryName, list);
			} else {
				@SuppressWarnings("unchecked")
				Map<String, Object> submap = (Map<String, Object>) data.get(queryName);
				map.put(queryName, submap);
			}
		}
		System.out.println("datamap list: " + map);
		try {
			GofficeReport outreport = GofficeReportXDOC.builder().setTemplate(in).setContext(map).build();
			OutputStream out = new FileOutputStream(new File("reportModelloDelibera.odt"));
			out.write(outreport.getReportBytes());
			out.close();
		} catch (ReportEngineException e) {
			fail(e);
		}
	}

	@Test
	public void whengenerateDeliberaPDFReport() throws IOException {
		// Import model
		InputStream in = this.getClass().getResourceAsStream("/modello_delibera.odt");
		// Import data
		String queryDataFile = "/" + "modello_delibera_data" + ".json";
		InputStream queryData = this.getClass().getResourceAsStream(queryDataFile);
		BufferedReader bR = new BufferedReader(new InputStreamReader(queryData));
		String line = "";
		StringBuilder responseStrBuilder = new StringBuilder();
		while ((line = bR.readLine()) != null) {
			responseStrBuilder.append(line);
		}
		queryData.close();
		final String json = responseStrBuilder.toString();
		//
		Map<String, Object> map = new HashMap<>();
		String queryNames[] = new String[] { "GenericData", "Members", "Verpflichtungsdaten" };
		List<?> queryList = new ArrayList<>(Arrays.asList(queryNames));
		for (Object obj : queryList) {
			String queryName = obj.toString();
			final ObjectMapper mapper = new ObjectMapper();
			final MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
			final Map<String, Object> data = mapper.readValue(json, type);
			if (data.get(queryName) instanceof List<?>) {
				@SuppressWarnings("unchecked")
				List<Map<String, String>> list = (List<Map<String, String>>) data.get(queryName);
				map.put(queryName, list);
			} else {
				@SuppressWarnings("unchecked")
				Map<String, Object> submap = (Map<String, Object>) data.get(queryName);
				map.put(queryName, submap);
			}
		}
		System.out.println("datamap list: " + map);
		try {
			GofficeReportPdf outreport = GofficeReportXDOC.builder().setTemplate(in).setContext(map).build().generatePdf();
			OutputStream out = new FileOutputStream(new File("ModelloDelibera.pdf"));
			out.write(outreport.getPdfBytes());
			out.close();
		} catch (ReportEngineException e) {
			fail(e);
		}
	}

	private List<String> getQueryList(String filename) {
		List<String> list = new ArrayList<String>();
		InputStream queryData = this.getClass().getResourceAsStream(filename);
		BufferedReader bR = new BufferedReader(new InputStreamReader(queryData));
		String line = "";
		// StringBuilder responseStrBuilder = new StringBuilder();
		try {
			while ((line = bR.readLine()) != null) {
				// responseStrBuilder.append(line);
				list.add(line);
			}
			queryData.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

}
