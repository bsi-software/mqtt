/*
 * Copyright 2014 BSI Business Systems Integration AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bsiag.hivemq.scanner.callbacks;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bsiag.hivemq.scanner.plugin.Scanner;
import com.dcsquare.hivemq.spi.callback.events.OnUnsubscribeCallback;
import com.dcsquare.hivemq.spi.callback.exception.RefusedConnectionException;
import com.dcsquare.hivemq.spi.message.CONNECT;
import com.dcsquare.hivemq.spi.message.Topic;
import com.dcsquare.hivemq.spi.message.UNSUBSCRIBE;
import com.dcsquare.hivemq.spi.security.ClientData;
import com.dcsquare.hivemq.spi.services.PublishService;
import com.dcsquare.hivemq.spi.services.RetainedMessageStore;
import com.google.common.base.Joiner;
import com.google.inject.Inject;

/**
 * This class is an acme of a callback, which is invoked every time a new client is
 * successfully authenticated. The callback can be used to execute some custom behavior,
 * which is necessary when a new client connects or to implement a custom logic based
 * on the {@link CONNECT} to refuse the connection throwing a
 * {@link RefusedConnectionException}.
 *
 * @author Matthias Zimmermann
 */
public class ClientUnsubscribe implements OnUnsubscribeCallback {

    Logger log = LoggerFactory.getLogger(ClientUnsubscribe.class);
    
	private final PublishService service;
	private final RetainedMessageStore store;

    @Inject
    public ClientUnsubscribe(final PublishService publishService, final RetainedMessageStore retainedMessageStore) {
        service = publishService;
        store = retainedMessageStore;
    }

	/**
     * Publishes retained messages to update the client's new subscription.
     * This is the callback method, which is called by the HiveMQ core, if a client has sent,
     * a {@link CONNECT} Message and was successfully authenticated. 
     *
     * @param connect    The {@link CONNECT} message from the client.
     * @param clientData Useful information about the clients authentication state and credentials.
     * @throws RefusedConnectionException This exception should be thrown, if the client is
     *                                    not allowed to connect.
     */
    @Override
	public void onUnsubscribe(UNSUBSCRIBE message, ClientData clientData)  {
    	String clientId = clientData.getClientId();
    	List<String> topics = message.getTopics();
    	
        log.info("Client {} unsubscribes topics: {}", clientId, Joiner.on(",").join(topics));

        Scanner.publishTopics(service, store, clientId, new ArrayList<Topic>(), topics);
    }
}
