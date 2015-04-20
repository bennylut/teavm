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
import org.teavm.model.instructions.InvokeInstruction;

/**
 *
 * @author bennyl
 */
public class InvokeSubtituteBuilder extends DelegatingSubstituteBuilder<InvokeInstruction, InvokeSubtituteBuilder> {

    public InvokeSubtituteBuilder(SubstituteBuilder s) {
        super(s);
        assignReceiver(getInstruction().getReceiver(), getInstruction().getMethod().getReturnType(), ConversionMode.TO_JAVA);
    }

    public InvokeSubtituteBuilder appendArgJS(int index) {
        return append(getInstruction().getArguments().get(index), getInstruction().getMethod().getParameterTypes()[index], ConversionMode.TO_JS);
    }

    public InvokeSubtituteBuilder appendArgListJS() {
        append("(");
        final int numArgs = getInstruction().getArguments().size();
        for (int i = 0; i < numArgs; i++) {
            appendArgJS(i);
            if (i + 1 < numArgs) {
                append(", ");
            }
        }
        append(")");
        return this;
    }

    public InvokeSubtituteBuilder appendInstance() {
        return append(getInstruction().getInstance());
    }

}
