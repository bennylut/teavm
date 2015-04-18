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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.teavm.jso.JSConstructor;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSMethod;

/**
 *
 * @author bennyl
 */
public class JSObjectRelatedAnnotationProcessor extends AbstractAnnotationProcessor {

    @Override
    public Set<String> supportedAnnotations() {
        return new HashSet<>(Arrays.asList(
                JSConstructor.class.getCanonicalName(), 
                JSFunctor.class.getCanonicalName(),
                JSIndexer.class.getCanonicalName(),
                JSMethod.class.getCanonicalName()
                ));
    }
    
}
