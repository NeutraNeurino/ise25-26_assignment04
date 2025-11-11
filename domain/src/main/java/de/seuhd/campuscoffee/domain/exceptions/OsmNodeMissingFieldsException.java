package de.seuhd.campuscoffee.domain.exceptions;

import java.util.Set;

public class OsmNodeMissingFieldsException extends RuntimeException {

    private final long osmNodeId;
    private final Set<String> missingFields;

    public OsmNodeMissingFieldsException(long osmNodeId, Set<String> missingFields) {
        super("OSM node " + osmNodeId + " is missing required fields: " + missingFields);
        this.osmNodeId = osmNodeId;
        this.missingFields = missingFields;
    }

    public long getOsmNodeId() {
        return osmNodeId;
    }

    public Set<String> getMissingFields() {
        return missingFields;
    }
}
