package no.hvl.dat109.texasholdem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Dat109ProsjektTexasHoldemApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Dat109ProsjektTexasHoldemApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Dat109ProsjektTexasHoldemApplication.class);
	}
}
