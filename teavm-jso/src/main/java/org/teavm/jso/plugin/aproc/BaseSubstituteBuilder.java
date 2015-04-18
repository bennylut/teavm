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
import org.teavm.model.instructions.EmptyInstruction;
import org.teavm.model.instructions.InvocationType;
import org.teavm.model.instructions.InvokeInstruction;
import org.teavm.model.instructions.StringConstantInstruction;

/**
 *
 * @author bennyl
 */
public class BaseSubstituteBuilder<I extends Instruction, T extends SubtituteBuilder> implements SubtituteBuilder<T> {

    private StringBuilder expression = new StringBuilder();
    private ObjectIntMap<VarInfo> variables = new ObjectIntOpenHashMap<>();
    private VarInfo receiver;

    protected List<Instruction> replacement;
    protected Program program;
    protected I instruction;
    protected WrapUnwrapService wuservice;
    protected ClassHolderSource classSource;
    protected MethodHolder processedMethod;

    public BaseSubstituteBuilder(WrapUnwrapService wuservice, MethodHolder processedMethod, ClassHolderSource classSource, I originalInstruction, List<Instruction> replacement) {
        this.replacement = replacement;
        this.program = processedMethod.getProgram();
        this.instruction = originalInstruction;
        this.wuservice = wuservice;
        this.classSource = classSource;
        this.processedMethod = processedMethod;
    }

    public I getInstruction() {
        return instruction;
    }

    @Override
    public T append(String... constant) {
        for (String c : constant) {
            appendEscaped(c);
        }

        return (T) this;
    }

    @Override
    public T append(Variable v, ValueType type, WrapMode wrap) {
        VarInfo vt = new VarInfo(v, type, wrap);
        int index = variables.getOrDefault(vt, -1);
        if (index < 0) {
            index = variables.size();
            variables.put(vt, index);
        }

        expression.append("$").append(index).append(" ");
        return (T) this;
    }

    public T appendWrappped(Variable v, ValueType type) {
        return append(v, type, WrapMode.WRAP);
    }

    public T appendUnwrapped(Variable v, ValueType type) {
        return append(v, type, WrapMode.UNWRAP);
    }

    public T append(Variable v, ValueType type) {
        return append(v, type, WrapMode.DO_NOTHING);
    }

    @Override
    public T assignReceiver(Variable receiver, ValueType type, WrapMode wrap) {
        this.receiver = new VarInfo(receiver, type, wrap);
        return (T) this;
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

    @Override
    public void substitute() {
        Variable result = receiver != null ? program.createVariable() : null;
        InvokeInstruction newInvoke = new InvokeInstruction();
        ValueType[] signature = new ValueType[variables.size() + 2];
        Arrays.fill(signature, ValueType.object(JSObject.class.getName()));

        newInvoke.setMethod(new MethodReference(Substituter.class.getName(), "substitute", signature));
        newInvoke.setType(InvocationType.SPECIAL);
        newInvoke.setReceiver(result);
        newInvoke.getArguments().add(addStringWrap(addString(expression.toString(), instruction.getLocation()),
                instruction.getLocation()));

        newInvoke.setLocation(instruction.getLocation());
        CallLocation callLocation = new CallLocation(processedMethod.getReference(), instruction.getLocation());

        VarInfo[] arguments = new VarInfo[variables.size()];
        for (ObjectIntCursor<VarInfo> v : variables) {
            arguments[v.value] = v.key;
        }

        for (int k = 0; k < arguments.length; ++k) {
            Variable arg;
            switch (arguments[k].wrap) {
                case WRAP:
                    arg = wuservice.wrap(arguments[k].var, arguments[k].type, callLocation.getSourceLocation());
                    break;
                case UNWRAP:
                    arg = wuservice.unwrap(callLocation, arguments[k].var, arguments[k].type);
                    break;
                case DO_NOTHING:
                    arg = arguments[k].var;
                    break;
                default:
                    throw new AssertionError(arguments[k].wrap.name());
                
            }
            newInvoke.getArguments().add(arg);
        }

        replacement.add(newInvoke);
        if (result != null) {
            switch (receiver.wrap) {
                case WRAP:
                    result = wuservice.wrap(result, receiver.type, callLocation.getSourceLocation());
                    break;
                case UNWRAP:
                    result = wuservice.unwrap(callLocation, result, receiver.type);
                    break;
                case DO_NOTHING:
                    break;
                default:
                    throw new AssertionError(receiver.wrap.name());
                
            }
            
            addAssignmentInstruction(result, receiver.var, instruction.getLocation());
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

    @Override
    public void substituteWithEmptyInstruction() {
        replacement.clear();
        replacement.add(new EmptyInstruction());
    }

    private static final class VarInfo {

        Variable var;
        ValueType type;
        WrapMode wrap;

        public VarInfo(Variable var, ValueType type, WrapMode wrap) {
            this.var = var;
            this.type = type;
            this.wrap = wrap;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + Objects.hashCode(this.var);
            hash = 79 * hash + Objects.hashCode(this.wrap);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            final VarInfo other = (VarInfo) obj;
            return this.var == other.var && this.wrap == other.wrap;
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
