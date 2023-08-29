package sandipchitale.profilebasedprofile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.Arrays;

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
		ProfilesForProfile profilesForProfile = new ProfilesForProfile();
		profilesForProfile.profileForProfile("india", "hindi");
		profilesForProfile.profileForProfile("spain", "spanish");
		profilesForProfile.profileForProfile("mexico", "spanish");
		springApplication.addListeners(profilesForProfile);
		springApplication.run(args);
	}

}
