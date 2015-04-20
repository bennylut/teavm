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
package org.teavm.jso.plugin.util;

import org.teavm.jso.plugin.jsc.ConversionMode;
import com.carrotsearch.hppc.ObjectIntMap;
import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import com.carrotsearch.hppc.cursors.ObjectIntCursor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.teavm.diagnostics.Diagnostics;
import org.teavm.jso.JSObject;
import org.teavm.jso.plugin.jsc.JsConversionDriver;
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
public class ReusableSubstitutionBuilder<T extends SubstituteBuilder<T>> implements SubstituteBuilder<T> {

    private StringBuilder expression = new StringBuilder();
    private ObjectIntMap<VarInfo> variables = new ObjectIntOpenHashMap<>();
    private VarInfo receiver;

    private List<Instruction> replacement;
    private Program program;
    private Instruction instruction;
    private JsConversionDriver wuDriver;
    private ClassHolderSource classSource;
    private MethodHolder processedMethod;
    private Diagnostics diagnostics;

    public ReusableSubstitutionBuilder(JsConversionDriver wuDriver, MethodHolder processedMethod, ClassHolderSource classSource, Diagnostics diagnostics) {
        this.replacement = new ArrayList<>();
        this.program = processedMethod.getProgram();
        this.instruction = null;
        this.wuDriver = wuDriver;
        this.classSource = classSource;
        this.processedMethod = processedMethod;
        this.diagnostics = diagnostics;
    }

    @Override
    public Instruction getInstruction() {
        return instruction;
    }

    public List<Instruction> getSubstitution() {
        return replacement;
    }

    public void reset(Instruction instruction) {
        this.instruction = instruction;

        replacement.clear();
    }

    @Override
    public T append(String... constant) {
        for (String c : constant) {
            appendEscaped(c);
        }

        return (T) this;
    }

    @Override
    public T append(Variable v, ValueType type, ConversionMode wrap) {
        VarInfo vt = new VarInfo(v, type, wrap);
        int index = variables.getOrDefault(vt, -1);
        if (index < 0) {
            index = variables.size();
            variables.put(vt, index);
        }

        expression.append("$").append(index).append(" ");
        return (T) this;
    }

    @Override
    public T appendWrappped(Variable v, ValueType type) {
        return append(v, type, ConversionMode.TO_JS);
    }

    @Override
    public T appendUnwrapped(Variable v, ValueType type) {
        return append(v, type, ConversionMode.TO_JAVA);
    }

    @Override
    public T append(Variable v) {
        return append(v,
                null, //If no conversion is made than the type is not important
                ConversionMode.DO_NOTHING);
    }

    @Override
    public T assignReceiver(Variable receiver, ValueType type, ConversionMode wrap) {
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

    public void finalizeInstruction() {
        Variable result = receiver != null ? program.createVariable() : null;
        InvokeInstruction newInvoke = new InvokeInstruction();
        CallLocation callLocation = new CallLocation(processedMethod.getReference(), instruction.getLocation());

        ValueType[] signature = new ValueType[variables.size() + 2];
        Arrays.fill(signature, ValueType.object(JSObject.class.getName()));

        newInvoke.setMethod(new MethodReference(Substituter.class.getName(), "substitute", signature));
        newInvoke.setType(InvocationType.SPECIAL);
        newInvoke.setReceiver(result);
        newInvoke.getArguments().add(addStringWrap(addString(expression.toString(), instruction.getLocation()), callLocation));
        newInvoke.setLocation(instruction.getLocation());

        VarInfo[] arguments = new VarInfo[variables.size()];
        for (ObjectIntCursor<VarInfo> v : variables) {
            arguments[v.value] = v.key;
        }

        for (int k = 0; k < arguments.length; ++k) {
            try {
                Variable arg;
                switch (arguments[k].wrap) {
                    case TO_JS:
                        arg = program.createVariable();
                        wuDriver.toJs(arguments[k].var, arg, arguments[k].type, replacement);
                        break;
                    case TO_JAVA:
                        arg = program.createVariable();
                        wuDriver.toJava(arguments[k].var, arg, arguments[k].type, replacement);
                        break;
                    case DO_NOTHING:
                        arg = arguments[k].var;
                        break;
                    default:
                        throw new AssertionError(arguments[k].wrap.name());

                }
                newInvoke.getArguments().add(arg);
            } catch (Exception ex) {
                diagnostics.error(callLocation, "error while attempting to {{1}}  object: {{2}}", arguments[k].wrap.name(), ex.getMessage());
            }
        }

        replacement.add(newInvoke);
        if (result != null) {
            try {
                Variable temp = program.createVariable();
                switch (receiver.wrap) {
                    case TO_JS:
                        wuDriver.toJs(result, temp, receiver.type, replacement);
                        result = temp;
                        break;
                    case TO_JAVA:
                        wuDriver.toJava(result, temp, receiver.type, replacement);
                        result = temp;
                        break;
                    case DO_NOTHING:
                        break;

                }

                addAssignmentInstruction(result, receiver.var, instruction.getLocation());
            } catch (Exception ex) {
                diagnostics.error(callLocation, "error while attempting to {{1}}  object: {{2}}", receiver.wrap.name(), ex.getMessage());
            }
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

    private Variable addStringWrap(Variable var, CallLocation location) {
        try {
            Variable v = program.createVariable();
            wuDriver.toJs(var, v, ValueType.object("java.lang.String"), replacement);
            return v;
        } catch (Exception ex) {
            diagnostics.error(location, "error while attempting to wrap string constant: {{1}}", ex.getMessage());
            return null;
        }
    }

    @Override
    public void substituteWithEmptyInstruction() {
        replacement.clear();
        replacement.add(new EmptyInstruction());
    }

    private static final class VarInfo {

        Variable var;
        ValueType type;
        ConversionMode wrap;

        public VarInfo(Variable var, ValueType type, ConversionMode wrap) {
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
