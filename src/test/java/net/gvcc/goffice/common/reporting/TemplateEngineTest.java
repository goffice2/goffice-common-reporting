package net.gvcc.goffice.common.reporting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateEngineTest {
	
	@Test
	public void loadODTDocument() {
		
	}
	
	@Test
	public void testFreemarkerTemplate() {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
		configuration.setClassForTemplateLoading(this.getClass(), "/");
		try {
			Template template = configuration.getTemplate("test.ftl");
			StringWriter writer = new StringWriter();
			Map<String, Object> map = new HashMap<>();
			map.put("name", "Marco");
 			template.process(map, writer);
 			String result = writer.toString();
 			assertEquals("Hello Marco", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
