package gov.usgs.wma.mlrlegacy;

/**
 * Validates!
 * 
 */
public interface Validator<T> {

	/**
	 *
	 * @param toValidate
	 * @return a ValidationResult
	 */
	public ValidationResult validate(T toValidate);
}
