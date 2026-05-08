package alchemy.laboratory;

/**
 * An interface for devices that can contain at most one ingredient container
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 * @version 1.0
 */
public interface SingleContainerDevice {

    /**
     * Check whether this device currently contains an ingredient container
     */
    boolean hasContainer();
}