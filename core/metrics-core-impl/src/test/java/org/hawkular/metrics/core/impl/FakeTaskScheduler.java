/*
 * Copyright 2014-2015 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.metrics.core.impl;

import java.util.Map;

import org.hawkular.metrics.tasks.api.Task2;
import org.hawkular.metrics.tasks.api.TaskScheduler;
import org.hawkular.metrics.tasks.api.Trigger;
import org.hawkular.metrics.tasks.impl.Lease;
import rx.Observable;

/**
 * @author jsanda
 */
public class FakeTaskScheduler implements TaskScheduler {

    @Override
    public Observable<Lease> start() {
        return Observable.empty();
    }

    @Override
    public Observable<Task2> scheduleTask(String name, String groupKey, int executionOrder,
            Map<String, String> parameters, Trigger trigger) {
        return Observable.empty();
    }

    @Override
    public void shutdown() {
    }
}