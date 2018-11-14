package gov.usgs.wma.mlrlegacy;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ValidationResultImpl implements ValidationResult {

	protected final boolean valid;
	protected final List<String> messages;

	/**
	 * 
	 * @param valid whether or not a validation passed
	 * @param messages `null` of empty List if `valid == true`. 
	 */
	public ValidationResultImpl(boolean valid, List<String> messages) {
		if (valid && messages != null && !messages.isEmpty()) {
			throw new IllegalArgumentException("No validation message expected when valid.");
		} else if (!valid && messages == null) {
			throw new IllegalArgumentException("Validation message required when invalid");
		}
		this.valid = valid;
		this.messages = messages;
	}
	/**
	 * @return whether or not valid
	 */
	@Override
	public boolean isValid() {
		return valid;
	}

	/**
	 * @return the messages. Empty List if valid.
	 */
	@Override
	public List<String> getMessages() {
		return messages == null ? Arrays.asList() : messages;
	}

	@Override
	public ValidationResult and(ValidationResult otherValidationResult) {
		boolean combinedValidities = this.isValid() && otherValidationResult.isValid();
		List<String> combinedMessages = new ArrayList<>(this.getMessages());
		combinedMessages.addAll(otherValidationResult.getMessages());
		ValidationResult combinedResult = new ValidationResultImpl(
			combinedValidities,
			combinedMessages
		);
		return combinedResult;
	}
	
	@Override
	public boolean equals(Object thatObject) {
		boolean equal = false;
		if (null != thatObject) {
			if (thatObject instanceof ValidationResultImpl) {
				ValidationResultImpl that = (ValidationResultImpl) thatObject;
				if (this.valid == that.valid) {
					if (null == this.messages && null == that.messages) {
						equal = true;
					
					} else {
						//At this point we should be dealing 
						//with two invalid results that both 
						//have non-null messages.
						if (this.messages.equals(that.messages)) {
							equal = true;
						}
					}
				}
			}
		}
		return equal;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 23 * hash + (this.valid ? 1 : 0);
		hash = 23 * hash + Objects.hashCode(this.messages);
		return hash;
	}
}
