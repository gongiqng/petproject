package org.pet.home.util;

import org.pet.home.entity.Location;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public class AddressDistanceComparator {
    /**
     * 对比1个地址与多个地址的距离返回最近的那个
     * @param targetLocation
     * @param locations
     * @return
     */
    public static Location findNearestAddress(Location targetLocation, List<Location> locations) {
        Location nearestLocation = null;
        double minDistance = Double.MAX_VALUE;

        for (Location location : locations) {
            double distance = DistanceCalculator.calculateDistance(targetLocation, location);

            if (distance < minDistance) {
                minDistance = distance;
                nearestLocation = location;
            }
        }

        return nearestLocation;
    }
}
