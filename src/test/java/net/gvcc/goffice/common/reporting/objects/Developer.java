package net.gvcc.goffice.common.reporting.objects;

public class Developer {
	private final String name;
	private final String lastName;
	private final String mail;
	private final Program program;

	public Program getProgram() {
		return program;
	}

	public Developer(String name, String lastName, String mail, Program program) {
		this.name = name;
		this.lastName = lastName;
		this.mail = mail;
		this.program = program;
	}

	public String getName() {
		return name;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMail() {
		return mail;
	}
}
