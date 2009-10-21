/*
 * Copyright (c) 2009 University of Cardiff and others.
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

package org.openhealthtools.openatna.audit.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 21, 2009: 8:42:28 PM
 * @date $Date:$ modified by $Author:$
 */

public class PropertiesLoader {

    private static Properties atnaProps = new Properties();

    static {
        InputStream in = ServerConfiguration.class.getResourceAsStream("/openatna.properties");
        if (in == null) {
            throw new RuntimeException("FATAL: Could not find openatna.properties file!");
        }
        try {
            atnaProps.load(in);
        } catch (IOException e) {
            throw new RuntimeException("FATAL: Could not load openatna.properties file", e);
        }
    }

    public static Properties getProperties() {
        return atnaProps;
    }

    public static File getActorsFile() {
        File actors = new File(atnaProps.getProperty("ihe.actors.dir"));
        String actorsFile = actors.getAbsolutePath();
        actorsFile = actorsFile.replace(File.separator + "." + File.separator, File.separator);
        actors = new File(actorsFile);
        if (!actors.exists()) {
            return null;
        }
        String filename = "IheActors.xml";
        if (atnaProps.getProperty("ihe.actors.file") != null) {
            filename = atnaProps.getProperty("ihe.actors.file");
        }
        File configFile = new File(actors, filename);
        if (!configFile.exists()) {
            return null;
        }
        return configFile;
    }
}
