package kwaqua;

import net.thucydides.jbehave.ThucydidesStepFactory;
import org.jbehave.core.configuration.Configuration;

import java.util.Collections;
import java.util.List;

public class SingleTestStepsFactory extends ThucydidesStepFactory {

	private final Class<?> testClass;

	public SingleTestStepsFactory( final Class<? extends KwaquaStory> testClass, final Configuration configuration, final String rootPackage, final ClassLoader classLoader ) {
		super( configuration, rootPackage, classLoader );
		this.testClass = testClass;
	}

	@Override
	protected List<Class<?>> stepsTypes() {
		return Collections.<Class<?>>singletonList( testClass );
	}
}
