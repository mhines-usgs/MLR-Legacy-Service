package gov.usgs.wma.mlrlegacy;

import java.util.List;

/**
 * This should be returned by Validators.
 * 
 */
public interface ValidationResult {
	
	/**
	 * Returns a new validation result with the following characteristics:
	 *  * the `isValid()` value is the AND of this ValidationResult's 
	 *     `isValid()` and `otherValidationResult.isValid()`
	 *  * the `getMessage()` value is the concatenation of both 
	 *     ValidationResults' `getMessages()` methods.
	 * @param otherValidationResult
	 * @return 
	 */
	public ValidationResult and(ValidationResult otherValidationResult);
	
	
	/**
	 * 
	 * @return true if valid, false if validation errors occur
	 */
	public boolean isValid();
	
	/**
	 * 
	 * @return if validation errors occur, return a nonzero-length
	 * List of Strings. If no validation errors, return an empty list
	 */
	public List<String> getMessages();
}

