package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.model.OsmNode;

public interface OsmDataService {

    /**
     * Lädt einen OSM-Node von der externen OSM-API.
     *
     * @param osmNodeId OSM node id
     * @return OsmNode mit allen relevanten Daten
     * @throws de.seuhd.campuscoffee.domain.exceptions.OsmNodeNotFoundException wenn der Node nicht existiert
     */
    OsmNode loadNode(long osmNodeId);
}
