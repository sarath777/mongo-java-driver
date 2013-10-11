/*
 * Copyright (c) 2008 - 2013 MongoDB Inc., Inc. <http://mongodb.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb;

import static com.mongodb.ServerConnectionState.Connecting;

public class TestServer implements ClusterableServer {
    private ChangeListener<ServerDescription> changeListener;
    private ServerDescription description;
    private boolean isClosed;
    private ServerAddress serverAddress;

    public TestServer(final ServerAddress serverAddress) {
        this.serverAddress = serverAddress;
        invalidate();
    }

    public void sendNotification(final ServerDescription newDescription) {
        ServerDescription currentDescription = description;
        description = newDescription;
        if (changeListener != null) {
            changeListener.stateChanged(new ChangeEvent<ServerDescription>(currentDescription, newDescription));
        }
    }

    public void addChangeListener(final ChangeListener<ServerDescription> newChangeListener) {
        this.changeListener = newChangeListener;
    }

    public void invalidate() {
        description = ServerDescription.builder().state(Connecting).address(serverAddress).build();
    }

    public void close() {
        isClosed = true;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public ServerDescription getDescription() {
        return description;
    }

    @Override
    public DBPort getConnection() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
