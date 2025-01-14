package pl.commit.craft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"pl.commit.craft"})
public class CommitCraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommitCraftApplication.class, args);
	}

}
