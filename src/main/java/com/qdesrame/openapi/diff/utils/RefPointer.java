package com.qdesrame.openapi.diff.utils;

import io.swagger.v3.oas.models.Components;

import java.util.Map;

/**
 * Created by adarsh.sharma on 07/01/18.
 */
public class RefPointer<T> {
    public final static String BASE_REF = "#/components/";
    private RefType refType;

    public RefPointer(RefType refType) {
        this.refType = refType;
    }

    public T resolveRef(Components components, T t, String ref) {
        if (ref != null) {
            String refName = getRefName(refType.getName(), ref);
            T result = getMap(components).get(refName);
            if (result == null) {
                throw new IllegalArgumentException(String.format("ref '%s' doesn't exist.", ref));
            }
            return result;
        }
        return t;
    }

    private Map<String, T> getMap(Components components) {
        switch (refType) {
            case REQUEST_BODIES:
                return (Map<String, T>) components.getRequestBodies();

            case RESPONSES:
                return (Map<String, T>) components.getResponses();

            case PARAMETERS:
                return (Map<String, T>) components.getParameters();

            case SCHEMAS:
                return (Map<String, T>) components.getSchemas();

            case HEADERS:
                return (Map<String, T>) components.getSchemas();

            case SECURITY_SCHEMES:
                return (Map<String, T>) components.getSecuritySchemes();

            default:
                throw new IllegalArgumentException("Not mapped for refType: " + refType);
        }
    }

    private String getBaseRefForType(String type) {
        return String.format("%s%s/", BASE_REF, type);
    }

    private String getRefName(String type, String ref) {
        final String baseRef = getBaseRefForType(type);
        if (!ref.startsWith(baseRef)) {
            throw new IllegalArgumentException("Invalid ref: " + ref);
        }
        return ref.substring(baseRef.length());
    }

    public String getRefName(String ref) {
        if (ref != null) {
            return getRefName(refType.getName(), ref);
        }
        return null;
    }
}
