package services;

import com.bordozer.jphoto.core.services.archiving.ArchivingServiceImpl;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ArchivingServiceTest extends AbstractTestCase {

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void getArchiveStartDate() {
        final ArchivingServiceImpl archivingService = new ArchivingServiceImpl();
        archivingService.setDateUtilsService(dateUtilsService);

        final int days = 5;
        assertEquals("Wrong Archive Start Date", archivingService.getArchiveStartDate(days).getTime(), dateUtilsService.getFirstSecondOfTheDayNDaysAgo(days - 1).getTime());
    }
}
