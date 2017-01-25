/**
 * The MIT License (MIT)
 * 
 * Sonar Understand Plugin
 * Copyright (c) 2015 Isaac Griffith, SiliconCode, LLC
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
package com.sparqline.quamoco;

/**
 * MetricNames -
 * 
 * @author Isaac Griffith
 *
 */
public interface MetricNames {

    public static final String MAXNESTING = "MaxNesting";
    public static final String NOP        = "CountDeclProperty";                  // => NumFields
    public static final String NCV        = "CountDeclClassVariable";        // => NumClassFields
    public static final String NIV        = "CountDeclInstanceVariable";  // => NumVariables/NumFields
    public static final String NOV        = "CountDeclInstanceVariable";
    public static final String NOF        = "CountDeclInstanceVariable"; //"CountDeclClassVariable";
    public static final String NOS        = "CountStmt";                                  // => NumStatements
    public static final String NC         = "CountDeclClass";                        // => NumClasses
    public static final String NOC        = "CountDeclClass";                        // => NumClasses
    public static final String NOT        = "CountDeclClass";                        // => NumTypes
    public static final String LOC        = "CountLineCode";
    public static final String RCC        = "RatioCommentToCode";
    public static final String NOM        = "CountDeclMethodAll";                // => NumMethods
}
