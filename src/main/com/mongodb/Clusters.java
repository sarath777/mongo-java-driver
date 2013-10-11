/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
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

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A factory for cluster implementations.
 *
 */
final class Clusters {
    private static final AtomicInteger NEXT_CLUSTER_ID = new AtomicInteger(1);

    private Clusters() {
    }

    public static Cluster create(final ClusterSettings settings, final ServerSettings serverSettings,
                                 final ScheduledExecutorService scheduledExecutorService,
                                 final ClusterListener clusterListener,
                                 final Mongo mongo) {
        String clusterId = Integer.toString(NEXT_CLUSTER_ID.getAndIncrement());

        ClusterableServerFactory serverFactory = new DefaultClusterableServerFactory(clusterId, serverSettings, scheduledExecutorService,
                                                                                     mongo);

        if (settings.getMode() == ClusterConnectionMode.Single) {
            return new SingleServerCluster(clusterId, settings, serverFactory,
                                           clusterListener != null ? clusterListener : new NoOpClusterListener());
        } else if (settings.getMode() == ClusterConnectionMode.Multiple) {
            return new MultiServerCluster(clusterId, settings, serverFactory,
                                          clusterListener != null ? clusterListener : new NoOpClusterListener());
        } else {
            throw new UnsupportedOperationException("Unsupported cluster mode: " + settings.getMode());
        }
    }
}