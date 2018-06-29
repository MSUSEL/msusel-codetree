package edu.montana.gsoc.msusel.dao

import edu.montana.gsoc.msusel.datamodel.member.Member
import edu.montana.gsoc.msusel.datamodel.type.Type

interface MemberDAO<T extends Member> extends ComponentDAO<T> {

    Type findParent(T t)
}