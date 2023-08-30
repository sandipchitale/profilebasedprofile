package sandipchitale.profilebasedprofile;

import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Profiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This allows addition of dependent profile using a multi value map.
 * Add dependent profiles of a profile to an instance of this class and then
 * add it as listener to a SpringApplication before running it.
 */
public class ProfilesForProfile implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private final MultiValueMap<String, String> profilesForProfileMap;

    public ProfilesForProfile() {
        this(null);
    }

    public ProfilesForProfile(MultiValueMap<String, String> profilesForProfile) {
        this.profilesForProfileMap = new LinkedMultiValueMap<>();
        if (profilesForProfile != null) {
            this.profilesForProfileMap.addAll(profilesForProfile);
        }
    }

    // profile -> dependent profiles
    public void profileForProfile(String profile, String dependentProfile) {
        this.profilesForProfileMap.add(profile, dependentProfile);
    }

    public void profilesForProfile(String profile, String... dependentProfiles) {
        this.profilesForProfileMap.addAll(profile, Arrays.asList(dependentProfiles));
    }

    public void profilesForProfile(String profile, List<String> dependentProfiles) {
        this.profilesForProfileMap.addAll(profile, dependentProfiles);
    }

    public void profilesForProfile(MultiValueMap<String, String> profilesForProfile) {
        this.profilesForProfileMap.addAll(profilesForProfile);
    }

    // dependent profile -> dependee profiles
    public void profileForProfiles(String dependentProfile, String... dependeeProfiles) {
        Arrays.asList(dependeeProfiles).stream().forEach((String profile) -> {
            this.profilesForProfileMap.add(profile, dependentProfile);
        });
    }

    public void profileForProfiles(String dependentProfile, List<String> dependeeProfiles) {
        dependeeProfiles.stream().forEach((String profile) -> {
            this.profilesForProfileMap.add(profile, dependentProfile);
        });
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        ConfigurableEnvironment environment = applicationEnvironmentPreparedEvent.getEnvironment();
        Set<String> additionalProfiles = new LinkedHashSet<>();
        profilesForProfileMap.forEach((profile, dependentProfiles) -> {
            if (environment.acceptsProfiles(Profiles.of(profile))) {
                additionalProfiles.addAll(dependentProfiles);
            }
        });
        ConfigDataEnvironmentPostProcessor.applyTo(environment, null, null, additionalProfiles.toArray(String[]::new));
    }
}