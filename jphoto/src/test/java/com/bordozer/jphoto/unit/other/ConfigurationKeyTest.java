package other;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static junit.framework.Assert.assertFalse;

public class ConfigurationKeyTest extends AbstractTestCase {

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void configurationKeyCanNotHasDuplicateIdsTest() {

        final Set<Integer> ids = newHashSet();

        for (final ConfigurationKey configurationKey : ConfigurationKey.values()) {

            assertFalse(String.format("Configuration key '%s' has id=%d but this Id has already been assigned to another configuration key", configurationKey, configurationKey.getId()), ids.contains(configurationKey.getId()));

            ids.add(configurationKey.getId());

        }
    }
}
