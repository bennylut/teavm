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
package org.teavm.jso.plugin.aproc;

import org.teavm.jso.plugin.util.DelegatingSubstituteBuilder;
import org.teavm.jso.plugin.util.SubstituteBuilder;
import org.teavm.jso.plugin.jsc.ConversionMode;
import org.teavm.model.instructions.PutFieldInstruction;

/**
 *
 * @author bennyl
 */
public class FieldPutSubtituteBuilder extends DelegatingSubstituteBuilder<PutFieldInstruction, FieldPutSubtituteBuilder> {

    public FieldPutSubtituteBuilder(SubstituteBuilder s) {
        super(s);
    }


    public String getFieldName() {
        return getInstruction().getField().getFieldName();
    }

    public FieldPutSubtituteBuilder appendValueJS() {
        return append(getInstruction().getValue(), getInstruction().getFieldType(), ConversionMode.TO_JS);
    }

    public FieldPutSubtituteBuilder appendInstance() {
        return append(getInstruction().getInstance());
    }
}
