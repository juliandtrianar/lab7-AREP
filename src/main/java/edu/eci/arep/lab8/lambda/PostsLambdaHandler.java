package edu.eci.arep.lab8.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.arep.lab8.model.Post;
import edu.eci.arep.lab8.service.PostService;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class PostsLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final PostService postService = new PostService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        try {
            String httpMethod = event.getHttpMethod();

            if ("OPTIONS".equalsIgnoreCase(httpMethod)) {
                return createCorsPreflight();
            }

            if ("GET".equalsIgnoreCase(httpMethod)) {
                List<Post> posts = postService.getPosts();
                String body = objectMapper.writeValueAsString(posts);
                return createResponse(200, body, "application/json");
            } else if ("POST".equalsIgnoreCase(httpMethod)) {
                String body = event.getBody();
                Post post = objectMapper.readValue(body, Post.class);
                postService.savePost(post);
                return createResponse(201, "Post created successfully", "text/plain");
            } else {
                return createResponse(405, "Method Not Allowed", "text/plain");
            }
        } catch (Exception ex) {
            context.getLogger().log("Error in PostsLambdaHandler: " + ex.getMessage());
            return createResponse(500, "Internal Server Error: " + ex.getMessage(), "text/plain");
        }
    }

    private APIGatewayProxyResponseEvent createCorsPreflight() {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(204)
                .withHeaders(createCorsHeaders());
    }

    private APIGatewayProxyResponseEvent createResponse(int status, String body, String contentType) {
        Map<String, String> headers = createCorsHeaders();
        headers.put("Content-Type", contentType);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(status)
                .withBody(body)
                .withHeaders(headers);
    }

    private Map<String, String> createCorsHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Amz-Date,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Max-Age", "3600");
        return headers;
    }
}
