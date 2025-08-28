package com.niti.vision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class NitiVisionApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(NitiVisionApplication.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(NitiVisionApplication.class);
		
	}
	/*
	 * @Bean CommandLineRunner init(UserRepository repo) { return args -> { if
	 * (repo.findByEmail("test@niti.com").isEmpty()) { User user = new User();
	 * user.setEmail("test@niti.com"); user.setPassword(new
	 * BCryptPasswordEncoder().encode("secret")); repo.save(user);
	 * System.out.println("Test user created: test@niti.com / secret"); } }; }
	 */
}
