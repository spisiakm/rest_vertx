/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.obsidiantoaster.quickstart;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.Json;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.obsidiantoaster.quickstart.service.Greeting;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(VertxUnitRunner.class)
public class RestApplicationTest {

    private Vertx vertx;
    private HttpClient client;

    @Before
    public void before(TestContext context) {
        vertx = Vertx.vertx();
        vertx.exceptionHandler(context.exceptionHandler());
        vertx.deployVerticle(RestApplication.class.getName(), context.asyncAssertSuccess());
        client = vertx.createHttpClient();
    }

    @After
    public void after(TestContext context) {
        client.close();
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void callGreetingTest(TestContext context) {
        // Send a request and get a response
        Async async = context.async();
        client.get(8080, "localhost", "/greeting", resp -> {
            assertThat(resp.statusCode()).isEqualTo(200);
            resp.bodyHandler(body -> {
                Greeting greeting = Json.decodeValue(body.toString(), Greeting.class);
                assertThat(greeting.getContent()).isEqualTo("Hello, World!");
            });
            async.complete();
        })
            .exceptionHandler(context::fail)
            .end();
    }

    @Test
    public void callGreetingWithParamTest(TestContext context) {
        // Send a request and get a response
        Async async = context.async();
        client.get(8080, "localhost", "/greeting?name=Charles", resp -> {
            assertThat(resp.statusCode()).isEqualTo(200);
            resp.bodyHandler(body -> {
                Greeting greeting = Json.decodeValue(body.toString(), Greeting.class);
                assertThat(greeting.getContent()).isEqualTo("Hello, World!");
            });
            async.complete();
        })
            .exceptionHandler(context::fail)
            .end();
    }

}
