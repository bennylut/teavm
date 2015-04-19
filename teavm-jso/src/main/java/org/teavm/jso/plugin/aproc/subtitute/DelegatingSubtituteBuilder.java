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
package org.teavm.jso.plugin.aproc.subtitute;

import org.teavm.model.Instruction;
import org.teavm.model.ValueType;
import org.teavm.model.Variable;

/**
 *
 * @author bennyl
 */
public class DelegatingSubtituteBuilder<I extends Instruction, T extends DelegatingSubtituteBuilder<I, T>> implements SubtituteBuilder<T> {

    private SubtituteBuilder s;

    public DelegatingSubtituteBuilder(SubtituteBuilder s) {
        this.s = s;
    }

    @Override
    public T append(String... constant) {
        s.append(constant);
        return (T) this;
    }

    @Override
    public T append(Variable v, ValueType type, WrapMode wrapMode) {
        s.append(v, type, wrapMode);
        return (T) this;
    }

    @Override
    public T assignReceiver(Variable receiver, ValueType type, WrapMode wrap) {
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
    public T append(Variable v, ValueType type) {
        s.append(v, type);
        return (T) this;
    }

    @Override
    public I getInstruction() {
        return (I) s.getInstruction();
    }

    @Override
    public void substitute() {
        s.substitute();
    }

    @Override
    public void substituteWithEmptyInstruction() {
        s.substituteWithEmptyInstruction();
    }

}
