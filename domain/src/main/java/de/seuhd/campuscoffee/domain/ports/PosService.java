package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.exceptions.DuplicatePosNameException;
import de.seuhd.campuscoffee.domain.exceptions.OsmNodeMissingFieldsException;
import de.seuhd.campuscoffee.domain.exceptions.OsmNodeNotFoundException;
import de.seuhd.campuscoffee.domain.exceptions.PosNotFoundException;
import de.seuhd.campuscoffee.domain.model.Pos;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Service interface for POS (Point of Sale) operations.
 *
 * This interface defines the core business operations for managing Points of Sale.
 * It is a domain port in the hexagonal (ports-and-adapters) architecture and is
 * implemented in the domain layer and consumed by the API layer.
 */
public interface PosService {

    /**
     * Clears all POS data.
     * Typically used for testing or administrative purposes.
     */
    void clear();

    /**
     * Retrieves all Points of Sale in the system.
     *
     * @return a list of all POS entities; never null, but may be empty
     */
    @NonNull List<Pos> getAll();

    /**
     * Retrieves a specific Point of Sale by its unique identifier.
     *
     * @param id the unique identifier of the POS to retrieve; must not be null
     * @return the POS entity with the specified ID; never null
     * @throws PosNotFoundException if no POS exists with the given ID
     */
    @NonNull Pos getById(@NonNull Long id) throws PosNotFoundException;

    /**
     * Creates a new POS or updates an existing one.
     * <ul>
     *     <li>If the POS has no ID (null), a new POS is created.</li>
     *     <li>If the POS has an ID and it exists, the existing POS is updated.</li>
     * </ul>
     *
     * @param pos the POS entity to create or update; must not be null
     * @return the persisted POS entity with populated ID and timestamps; never null
     * @throws PosNotFoundException       if attempting to update a POS that does not exist
     * @throws DuplicatePosNameException  if a POS with the same name already exists
     */
    @NonNull Pos upsert(@NonNull Pos pos) throws PosNotFoundException, DuplicatePosNameException;

    /**
     * Imports a Point of Sale from an OpenStreetMap node.
     * <p>
     * The import process:
     * <ol>
     *     <li>Loads OSM node data via {@link de.seuhd.campuscoffee.domain.ports.OsmDataService}.</li>
     *     <li>Extracts relevant tags (e.g. name, address fields).</li>
     *     <li>Maps the OSM data to the POS domain model.</li>
     *     <li>Persists the POS entity (typically via {@link #upsert(Pos)}).</li>
     * </ol>
     *
     * @param osmNodeId the OpenStreetMap node ID to import; must not be null
     * @return the created or updated POS entity; never null
     * @throws OsmNodeNotFoundException      if the OSM node with the given ID does not exist or cannot be fetched
     * @throws OsmNodeMissingFieldsException if the OSM node lacks required fields for creating a valid POS
     * @throws DuplicatePosNameException     if a POS with the same name already exists
     */
    @NonNull Pos importFromOsmNode(@NonNull Long osmNodeId)
            throws OsmNodeNotFoundException, OsmNodeMissingFieldsException, DuplicatePosNameException;
}
