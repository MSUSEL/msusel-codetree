/**
 * The MIT License (MIT)
 *
 * SparQLine Code Tree
 * Copyright (c) 2015-2017 Isaac Griffith, SparQLine Analytics, LLC
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
package edu.montana.gsoc.msusel.node;

/**
 * A Simple abstraction of a Type or Method parameter. It includes a Type and
 * Variable name along with whether it represents a collection or not.
 * 
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class Parameter {

    /**
     * The context unique name of the parameter
     */
    private String  name;
    /**
     * The qualified identifier of the type
     */
    private String  typeRef;
    /**
     * Boolean flag indicating that this is a collection, array, or hash
     */
    private boolean collection;

    /**
     * Constructs a new Parameter with the given variable name and Qualified
     * Identifier for the associated type.
     * 
     * @param name
     *            Variable Name
     * @param type
     *            Qualified Identifier of the associated type.
     */
    public Parameter(String name, String type)
    {
        this(name, type, false);
    }

    /**
     * Constructs a new Parameter with the given variable name, Qualified
     * Identifier of the associated type, and a boolean flag indicating whether
     * it is a collection.
     * 
     * @param name
     *            Variable Name
     * @param type
     *            Qualified Identifier of the associated type
     * @param collection
     *            boolean flag indicating whether this is a collection variable
     *            or not
     */
    public Parameter(String name, String type, boolean collection)
    {
        this.name = name;
        this.typeRef = type;
        this.collection = collection;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the typeRef
     */
    public String getTypeRef()
    {
        return typeRef;
    }

    /**
     * @param typeRef
     *            the typeRef to set
     */
    public void setTypeRef(String typeRef)
    {
        this.typeRef = typeRef;
    }

    /**
     * @return the collection
     */
    public boolean isCollection()
    {
        return collection;
    }

    /**
     * @param collection
     *            the collection to set
     */
    public void setCollection(boolean collection)
    {
        this.collection = collection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "Parameter [name=" + name + ", typeRef=" + typeRef + ", collection=" + collection + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((typeRef == null) ? 0 : typeRef.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Parameter other = (Parameter) obj;
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!name.equals(other.name))
        {
            return false;
        }
        if (typeRef == null)
        {
            if (other.typeRef != null)
            {
                return false;
            }
        }
        else if (!typeRef.equals(other.typeRef))
        {
            return false;
        }
        return true;
    }

}
