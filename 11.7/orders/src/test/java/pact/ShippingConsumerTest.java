package pact;

import au.com.dius.pact.consumer.ConsumerPactTestMk2;
import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import au.com.dius.pact.model.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.Rule;
import org.springframework.http.HttpStatus;
import unfiltered.response.Created;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ShippingConsumerTest extends ConsumerPactTestMk2 {

    @Rule
    public PactProviderRuleMk2 rule = new PactProviderRuleMk2("shipping-service", "localhost", 9999, this);

    private PactDslJsonBody requestBody = new PactDslJsonBody().stringValue("name", "shipment1").stringValue("amount",
            "8.0");

    private PactDslJsonBody responseBody = new PactDslJsonBody()
            .stringValue("id", "d331083b-a1f4-4d61-ab59-14eed5e3fc7f").stringValue("name", "shipment1")
            .stringValue("amount", "8.0");

    @Override
    protected RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder.given("shippment of order sent.").uponReceiving("shippment request from order service")
                .path("/shipping").query("username=consumerA").method("POST")
                .headers("Content-Type", "application/json").body(requestBody).willRespondWith().status(200)
                .body(responseBody).matchHeader("Content-Type", "application/json", "application/json").toPact();
    }

    @Override
    protected String providerName() {
        return "shipping-service";
    }

    @Override
    protected String consumerName() {
        return "orders-service";
    }

    @Override
    protected void runTest(MockServer mockServer) throws IOException {
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(mockServer.getUrl()).setPath("/shipping").setParameter("username", "consumerA");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        System.out.println(uriBuilder.toString());

        HttpResponse response = Request.Post(uriBuilder.toString()).body(new StringEntity(requestBody.toString()))
                .addHeader("Content-Type", "application/json").execute().returnResponse();

        String body = Request.Post(uriBuilder.toString()).body(new StringEntity(requestBody.toString()))
                .addHeader("Content-Type", "application/json").execute().returnContent().toString();

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(responseBody.toString(), body);

    }
}
