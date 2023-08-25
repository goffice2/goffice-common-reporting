package net.gvcc.goffice.common.reporting.utils;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

public class DataUtils {
	@Data
	@AllArgsConstructor
	public static class Dev {
		private String name;
		private Integer age = 10;
		private String email = "email";
		private Task task = new Task("embedded", 2.0f, null);
		private List<Task> tasks = new ArrayList<>();
	}

	@Data
	@AllArgsConstructor
	public static class Task {
		private String name;
		private Float duration;
		private List<Task> tasks = new ArrayList<>();

	}

	public static Dev getTestDev(Integer i) {
		List<Task> tasks = new ArrayList<>();
		List<Task> stasks1 = new ArrayList<>();
		List<Task> sstasks1 = new ArrayList<>();
		Task sst11 = new Task(i + "sst11", 1.0f, null);
		sstasks1.add(sst11);
		Task st11 = new Task(i + "st11", 1.0f, sstasks1);
		Task st12 = new Task(i + "st12", 1.0f, null);
		stasks1.add(st11);
		stasks1.add(st12);
		List<Task> stasks2 = new ArrayList<>();
		Task st21 = new Task(i + "st21", 1.0f, null);
		Task st22 = new Task(i + "st22", 1.0f, null);
		stasks2.add(st21);
		stasks2.add(st22);

		Task t1 = new Task(i + "t1", 1.0f, stasks1);
		Task t2 = new Task(i + "t2", 1.0f, stasks2);
		Task embedded = new Task(i + "embedded", 1.0f, null);

		tasks.add(t1);
		tasks.add(t2);

		return new Dev(i + "dave", 10, i + "email", embedded, tasks);
	}

	public static List<Dev> getTestDevs() {
		List<Dev> devs = new ArrayList<>();
		devs.add(getTestDev(1));
		devs.add(getTestDev(2));
		devs.add(getTestDev(3));
		return devs;
	}
}
