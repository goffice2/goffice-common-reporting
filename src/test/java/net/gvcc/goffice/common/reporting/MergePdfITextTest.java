package net.gvcc.goffice.common.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.gvcc.goffice.common.reporting.utils.MergePDF;

/**
 * @author marco.mancuso
 *
 */
public class MergePdfITextTest {
	/**
	 * 
	 */
	@Test
	public void test() {
	    try {
		      List<InputStream> pdfs = new ArrayList<InputStream>();
		      InputStream pdf1 = this.getClass().getResourceAsStream("/reportEngineOutput.pdf");
		      InputStream pdf2 = this.getClass().getResourceAsStream("/reportEngineOutput2.pdf");
		      pdfs.add(pdf1);
		      pdfs.add(pdf2);
			  OutputStream output = new FileOutputStream(new File("reportEngineOutputMerged.pdf"));
		      MergePDF.concatPDFs(pdfs, output, true);
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	}
}
