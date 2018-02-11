/**
 * The MIT License (MIT)
 *
 * MSUSEL CodeTree
 * Copyright (c) 2015-2017 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package edu.montana.gsoc.msusel.codetree.node

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
enum Modifiers {

    // Java
    STATIC,
    FINAL,
    ABSTRACT,
    NATIVE,
    STRICTFP,
    SYNCHRONIZED,
    TRANSIENT,
    VOLATILE,
    DEFAULT,
    
    // C# Specific
    ASYNC,
    CONST,
    EXTERN,
    READONLY,
    SEALED,
    UNSAFE,
    VIRTUAL,
    OUT,
    REF,
    PARAMS,
    OVERRIDE,
    NEW,
    PARTIAL,
    EXPLICIT,
    IMPLICIT,
    YIELD,
    THIS
    
    static Modifiers valueForJava(String s) {
        switch(s) {
            case 'readonly': return FINAL
            case 'final': return FINAL
            case 'static': return STATIC
            case 'abstract': return ABSTRACT
            case 'native': return NATIVE
            case 'strictfp': return STRICTFP
            case 'synchronized': return SYNCHRONIZED
            case 'transient': return TRANSIENT
            case 'volatile': return VOLATILE
            case 'default': return DEFAULT
            default: return null
        }
    }
    
    static Modifiers valueForCSharp(String s) {
        switch(s) {
            case 'final': return FINAL
            case 'static': return STATIC
            case 'abstract': return ABSTRACT
            case 'synchronized': return SYNCHRONIZED
            case 'transient': return TRANSIENT
            case 'volatile': return VOLATILE
            case 'default': return DEFAULT
            case 'async': return ASYNC
            case 'const': return CONST
            case 'extern': return EXTERN
            case 'readOnly': return READONLY
            case 'sealed': return SEALED
            case 'unsafe': return UNSAFE
            case 'virtual': return VIRTUAL
            case 'out': return OUT
            case 'ref': return REF
            case 'params': return PARAMS
            case 'override': return OVERRIDE
            case 'new': return NEW
            case 'partial': return PARTIAL
            case 'explicit': return EXPLICIT
            case 'implicit': return IMPLICIT
            case 'yield': return YIELD
            case 'this': return THIS
        }
    }
}
