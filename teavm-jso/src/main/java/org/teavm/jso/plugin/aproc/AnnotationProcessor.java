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

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import org.teavm.model.CallLocation;
import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderSource;
import org.teavm.model.FieldHolder;
import org.teavm.model.Instruction;
import org.teavm.model.MethodHolder;
import org.teavm.model.Variable;
import org.teavm.model.instructions.ConstructInstruction;
import org.teavm.model.instructions.GetFieldInstruction;
import org.teavm.model.instructions.InvokeInstruction;
import org.teavm.model.instructions.PutFieldInstruction;

/**
 *
 * @author bennyl
 */
public interface AnnotationProcessor {

    /**
     *
     * @return a set of annotation derivations which should be handled by this
     * annotation processor
     */
    Set<Class<? extends Annotation>> supportedAnnotations();

    /**
     * initialize this annotation processor, this method is the first method
     * that will get invoked on the annotation processor
     *
     * @param source
     */
    void initialize(ClassHolderSource source);

    /**
     * executed upon the first encounter with a class definition which is
     * annotated with a supported annotation this method allows to transform the
     * class definition.
     *
     * @param annotation
     * @param cls
     */
    void transformAnnotatedClass(Annotation annotation, ClassHolder cls);

    /**
     * executed upon the first encounter with a method definition which is
     * annotated with a supported annotation this method allows to transform the
     * method definition.
     *
     * @param annotation
     * @param mtd
     */
    void transformAnnotatedMethod(Annotation annotation, MethodHolder mtd);

    /**
     * executed upon the first encounter with a field definition which is
     * annotated with a supported annotation this method allows to transform the
     * field definition.
     *
     * @param annotation
     * @param field
     */
    void transformAnnotatedField(Annotation annotation, FieldHolder field);

    /**
     * executed when a construct instruction of a class which annotated with a
     * supported annotation is encountered
     *
     * @param cls the annotated class being constructed
     * @param instruction the construct instruction
     * @param replacement a list of instructions, initially empty, if any
     * instruction is added to this list it will substitute the original
     * instruction
     */
    void substituteConstructorCall(ClassHolder cls, ConstructInstruction instruction, List<Instruction> replacement);

    /**
     * executed when an invocation of a method belongs to a class which
     * annotated with a supported annotation is encountered
     *
     * @param cls the annotated class which own the method being invoked
     * @param instruction the invocation instruction
     * @param replacement a list of instructions, initially empty, if any
     * instruction is added to this list it will substitute the original
     * instruction
     */
    void substituteMethodInvocation(ClassHolder cls, InvokeInstruction instruction, List<Instruction> replacement);

    /**
     * executed when an invocation of a method which annotated with a supported
     * annotation is encountered
     *
     * @param mtd the annotated method being invoked
     * @param instruction the invocation instruction
     * @param replacement a list of instructions, initially empty, if any
     * instruction is added to this list it will substitute the original
     * instruction
     */
    void substituteMethodInvocation(MethodHolder mtd, InvokeInstruction instruction, List<Instruction> replacement);

    /**
     * executed when a put-operation of a field of a class which annotated with
     * a supported annotation is encountered
     *
     * @param cls the annotated class which its field is being put into
     * @param instruction the put instruction
     * @param replacement a list of instructions, initially empty, if any
     * instruction is added to this list it will substitute the original
     * instruction
     */
    void substituteFieldPut(ClassHolder cls, PutFieldInstruction instruction, List<Instruction> replacement);

    /**
     * executed when a get-operation of a field of a class which annotated with
     * a supported annotation is encountered
     *
     * @param cls the annotated class which its field is being read from
     * @param instruction the get instruction
     * @param replacement a list of instructions, initially empty, if any
     * instruction is added to this list it will substitute the original
     * instruction
     */
    void substituteFieldGet(ClassHolder cls, GetFieldInstruction instruction, List<Instruction> replacement);

    /**
     * executed when a put-operation of a field which annotated with a supported
     * annotation is encountered
     *
     * @param field the annotated field being put into
     * @param instruction the put instruction
     * @param replacement a list of instructions, initially empty, if any
     * instruction is added to this list it will substitute the original
     * instruction
     */
    void substituteFieldPut(FieldHolder field, PutFieldInstruction instruction, List<Instruction> replacement);

    /**
     * executed when a get-operation of a field which annotated with a supported
     * annotation is encountered
     *
     * @param field the annotated field being read from
     * @param instruction the get instruction
     * @param replacement a list of instructions, initially empty, if any
     * instruction is added to this list it will substitute the original
     * instruction
     */
    void substituteFieldGet(FieldHolder field, GetFieldInstruction instruction, List<Instruction> replacement);

    /**
     * wrap (i.e., transform to native javascript representation) a variable of
     * a type annotated with a supported annotation
     *
     * @param cls the annotated class requiring the transformation
     * @param source the source variable which holds an instance of the the
     * annotated class
     * @param target a target variable that should hold the result of the
     * transformation
     * @param wrappingInstructions list of instructions to add to the program,
     * this instructions should actually do the transformation
     */
    void wrap(ClassHolder cls, Variable source, Variable target, List<Instruction> wrappingInstructions);

    /**
     * unwrap (i.e., transform a native javascript representation into managed
     * java representation) a variable of a native javascript type into an
     * instance of the class annotated with a supported annotation
     *
     * @param cls the annotated class requiring the transformation
     * @param source the source variable which holds a native javascript value
     * @param target a target variable that should hold the result of the
     * transformation
     * @param wrappingInstructions list of instructions to add to the program,
     * this instructions should actually do the transformation
     */
    void unwrap(ClassHolder cls, CallLocation location, Variable source, Variable target, List<Instruction> wrappingInstructions);
}
