package io.pavelkoch.eoin.rtm;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.glassfish.tyrus.client.SslContextConfigurator;
import org.glassfish.tyrus.client.SslEngineConfigurator;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class Client {
    /**
     * The web socket URI to connect to.
     */
    private final URI uri;

    /**
     * @param uri The web socket URI to connect to.
     * @throws URISyntaxException If the URI is no valid
     */
    public Client(String uri) throws URISyntaxException {
        this.uri = new URI(uri);
    }

    /**
     * Attempts to connect to the Slack web socket, assigning event listeners.
     *
     * @throws IOException If the connection fails
     * @throws DeploymentException If the connection fails
     * @throws InterruptedException If the web socket is interrupted.
     */
    public void connect() throws IOException, DeploymentException, InterruptedException {
        ClientManager client = ClientManager.createClient();

        // We need to set all Slack hosts to be trusted
        // as default, can't see why this could be an issue
        client.getProperties().put(ClientProperties.SSL_ENGINE_CONFIGURATOR, this.configureSsl());

        // We attempt to establish the connection while
        // assigning the message handler to the Handler class
        client.connectToServer(new EoinEndpoint(), ClientEndpointConfig.Builder.create().build(), this.uri);

        // We set the default wait timer to a day xd.
        client.getExecutorService().awaitTermination(1, TimeUnit.DAYS);
    }

    /**
     * We just turn off host checking.
     *
     * @return The SSL configuration
     */
    private SslEngineConfigurator configureSsl() {
        SslEngineConfigurator sslEngineConfigurator = new SslEngineConfigurator(new SslContextConfigurator());
        sslEngineConfigurator.setHostVerificationEnabled(false);

        return sslEngineConfigurator;
    }

    /**
     * The endpoint inner class.
     */
    public class EoinEndpoint extends Endpoint {
        /**
         * Fired upon opening the web socket session.
         *
         * @param session The current web socket session
         * @param config The eoin endpoint configuration
         */
        @Override
        public void onOpen(Session session, EndpointConfig config) {
            session.addMessageHandler(new Handler(session));
        }
    }
}
