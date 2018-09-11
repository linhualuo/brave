package brave.vertx.web;

import brave.vertx.web.TracingRoutingContextHandler.Adapter;
import io.vertx.core.http.HttpServerResponse;
import java.lang.reflect.Proxy;
import org.junit.After;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TracingRoutingContextHandlerAdapterTest {
  Adapter adapter = new Adapter();

  @After public void clear() {
    TracingRoutingContextHandler.METHOD_AND_PATH.remove();
  }

  @Test public void methodFromResponse() {
    HttpServerResponse response = dummyResponse();

    TracingRoutingContextHandler.getMethodAndPath()[0] = "GET";
    assertThat(adapter.methodFromResponse(response))
        .isEqualTo("GET");
  }

  @Test public void route_emptyByDefault() {
    HttpServerResponse response = dummyResponse();

    TracingRoutingContextHandler.getMethodAndPath()[0] = "GET";
    assertThat(adapter.route(response)).isEmpty();
  }

  @Test public void route() {
    HttpServerResponse response = dummyResponse();

    TracingRoutingContextHandler.getMethodAndPath()[0] = "GET";
    TracingRoutingContextHandler.getMethodAndPath()[1] = "/users/:userID";

    assertThat(adapter.route(response))
        .isEqualTo("/users/:userID");
  }

  /** In JRE 1.8, mockito crashes with 'Mockito cannot mock this class' */
  HttpServerResponse dummyResponse() {
    return (HttpServerResponse) Proxy.newProxyInstance(
        getClass().getClassLoader(),
        new Class[] {HttpServerResponse.class},
        (proxy, method, methodArgs) -> {
          throw new UnsupportedOperationException(
              "Unsupported method: " + method.getName());
        });
  }
}
