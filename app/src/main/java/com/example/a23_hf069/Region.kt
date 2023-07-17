//package com.example.a23_hf069
//
//class Region {
//    private var name: String? = null
//    private var code: String? = null
//    private var superCd: String? = null
//    private var rList: MutableList<Region> = ArrayList()
//
//    constructor() {
//        rList = ArrayList<Region>()
//    }
//
//    fun getName(): String? {
//        return name
//    }
//
//    fun setName(name: String?) {
//        this.name = name
//    }
//
//    fun getCode(): String? {
//        return code
//    }
//
//    fun setCode(code: String?) {
//        this.code = code
//    }
//
//    fun getSuperCd(): String? {
//        return superCd
//    }
//
//    fun setSuperCd(superCd: String?) {
//        this.superCd = superCd
//    }
//
//    fun getrList(): List<Region> {
//        return rList
//    }
//
//    fun setrList(rList: List<Region>) {
//        this.rList.addAll(rList)
//    }
//
//    override fun toString(): String {
//        return name ?: ""
//    }
//}