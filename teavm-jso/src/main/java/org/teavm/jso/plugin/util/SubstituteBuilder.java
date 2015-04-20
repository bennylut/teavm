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
import org.teavm.model.Instruction;
import org.teavm.model.ValueType;
import org.teavm.model.Variable;
import org.teavm.model.instructions.EmptyInstruction;

/**
 *
 * @author bennyl
 */
public interface SubstituteBuilder<T extends SubstituteBuilder<T>> {

    T append(String... constant);
    
    T append(Variable v, ValueType type, ConversionMode wrapMode);

    T assignReceiver(Variable receiver, ValueType type, ConversionMode wrap);
    
    T appendWrappped(Variable v, ValueType type);

    T appendUnwrapped(Variable v, ValueType type) ;

    T append(Variable v);
    
    Instruction getInstruction();
    

    /**
     * removes the substitute method, i.e., replacing it with an
     * {@link EmptyInstruction}
     */
    void substituteWithEmptyInstruction();

    
}
