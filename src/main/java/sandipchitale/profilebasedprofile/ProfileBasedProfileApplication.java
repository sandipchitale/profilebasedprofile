package sandipchitale.profilebasedprofile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
@ConfigurationPropertiesScan // Needed because Account is a java record.
public class ProfileBasedProfileApplication {

	// Show that late added profiles work for @ConfigurationProperties also
	@ConfigurationProperties(prefix = "account")
	public record Account (String name, String password) { }

	@Bean
	public CommandLineRunner clr (Environment environment, @Value("${greeting}") String greeting, Account account) {
	    return (String... args) -> {
			System.out.println("Active profiles: " + Arrays.asList(environment.getActiveProfiles()));
			System.out.println("--");
			if (environment instanceof ConfigurableEnvironment configurableEnvironment) {
				System.out.println("Property sources: ");
				configurableEnvironment.getPropertySources().stream().forEach(System.out::println);
			}
			System.out.println("--");
			System.out.println(greeting);

			System.out.println(account);
		};
	}

	public static void main(String[] args) {
		SpringApplication springApplication =
				new SpringApplication(ProfileBasedProfileApplication.class);
		ProfilesForProfile profilesForProfile = new ProfilesForProfile();
		profilesForProfile.profileForProfile("india", "hindi");
		profilesForProfile.profileForProfiles("spanish", "spain", "mexico");
		springApplication.addListeners(profilesForProfile);
		springApplication.run(args);
	}

}
