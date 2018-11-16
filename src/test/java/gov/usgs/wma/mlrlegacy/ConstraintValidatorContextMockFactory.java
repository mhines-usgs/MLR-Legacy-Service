package gov.usgs.wma.mlrlegacy;

import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.spi.time.TimeProvider;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Supports testing ConstraintValidators.
 * Usages of ConstraintValidatorContext require more sophisticated mocks.
 */
public class ConstraintValidatorContextMockFactory {
	public static ConstraintValidatorContext get () {
		PathImpl path = PathImpl.createRootPath();
		path.addBeanNode();
		TimeProvider timeProvider = mock(TimeProvider.class);
		ConstraintValidatorContextImpl context = new ConstraintValidatorContextImpl(
			null, timeProvider, path, null
		);
		context.disableDefaultConstraintViolation();
		return spy(context);
	}
}
