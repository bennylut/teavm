/*
 * Copyright 2015 bennyl.
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
 */
package org.teavm.jso.plugin.jsc;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.teavm.jso.JSArray;
import org.teavm.jso.JSBooleanArray;
import org.teavm.jso.JSDoubleArray;
import org.teavm.jso.JSIntArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSStringArray;
import org.teavm.model.ValueType;
import org.teavm.model.Variable;

/**
 *
 * @author bennyl
 */
public class DefaultJsConversionsService extends JsConversionService {

    @Override
    public Collection<String> supportedAnnotations() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Collection<ValueType> supportedTypes() {
        return Arrays.asList(
                ValueType.parse(String.class),
                ValueType.BOOLEAN,
                ValueType.BYTE,
                ValueType.CHARACTER,
                ValueType.DOUBLE,
                ValueType.FLOAT,
                ValueType.INTEGER,
                ValueType.LONG,
                ValueType.SHORT);
    }

    @Override
    public void toJava(Variable jsVar, ValueType type, JsConversionBuilder wb) {
        if (type.isObject("java.lang.String")) {
            wb.append("$rt_ustr(").append(jsVar).append(")");
        } else {
            wb.append(jsVar);
        }
    }

    @Override
    public void toJs(Variable javaVar, ValueType type, JsConversionBuilder wb) {
        if (type.isObject("java.lang.String")) {
            wb.append("$rt_str(").append(javaVar).append(")");
        } else {
            wb.append(javaVar);
        }
    }

    private ValueType getWrappedType(ValueType type) {
        if (type instanceof ValueType.Array) {
            ValueType itemType = ((ValueType.Array) type).getItemType();
            return ValueType.arrayOf(getWrappedType(itemType));
        } else if (type instanceof ValueType.Object) {
            if (type.isObject("java.lang.String")) {
                return type;
            } else {
                return ValueType.parse(JSObject.class);
            }
        } else {
            return type;
        }
    }

    private ValueType getWrapperType(ValueType type) {
        if (type instanceof ValueType.Array) {
            ValueType itemType = ((ValueType.Array) type).getItemType();
            if (itemType instanceof ValueType.Primitive) {
                switch (((ValueType.Primitive) itemType).getKind()) {
                    case BOOLEAN:
                        return ValueType.parse(JSBooleanArray.class);
                    case BYTE:
                    case SHORT:
                    case INTEGER:
                    case CHARACTER:
                        return ValueType.parse(JSIntArray.class);
                    case FLOAT:
                    case DOUBLE:
                        return ValueType.parse(JSDoubleArray.class);
                    case LONG:
                    default:
                        return ValueType.parse(JSArray.class);
                }
            } else if (itemType.isObject("java.lang.String")) {
                return ValueType.parse(JSStringArray.class);
            } else {
                return ValueType.parse(JSArray.class);
            }
        } else {
            return ValueType.parse(JSObject.class);
        }
    }

    @Override
    public void toJava(Variable jsVar, ValueType.Array type, JsConversionBuilder wb) {
        
    }

    @Override
    public void toJs(Variable javaVar, ValueType.Array type, JsConversionBuilder wb) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
