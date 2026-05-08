package alchemy.laboratory;

/**
 * An interface for devices that can contain multiple ingredient containers
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 * @version 1.0
 */
public interface MultiContainerDevice {

    /**
     * Return the number of ingredient containers currently contained in this device
     */
    int getNbContainers();

    /**
     * Check whether this device currently contains no ingredient containers
     */
    default boolean isEmpty() {
        return getNbContainers() == 0;
    }
}