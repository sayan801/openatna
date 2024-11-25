package org.openhealthtools.openatna.all.test.persistence;

import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.util.DataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Populates the DB with dummy data
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 9:01:02 PM
 * @date $Date:$ modified by $Author:$
 */
public class Populate {

    private static final Logger LOGGER = LoggerFactory.getLogger(Populate.class);

    public static void main(String[] args) {
        try {
            InputStream in = Populate.class.getClassLoader().getResourceAsStream("test-data.xml");
            DataReader reader = new DataReader(in);
            reader.parse();
        } catch (AtnaPersistenceException e) {
            LOGGER.error("ERROR: '{}'", e.getError());
        }
    }
}
