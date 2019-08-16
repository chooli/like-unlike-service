package com.jumkid.like;

import com.jumkid.like.graphql.GraphQLProvider;
import graphql.servlet.SimpleGraphQLHttpServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

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

	private GraphQLProvider graphQLProvider;

	@Autowired
	public LikeUnlikeServiceApplication(GraphQLProvider graphQLProvider){
		this.graphQLProvider = graphQLProvider;
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean<>(SimpleGraphQLHttpServlet.newBuilder(graphQLProvider.getGraphQLSchema()).build(), "/graphql");
	}

}
