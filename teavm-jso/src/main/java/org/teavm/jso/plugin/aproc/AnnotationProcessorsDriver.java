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

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import org.teavm.model.AnnotationHolder;
import org.teavm.model.ClassHolder;

/**
 *
 * @author bennyl
 */
public class AnnotationProcessorsDriver {

    private Map<String, AnnotationProcessor> procMap = new HashMap<>();
    
    public AnnotationProcessorsDriver() {
    }
    
    public void loadRegisteredProcessors(){
        ServiceLoader<AnnotationProcessor> processors = ServiceLoader.load(AnnotationProcessor.class);
        for (AnnotationProcessor proc : processors) {
            for (String a : proc.supportedAnnotations()) {
                if (procMap.put(a, proc) != null) {
                    throw new UnsupportedOperationException("multiple annotation processors for the same annotation is not supported");
                }
            }
        }
    }
    
    public void applyTransformations(ClassHolder cls) {
        //TODO: should validate 
        for (AnnotationHolder a : cls.getAnnotations().all()) {
            
        }
    }
    
    public void enterProgram() {
        
    }
    
    public void processInvocation() {
        
    }
    
    public void processConstructorCall() {
        
    }
    
    public void processFieldPut(){
        
    }
    
    public void processFieldGet(){
        
    }
    
}
