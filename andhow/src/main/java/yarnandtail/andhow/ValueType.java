package yarnandtail.andhow;

/**
 *
 * @author eeverman
 */
public interface ValueType<T> {
	
	Class<T> getDestinationType();
	
	/**
	 * Attempts to convert the passed String into the type represented
	 * by the destinationType.
	 * 
	 * All trimming (removing whitespace from around a value) should be assumed
	 * to already have happened for the incoming sourceValue by a Trimmer.
	 * 
	 * Implementations should be careful to ONLY throw a ParsingException -
	 * Integers and other types may throw other unchecked exceptions when trying
	 * to convert values, which should be handled in this method and rethrown as
	 * a ParsingException.
	 * @param sourceValue
	 * @return
	 * @throws ParsingException 
	 */
	T convert(String sourceValue) throws ParsingException;
		
	boolean isConvertable(String sourceValue);
	
	/**
	 * Attempt to cast the passed object to the generic type T.
	 * AndHow uses this internally to cast values known to already be of type T,
	 * but were stored in a generic way.
	 * If used for unknown types that are not castable to T, it will throw
	 * a RuntimeException.
	 * 
	 * @param o
	 * @return
	 * @throws RuntimeException 
	 */
	T cast(Object o) throws RuntimeException;
}
