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
package org.hawkular.metrics.core.api;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * An enumeration of the supported metric types.
 *
 * @author John Sanda
 */
public final class MetricType<T> {
    public static final MetricType<Double> GAUGE = new MetricType<>(0, "gauge", true);
    public static final MetricType<AvailabilityType> AVAILABILITY = new MetricType<>(1, "availability", true);
    public static final MetricType<Long> COUNTER = new MetricType<>(2, "counter", true);
    public static final MetricType<Double> COUNTER_RATE = new MetricType<>(3, "counter_rate", false);
    // If you add a new type: don't forget to update the "all" and "userTypes" sets

    private static final Set<MetricType<?>> all = ImmutableSet.of(GAUGE, AVAILABILITY, COUNTER, COUNTER_RATE);

    private static final Set<MetricType<?>> userTypes;
    private static final Map<Integer, MetricType<?>> codes;
    private static final Map<String, MetricType<?>> texts;

    static {
        ImmutableSet.Builder<MetricType<?>> userTypesBuilder = ImmutableSet.builder();
        ImmutableMap.Builder<Integer, MetricType<?>> codesBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<String, MetricType<?>> textsBuilder = ImmutableMap.builder();
        all.forEach(type -> {
            if (type.isUserType()) {
                userTypesBuilder.add(type);
            }
            codesBuilder.put(type.code, type);
            textsBuilder.put(type.text, type);
        });
        userTypes = userTypesBuilder.build();
        codes = codesBuilder.build();
        checkArgument(codes.size() == all.size(), "Some metric types have the same code");
        texts = textsBuilder.build();
        checkArgument(texts.size() == all.size(), "Some metric types have the same string value");
    }

    private final int code;
    private final String text;
    private final boolean userType;

    private MetricType(int code, String text, boolean userType) {
        this.code = code;
        this.text = text;
        this.userType = userType;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public boolean isUserType() {
        return userType;
    }

    @Override
    public String toString() {
        return getText();
    }

    public static MetricType<?> fromCode(int code) {
        MetricType<?> type = codes.get(code);
        if (type == null) {
            throw new IllegalArgumentException(code + " is not a recognized metric type");
        }
        return type;
    }

    public static MetricType<?> fromTextCode(String textCode) {
        checkArgument(textCode != null, "textCode is null");
        MetricType<?> type = texts.get(textCode);
        if (type == null) {
            throw new IllegalArgumentException(textCode + " is not a recognized metric type");
        }
        return type;
    }

    public static Set<MetricType<?>> all() {
        return all;
    }

    public static Set<MetricType<?>> userTypes() {
        return userTypes;
    }
}
