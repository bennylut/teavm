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
import org.teavm.jso.plugin.util.SubstituteBuilder;
import org.teavm.model.Instruction;
import org.teavm.model.ValueType;
import org.teavm.model.Variable;

/**
 *
 * @author bennyl
 */
public class DelegatingSubstituteBuilder<I extends Instruction, T extends DelegatingSubstituteBuilder<I, T>> implements SubstituteBuilder<T> {

    private SubstituteBuilder s;

    public DelegatingSubstituteBuilder(SubstituteBuilder s) {
        this.s = s;
    }

    @Override
    public T append(String... constant) {
        s.append(constant);
        return (T) this;
    }

    @Override
    public T append(Variable v, ValueType type, ConversionMode wrapMode) {
        s.append(v, type, wrapMode);
        return (T) this;
    }

    @Override
    public T assignReceiver(Variable receiver, ValueType type, ConversionMode wrap) {
        s.assignReceiver(receiver, type, wrap);
        return (T) this;
    }

    @Override
    public T appendWrappped(Variable v, ValueType type) {
        s.appendWrappped(v, type);
        return (T) this;
    }

    @Override
    public T appendUnwrapped(Variable v, ValueType type) {
        s.appendUnwrapped(v, type);
        return (T) this;
    }

    @Override
    public T append(Variable v) {
        s.append(v);
        return (T) this;
    }

    @Override
    public I getInstruction() {
        return (I) s.getInstruction();
    }

    @Override
    public void substituteWithEmptyInstruction() {
        s.substituteWithEmptyInstruction();
    }

}
