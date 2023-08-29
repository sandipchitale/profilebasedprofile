package sandipchitale.profilebasedprofile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

@SpringBootApplication
public class ProfileBasedProfileApplication {

	@Bean
	public CommandLineRunner clr (Environment environment, @Value("${greeting}") String greeting) {
	    return (String... args) -> {
			System.out.println("Active profiles: " + Arrays.asList(environment.getActiveProfiles()));
			System.out.println("--");
			if (environment instanceof ConfigurableEnvironment configurableEnvironment) {
				System.out.println("Property sources: ");
				configurableEnvironment.getPropertySources().stream().forEach(System.out::println);
			}
			System.out.println("--");
			System.out.println(greeting);
	    };
	}

	public static void main(String[] args) {
		SpringApplication springApplication =
				new SpringApplication(ProfileBasedProfileApplication.class);
		springApplication.addListeners((ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) -> {
			ConfigurableEnvironment environment = applicationEnvironmentPreparedEvent.getEnvironment();
			Set<String> additionalProfiles = new LinkedHashSet<>();
			if (environment.acceptsProfiles(Profiles.of("spain"))) {
				additionalProfiles.add("spanish");
			}
			if (environment.acceptsProfiles(Profiles.of("mexico"))) {
				additionalProfiles.add("spanish");
			}
			if (environment.acceptsProfiles(Profiles.of("india"))) {
				additionalProfiles.add("hindi");
			}
			ConfigDataEnvironmentPostProcessor.applyTo(environment, null, null, additionalProfiles.toArray(String[]::new));
		});
		springApplication.run(args);
	}

}
