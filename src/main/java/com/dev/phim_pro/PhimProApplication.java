package com.dev.phim_pro;

import com.dev.phim_pro.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class PhimProApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhimProApplication.class, args);
	}

}
