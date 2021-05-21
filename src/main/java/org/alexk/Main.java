package org.alexk;

import lombok.SneakyThrows;
import org.alexk.presentation.javaStuff.SwingFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

	@SneakyThrows
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);

		System.setProperty("java.awt.headless", "false");
		new SwingFrame();
	}

}
