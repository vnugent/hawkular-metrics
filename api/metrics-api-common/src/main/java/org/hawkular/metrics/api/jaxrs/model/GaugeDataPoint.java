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
package org.hawkular.metrics.api.jaxrs.model;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import static org.hawkular.metrics.core.api.MetricType.GAUGE;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.Map;

import org.hawkular.metrics.core.api.DataPoint;
import org.hawkular.metrics.core.api.Metric;
import org.hawkular.metrics.core.api.MetricId;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import rx.Observable;

/**
 * @author John Sanda
 */
@ApiModel(description = "A timestamp and a value where the value is interpreted as a floating point number")
public class GaugeDataPoint {
    private final long timestamp;
    private final double value;
    private final Map<String, String> tags;

    @JsonCreator(mode = Mode.PROPERTIES)
    @org.codehaus.jackson.annotate.JsonCreator
    @SuppressWarnings("unused")
    public GaugeDataPoint(
            @JsonProperty("timestamp")
            @org.codehaus.jackson.annotate.JsonProperty("timestamp")
            Long timestamp,
            @JsonProperty("value")
            @org.codehaus.jackson.annotate.JsonProperty("value")
            Double value,
            @JsonProperty("tags")
            @org.codehaus.jackson.annotate.JsonProperty("tags")
            Map<String, String> tags
    ) {
        checkArgument(timestamp != null, "Data point timestamp is null");
        checkArgument(value != null, "Data point value is null");
        this.timestamp = timestamp;
        this.value = value;
        this.tags = tags == null ? emptyMap() : unmodifiableMap(tags);
    }

    public GaugeDataPoint(DataPoint<Double> dataPoint) {
        timestamp = dataPoint.getTimestamp();
        value = dataPoint.getValue();
        tags = dataPoint.getTags();
    }

    @ApiModelProperty(required = true)
    public long getTimestamp() {
        return timestamp;
    }

    @ApiModelProperty(required = true)
    public double getValue() {
        return value;
    }

    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @org.codehaus.jackson.map.annotate.JsonSerialize(
            include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_EMPTY
    )
    public Map<String, String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GaugeDataPoint that = (GaugeDataPoint) o;
        return timestamp == that.timestamp && Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (timestamp ^ (timestamp >>> 32));
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("timestamp", timestamp)
                .add("value", value)
                .add("tags", tags)
                .toString();
    }

    public static List<DataPoint<Double>> asDataPoints(List<GaugeDataPoint> points) {
        return Lists.transform(points, p -> new DataPoint<>(p.getTimestamp(), p.getValue()));
    }

    public static Observable<Metric<Double>> toObservable(String tenantId, String metricId, List<GaugeDataPoint>
            points) {
        List<DataPoint<Double>> dataPoints = asDataPoints(points);
        Metric<Double> metric = new Metric<>(new MetricId<>(tenantId, GAUGE, metricId), dataPoints);
        return Observable.just(metric);
    }
}
