/*
 *  Copyright 2015 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.javascript;

/**
 *
 * @author Alexey Andreev
 */
public enum Priority {
    COMMA,
    ASSIGNMENT,
    CONDITIONAL,
    LOGICAL_OR,
    LOGICAL_AND,
    BITWISE_OR,
    BITWISE_XOR,
    BITWISE_AND,
    EQUALITY,
    COMPARISON,
    BITWISE_SHIFT,
    ADDITION,
    MULTIPLICATION,
    UNARY,
    FUNCTION_CALL,
    MEMBER_ACCESS,
    GROUPING
}
