package de.seuhd.campuscoffee.domain.exceptions;

public class OsmNodeNotFoundException extends RuntimeException {

    private final long osmNodeId;

    public OsmNodeNotFoundException(long osmNodeId) {
        super("OSM node with id " + osmNodeId + " was not found");
        this.osmNodeId = osmNodeId;
    }

    public long getOsmNodeId() {
        return osmNodeId;
    }
}
