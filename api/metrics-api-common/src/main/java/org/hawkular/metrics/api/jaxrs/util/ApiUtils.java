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
package org.hawkular.metrics.api.jaxrs.util;

import java.util.Collection;
import java.util.Optional;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hawkular.metrics.api.jaxrs.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

/**
 * @author jsanda
 */
public class ApiUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ApiUtils.class);

    private ApiUtils() {
    }

    public static Response collectionToResponse(Collection<?> collection) {
        return collection.isEmpty() ? noContent() : Response.ok(collection).type(MediaType.APPLICATION_JSON).build();
    }

    public static Response serverError(Throwable t, String message) {
        LOG.trace("Server error response", t);
        String errorMsg = message + ": " + Throwables.getRootCause(t).getMessage();
        return Response.serverError().type(MediaType.APPLICATION_JSON).entity(new ApiError(errorMsg)).build();
    }

    public static Response serverError(Throwable t) {
        return serverError(t, "Failed to perform operation due to an error");
    }

    public static Response valueToResponse(Optional<?> optional) {
        return optional.map(value -> Response.ok(value).build()).orElse(noContent());
    }

    public static Response noContent() {
        return Response.noContent().build();
    }

    public static Response emptyPayload() {
        return badRequest(new ApiError("Payload is empty"));
    }

    public static Response badRequest(ApiError error) {
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }

    public static Response badRequest(Throwable t) {
        ApiError error = new ApiError(t.getLocalizedMessage());
        return badRequest(error);
    }
}