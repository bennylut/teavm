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
import org.teavm.diagnostics.Diagnostics;
import org.teavm.model.CallLocation;
import org.teavm.model.ClassHolder;
import org.teavm.model.ClassReaderSource;
import org.teavm.model.FieldHolder;
import org.teavm.model.Instruction;
import org.teavm.model.MethodHolder;
import org.teavm.model.Variable;
import org.teavm.model.instructions.GetFieldInstruction;
import org.teavm.model.instructions.PutFieldInstruction;

/**
 *
 * @author bennyl
 */
public abstract class AbstractAnnotationProcessor implements AnnotationProcessor {

    @Override
    public void transformAnnotatedClass(ClassHolder cls, ClassReaderSource innerSource, Diagnostics diagnostics) {
    }

    @Override
    public void transformAnnotatedMethod(MethodHolder mtd, ClassReaderSource innerSource, Diagnostics diagnostics) {
    }

    @Override
    public void transformAnnotatedField(FieldHolder field, ClassReaderSource innerSource, Diagnostics diagnostics) {
    }

    @Override
    public void substituteConstructorCall(ClassHolder cls, ConstructSubtituteBuilder s) {
    }

    @Override
    public void substituteMethodInvocation(ClassHolder cls, InvokeSubtituteBuilder s) {
    }

    @Override
    public void substituteMethodInvocation(MethodHolder mtd, InvokeSubtituteBuilder s) {
    }

    @Override
    public void substituteFieldPut(ClassHolder cls, PutFieldInstruction instruction, List<Instruction> replacement) {
    }

    @Override
    public void substituteFieldGet(ClassHolder cls, GetFieldInstruction instruction, List<Instruction> replacement) {
    }

    @Override
    public void substituteFieldPut(FieldHolder field, PutFieldInstruction instruction, List<Instruction> replacement) {
    }

    @Override
    public void substituteFieldGet(FieldHolder field, GetFieldInstruction instruction, List<Instruction> replacement) {
    }

    @Override
    public void wrap(ClassHolder cls, Variable source, Variable target, List<Instruction> wrappingInstructions) {
    }

    @Override
    public void unwrap(ClassHolder cls, CallLocation location, Variable source, Variable target, List<Instruction> unwrappingInstructions) {
    }

}
