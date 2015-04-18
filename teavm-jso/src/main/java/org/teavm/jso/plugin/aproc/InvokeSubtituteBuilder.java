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

import java.util.List;
import org.teavm.model.ClassHolderSource;
import org.teavm.model.Instruction;
import org.teavm.model.MethodHolder;
import org.teavm.model.ValueType;
import org.teavm.model.Variable;
import org.teavm.model.instructions.InvokeInstruction;

/**
 *
 * @author bennyl
 */
public class InvokeSubtituteBuilder extends BaseSubstituteBuilder<InvokeInstruction, InvokeSubtituteBuilder> {

    public InvokeSubtituteBuilder(WrapUnwrapService wuservice, MethodHolder processedMethod, ClassHolderSource classSource, InvokeInstruction instruction, List<Instruction> replacement) {
        super(wuservice, processedMethod, classSource, instruction, replacement);
        assignReceiver(instruction.getReceiver(), instruction.getMethod().getReturnType(), WrapMode.UNWRAP);
    }

    public InvokeSubtituteBuilder appendArgWrapped(int index) {
        return append(instruction.getArguments().get(index), instruction.getMethod().getParameterTypes()[index], WrapMode.WRAP);
    }
    
    public InvokeSubtituteBuilder appendArgListWrapped() {
        append("(");
        final int numArgs = instruction.getArguments().size();
        for (int i=0; i<numArgs; i++) {
            appendArgWrapped(i);
            if (i+1 < numArgs) {
                append(", ");
            }
        }
        append(")");
        return this;
    }
    
    public InvokeSubtituteBuilder appendInstance() {
        return append(instruction.getInstance(), ValueType.object(instruction.getMethod().getClassName()));
    }
    
}
