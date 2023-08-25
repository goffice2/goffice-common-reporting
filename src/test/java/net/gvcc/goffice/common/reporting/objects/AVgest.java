package net.gvcc.goffice.common.reporting.objects;

public class AVgest {

	private String desc1;
	private String desc2;
	private String importo;

	public AVgest(String desc1, String desc2, String importo) {
		this.desc1 = desc1;
		this.desc2 = desc2;
		this.importo = importo;
	}

	/**
	 * @return the desc
	 */
	public String getDesc1() {
		return desc1;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	/**
	 * @return the desc2
	 */
	public String getDesc2() {
		return desc2;
	}

	/**
	 * @param desc2
	 *            the desc2 to set
	 */
	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}

	/**
	 * @return the importo
	 */
	public String getImporto() {
		return importo;
	}

	/**
	 * @param importo
	 *            the importo to set
	 */
	public void setImporto(String importo) {
		this.importo = importo;
	}

}
