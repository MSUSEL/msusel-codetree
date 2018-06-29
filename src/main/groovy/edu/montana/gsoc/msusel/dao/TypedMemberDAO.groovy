package edu.montana.gsoc.msusel.dao

import edu.montana.gsoc.msusel.datamodel.member.TypedMember

interface TypedMemberDAO<T extends TypedMember> extends MemberDAO<T> {

}