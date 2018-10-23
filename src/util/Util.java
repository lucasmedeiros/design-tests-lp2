package util;

import java.util.HashSet;
import java.util.Set;

public class Util {
	
	/**
	 * Generic method that allows obtaining the intersection of two given sets.
	 * 
	 * @param setA first set given.
	 * @param setB second set given.
	 * @return a third set with all elements in common between two sets.
	 */
	public static <T> Set<T> intersectionBetweenSets(Set<T> setA, Set<T> setB) {
        Set<T> setC = new HashSet<>(setA);
        setC.retainAll(setB);
        
        
        return setC;
    }
	
	/**
	 * Generic method that allows obtaining the union of two given sets.
	 * 
	 * @param setA first set given.
	 * @param setB second set given.
	 * @return a third set with all elements contained in two sets.
	 */
	public static <T> Set<T> unionBetweenSets(Set<T> setA, Set<T> setB) {
		Set<T> setC = new HashSet<>();
		
		setC.addAll(setA);
		setC.addAll(setB);
		
		return setC;
	}
}
