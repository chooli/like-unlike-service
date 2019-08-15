package com.jumkid.like;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LikeUnlikeServiceApplication {

//	For reactive service. Need to config dependency with the lib of spring Netty and spring webflux
//	@Bean
//	public WebClient.Builder webClientBuilder(){
//		return new WebClient.Builder();
//	}

	public static void main(String[] args) {
		SpringApplication.run(LikeUnlikeServiceApplication.class, args);
	}

}
