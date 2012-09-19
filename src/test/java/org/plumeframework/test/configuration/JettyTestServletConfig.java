/*
 * Copyright (C) 2012 salesforce.com, inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.plumeframework.test.configuration;

import java.net.*;

import org.apache.commons.httpclient.HttpClient;
import org.plumeframework.Plume;

public class JettyTestServletConfig implements TestServletConfig {
    private URL baseUrl;

    public JettyTestServletConfig() throws MalformedURLException, URISyntaxException {
        int port = Integer.parseInt(System.getProperty("jetty.port", "8080"));
        String host = System.getProperty("jetty.host");
        if (host == null) {
            try {
                host = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                host = "localhost";
            }
        }
        baseUrl = new URL("http", host, port, "/");
    }

    @Override
    public URL getBaseUrl() {
        return baseUrl;
    }

    @Override
    public HttpClient getHttpClient() {
        // 10 minute timeout for making a connection and for waiting for data on the connection.
        // This prevents tests from hanging in the http code, which in turn can prevent the server from exiting.
        int timeout = 10 * 60 * 1000;
        HttpClient http = new HttpClient();
        http.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
        http.getParams().setSoTimeout(timeout);
        return http;
    }

    @Override
    public String getCsrfToken() throws Exception {
        String token = Plume.getConfigAdapter().getCSRFToken();
        return token == null ? "" : token;
    }
}
