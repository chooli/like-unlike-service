package com.jumkid.like.graphql;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {

    @Value("${graphql.schema.file}")
    private String schemaFile;

    private GraphQLSchema graphQLSchema;

    private GraphQL graphQL;

    private final LikeDataFetcher likeDataFetcher;
    private final LikeMutationDataFetcher likeMutationDataFetcher;

    @Autowired
    public GraphQLProvider(LikeDataFetcher likeDataFetcher, LikeMutationDataFetcher likeMutationDataFetcher) {
        this.likeDataFetcher = likeDataFetcher;
        this.likeMutationDataFetcher = likeMutationDataFetcher;
    }

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource(schemaFile);
        String sdl = Resources.toString(url, Charsets.UTF_8);
        this.graphQLSchema = buildSchema(sdl);
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("getLike", likeDataFetcher.getLike()))
                .type(newTypeWiring("Mutation")
                        .dataFetcher("addLike", likeMutationDataFetcher.addLike())
                        .dataFetcher("subtractLike", likeMutationDataFetcher.subtractLike())
                        .dataFetcher("removeLike", likeMutationDataFetcher.removeLike()))
                .build();
    }

    public GraphQLSchema getGraphQLSchema() {
        return graphQLSchema;
    }

    @Bean
    public GraphQL graphQL() {
        if (graphQL == null) {
            graphQL = GraphQL.newGraphQL(graphQLSchema).build();
        }
        return graphQL;
    }

}
