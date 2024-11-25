package org.openhealthtools.openatna.audit;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * @author Andrew Harrison
 */
public class OpenAtnaPropertiesLoader extends PropertySourcesPlaceholderConfigurer {

    @Override
    public void setLocation(Resource location) {
        String loc = AtnaFactory.getPropertiesLocation();
        if (loc != null && loc.length() > 0) {

            location = new ClassPathResource(loc);
            if (!location.exists()) {
                location = new FileSystemResource(loc);
            }
        }
        super.setLocation(location);
    }
}
