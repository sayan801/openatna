/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */

package org.openhealthtools.openatna.net;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.common.utils.DateUtil;
import org.openhealthtools.common.utils.StringUtil;

/**
 * This classes defines an Object as in the ObjectList.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class ObjectEntry {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.net.ObjectEntry");

    private static String name = null;
    private HashMap<String, Field> values = new HashMap<String, Field>();

    public ObjectEntry() {
    }

    public ObjectEntry(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean containsValue(String name) {
        if (name == null) {
            return false;
        }
        return values.containsKey(name);
    }

    public Field getField(String name) {
        if (name == null) {
            return null;
        }
        return values.get(name);
    }

    /**
     * Gets the field value as String type directly for a given field name.
     *
     * @param name The field name
     * @return the value of String type
     */
    public String getStringValue(String name) {
        Field field = getField(name);
        if (field == null) {
            return null;
        } else if (field.getType() == null || field.getType().equalsIgnoreCase("String")) {
            return (String) field.getValueObject();
        } else {
            logWrongTypeWarningMessage(field.getName(), "String");
            return null;
        }
    }

    /**
     * Gets the field value as Date type directly for a given field name.
     *
     * @param name The field name
     * @return the value of Date type
     */
    public Calendar getDateValue(String name) {
        Field field = getField(name);
        if (field == null) {
            return null;
        } else if (field.getType() != null && field.getType().equalsIgnoreCase("Date")) {
            return (Calendar) field.getValueObject();
        } else {
            logWrongTypeWarningMessage(field.getName(), "Date");
            return null;
        }
    }

    /**
     * Gets the field value as Code type directly for a given field name.
     *
     * @param name The field name
     * @return the value of CodeSystem type
     */
    public CodeSystem getCodeValue(String name) {
        Field field = getField(name);
        if (field == null) {
            return null;
        } else if (field.getType() != null && field.getType().equalsIgnoreCase("Code")) {
            return (CodeSystem) field.getValueObject();
        } else {
            logWrongTypeWarningMessage(field.getName(), "Code");
            return null;
        }
    }

    /**
     * Gets the field value as Measure type directly for a given field name.
     *
     * @param name The field name
     * @return the value of Measure type
     */
    public Measure getMeasureValue(String name) {
        Field field = getField(name);
        if (field == null) {
            return null;
        } else if (field.getType() != null && field.getType().equalsIgnoreCase("Measure")) {
            return (Measure) field.getValueObject();
        } else {
            logWrongTypeWarningMessage(field.getName(), "Measure");
            return null;
        }
    }

    private void logWrongTypeWarningMessage(String fieldName, String fieldType) {
        log.warn("Wrong type in " + fieldName + " Field element in ObjectEntry. " + fieldType + " type is expected.");
    }

    public void addField(String name, Field field) {
        if (name == null) {
            return;
        }
        if (field == null || !StringUtil.goodString(field.getValue())) {
            values.remove(name);
        } else {
            values.put(name, field);
        }
    }

    public static class Field {
        private String name;
        private String value;
        private String type;
        private String format;

        /**
         * @param name   the name of the field
         * @param value  the value of the field
         * @param type   The valid type can be String, Date, Double, Integer, Code, Measure
         * @param format If the type is Date, then the format of the date needs to be provided
         */
        public Field(String name, String value, String type, String format) {
            this.name = name;
            this.value = value;
            this.type = type;
            this.format = format;
        }

        public String getName() {
            return this.name;
        }

        public String getValue() {
            return this.value;
        }

        public Object getValueObject() {
            //the type defaults to String
            if (this.type == null) {
                return this.value;
            }
            if (this.value == null) {
                return null;
            } else if (this.type.equalsIgnoreCase("Date")) {
                try {
                    return DateUtil.parseCalendar(this.value, this.format);
                } catch (ParseException e) {
                    log.warn("Cannot parse Date in " + name + " Field element in ObjectEntry", e);
                }
                return null;
            } else if (this.type.equalsIgnoreCase("Integer")) {
                return Integer.parseInt(this.value);
            } else if (this.type.equalsIgnoreCase("Double")) {
                return Double.parseDouble(this.value);
            } else if (this.type.equalsIgnoreCase("Code")) {
                return CodeSystem.parseCode(this.value, this.name);
            } else if (this.type.equalsIgnoreCase("Measure")) {
                return new Measure(Integer.parseInt(this.value), this.format);
            } else {
                //Lastly, just return the value itself as a String
                return this.value;
            }
        }

        public String getType() {
            return this.type;
        }

        public String getFormat() {
            return this.format;
        }
    }

    public static class CodeSystem {
        private String code = null;
        private String displayName = null;
        private String codeSystem = null;
        private String codeSystemName = null;
        private String version = null;

        public CodeSystem(String code, String displayName, String codeSystem, String codeSystemName, String version) {
            this.code = code;
            this.displayName = displayName;
            this.codeSystem = codeSystem;
            this.codeSystemName = codeSystemName;
            this.version = version;
        }

        public String getCode() {
            return this.code;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public String getCodeSystem() {
            return this.codeSystem;
        }

        public String getCodeSystemName() {
            return this.codeSystemName;
        }

        public String getVersion() {
            return this.version;
        }

        /**
         * Parses a give code value to different parts.
         *
         * @param codeValue The codeValue to be parsed. The syntax of code value is
         *                  <code>Code^CodeDisplayName^CodeSystem^CodeSystemName^Version</code>
         *                  The ^ character should be reserved event if any part is missing.
         * @return An string array of code parts.
         */
        private static CodeSystem parseCode(String codeValue, String fieldName) {
            String[] parts = codeValue.split("\\^", 5);
            if (parts.length != 5) {
                log.warn("Cannot parse Code in " + fieldName + " Field element in ObjectEntry");
            } else {
                return new CodeSystem(parts[0], parts[1], parts[2], parts[3], parts[4]);
            }

            return null;
        }
    }

    public static class Measure {
        private int value = 0;
        private String unit = null;

        public Measure(int value, String unit) {
            this.value = value;
            this.unit = unit;
        }

        public int getValue() {
            return this.value;
        }

        public String getUnit() {
            return this.unit;
        }
    }
}
