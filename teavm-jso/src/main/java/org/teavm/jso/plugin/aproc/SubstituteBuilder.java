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

import com.carrotsearch.hppc.ObjectIntMap;
import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import com.carrotsearch.hppc.cursors.ObjectIntCursor;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.teavm.jso.JSObject;
import org.teavm.model.CallLocation;
import org.teavm.model.ClassHolderSource;
import org.teavm.model.Instruction;
import org.teavm.model.InstructionLocation;
import org.teavm.model.MethodHolder;
import org.teavm.model.MethodReference;
import org.teavm.model.Program;
import org.teavm.model.ValueType;
import org.teavm.model.Variable;
import org.teavm.model.instructions.AssignInstruction;
import org.teavm.model.instructions.InvocationType;
import org.teavm.model.instructions.InvokeInstruction;
import org.teavm.model.instructions.StringConstantInstruction;

/**
 *
 * @author bennyl
 */
public class SubstituteBuilder {

    private StringBuilder expression = new StringBuilder();
    private ObjectIntMap<VariableWithType> variables = new ObjectIntOpenHashMap<>();
    private List<Instruction> replacement;
    private Program program;
    private Instruction originalInstruction;
    private WrapUnwrapService wuservice;
    private ClassHolderSource classSource;
    private MethodHolder methodToProcess;

    public SubstituteBuilder(WrapUnwrapService wuservice, MethodHolder processedMethod, ClassHolderSource classSource, Instruction originalInstruction, List<Instruction> replacement) {
        this.replacement = replacement;
        this.program = processedMethod.getProgram();
        this.originalInstruction = originalInstruction;
        this.wuservice = wuservice;
        this.classSource = classSource;
        this.methodToProcess = processedMethod;
    }

    public SubstituteBuilder append(String... constant) {
        for (String c : constant) {
            appendEscaped(c);
        }

        return this;
    }

    public SubstituteBuilder appendArgOf(InvokeInstruction i, int argIdx) {
        return append(i.getArguments().get(argIdx), i.getMethod().getParameterTypes()[argIdx]);
    }

    public SubstituteBuilder append(Variable v, ValueType type) {
        VariableWithType vt = new VariableWithType(v, type);
        int index = variables.getOrDefault(vt, -1);
        if (index < 0) {
            index = variables.size();
            variables.put(vt, index);
        }

        expression.append("$").append(index).append(" ");
        return this;
    }

    private void appendEscaped(String str) {
        int nextIndex = str.indexOf("$");
        if (nextIndex < 0) {
            expression.append(str);
        } else {
            int startIndex = 0;
            while (nextIndex >= 0) {
                expression.append(str, startIndex, nextIndex + 1).append("$");
                startIndex = nextIndex + 1;
                nextIndex = str.indexOf("$", nextIndex + 1);
            }
            expression.append(str, startIndex, str.length());
        }
    }

    public void createSubstitution(Variable receiver, ValueType type) {
        Variable result = receiver != null ? program.createVariable() : null;
        InvokeInstruction newInvoke = new InvokeInstruction();
        ValueType[] signature = new ValueType[variables.size() + 2];
        Arrays.fill(signature, ValueType.object(JSObject.class.getName()));

        newInvoke.setMethod(new MethodReference(Substituter.class.getName(), "substitute", signature));
        newInvoke.setType(InvocationType.SPECIAL);
        newInvoke.setReceiver(result);
        newInvoke.getArguments().add(addStringWrap(addString(expression.toString(), originalInstruction.getLocation()),
                originalInstruction.getLocation()));

        newInvoke.setLocation(originalInstruction.getLocation());
        CallLocation callLocation = new CallLocation(methodToProcess.getReference(), originalInstruction.getLocation());

        VariableWithType[] arguments = new VariableWithType[variables.size()];
        for (ObjectIntCursor<VariableWithType> v : variables) {
            arguments[v.value] = v.key;
        }

        for (int k = 0; k < arguments.length; ++k) {
            Variable arg = wuservice.wrap(arguments[k].var, arguments[k].type, callLocation.getSourceLocation());
            newInvoke.getArguments().add(arg);
        }

        replacement.add(newInvoke);
        if (result != null) {
            result = wuservice.unwrap(callLocation, result, type);
            addAssignmentInstruction(result, receiver, originalInstruction.getLocation());
        }
    }

    private Variable addString(String str, InstructionLocation location) {
        Variable var = program.createVariable();
        StringConstantInstruction nameInsn = new StringConstantInstruction();
        nameInsn.setReceiver(var);
        nameInsn.setConstant(str);
        nameInsn.setLocation(location);
        replacement.add(nameInsn);
        return var;
    }

    private Variable addStringWrap(Variable var, InstructionLocation location) {
        return wuservice.wrap(var, ValueType.object("java.lang.String"), location);
    }

    private static final class VariableWithType {

        Variable var;
        ValueType type;

        public VariableWithType(Variable var, ValueType type) {
            this.var = var;
            this.type = type;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + Objects.hashCode(this.var);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            final VariableWithType other = (VariableWithType) obj;
            return this.var == other.var;
        }

    }

    private void addAssignmentInstruction(Variable a, Variable b, InstructionLocation location) {
        AssignInstruction insn = new AssignInstruction();
        insn.setAssignee(a);
        insn.setReceiver(b);
        insn.setLocation(location);
        replacement.add(insn);
    }

}
