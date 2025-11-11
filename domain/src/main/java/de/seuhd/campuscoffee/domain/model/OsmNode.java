package de.seuhd.campuscoffee.domain.model;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.util.Map;

/**
 * Represents an OpenStreetMap node with relevant Point of Sale information.
 * This is the domain model for OSM data before it is converted to a POS object.
 *
 * @param nodeId The OpenStreetMap node ID.
 * @param latitude The latitude of the node.
 * @param longitude The longitude of the node.
 * @param tags A map of OSM tags (k -> v), e.g. name, addr:street, addr:housenumber, ...
 */
@Builder
public record OsmNode(
        @NonNull Long nodeId,
        Double latitude,
        Double longitude,
        Map<String, String> tags
) {
}
