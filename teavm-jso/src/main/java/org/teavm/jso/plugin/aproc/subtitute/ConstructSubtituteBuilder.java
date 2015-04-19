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

import org.teavm.model.instructions.ConstructInstruction;

/**
 *
 * @author bennyl
 */
public class ConstructSubtituteBuilder extends DelegatingSubtituteBuilder<ConstructInstruction, ConstructSubtituteBuilder> {

    public ConstructSubtituteBuilder(SubtituteBuilder s) {
        super(s);
    }

}
