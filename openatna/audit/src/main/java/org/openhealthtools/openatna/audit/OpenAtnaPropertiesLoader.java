/*
 * Copyright (c) 2009-2011 University of Cardiff and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cardiff University - intial API and implementation
 */

package org.openhealthtools.openatna.audit;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Mar 13, 2010: 11:10:05 AM
 */

public class OpenAtnaPropertiesLoader extends PropertyPlaceholderConfigurer {

    public void setLocation(Resource location) {
        String loc = AtnaFactory.getPropertiesLocation();
        if (loc != null && loc.length() > 0) {
            location = new ClassPathResource(loc);
        }
        super.setLocation(location);
    }


}
