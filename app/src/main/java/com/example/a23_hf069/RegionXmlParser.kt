//package com.example.a23_hf069
//
//import org.xmlpull.v1.XmlPullParser
//import org.xmlpull.v1.XmlPullParserException
//import org.xmlpull.v1.XmlPullParserFactory
//import java.io.StringReader
//
//class RegionXmlParser {
//    private enum class TagType {
//        NONE, SUPERNM, SUBNM, SUPERCD, SUBCD
//    }
//
//    private val FAULT_RESULT = "faultResult"
//    private val ITEM_TAG1 = "oneDepth"
//    private val ITEM_TAG2 = "twoDepth"
//    private val NAME_TAG = "regionNm"
//    private val CODE_TAG = "regionCd"
//    private val SUPERCD_TAG = "superCd"
//
//    private var parser: XmlPullParser? = null
//
//    fun RegionXmlParser() {
//        var factory: XmlPullParserFactory? = null
//        try {
//            factory = XmlPullParserFactory.newInstance()
//            parser = factory.newPullParser()
//        } catch (e: XmlPullParserException) {
//            e.printStackTrace()
//        }
//    }
//
//    fun parse1(xml: String?): List<Region?>? {
//        val resultList: MutableList<Region?> = ArrayList()
//        val subRList: MutableList<Region> = ArrayList()
//        var sup: Region? = null
//        var sub: Region? = null
//        var tagType = TagType.NONE //  태그를 구분하기 위한 enum 변수 초기화
//        try {
//            parser!!.setInput(StringReader(xml))
//            var eventType = parser!!.eventType
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                when (eventType) {
//                    XmlPullParser.START_DOCUMENT -> {}
//                    XmlPullParser.START_TAG -> {
//                        val tag = parser!!.name
//                        if (tag == ITEM_TAG1) {
//                            sup = Region()
//                        } else if (tag == ITEM_TAG2) {
//                            sub = Region()
//                        } else if (tag == CODE_TAG) {
//                            if (sup != null && sub == null) {
//                                tagType = TagType.SUPERCD
//                            } else if (sup != null && sub != null) {
//                                tagType = TagType.SUBCD
//                            }
//                        } else if (tag == NAME_TAG) {
//                            if (sup != null && sub == null) {
//                                tagType = TagType.SUPERNM
//                            } else if (sup != null && sub != null) {
//                                tagType = TagType.SUBNM
//                            }
//                        } else if (tag == FAULT_RESULT) {
//                            return null
//                        }
//                    }
//                    XmlPullParser.END_TAG -> if (parser!!.name == ITEM_TAG1) {
//                        resultList.add(sup)
//                        subRList.clear()
//                        sup = null
//                    } else if (parser!!.name == ITEM_TAG2) {
//                        sup!!.getrList().add(sub)
//                        sub = null
//                    }
//                    XmlPullParser.TEXT -> {
//                        val text = parser!!.text
//                        when (tagType) {
//                            TagType.SUPERCD -> sup!!.setSuperCd(text)
//                            TagType.SUPERNM -> sup!!.setName(text)
//                            TagType.SUBCD -> sub!!.setCode(text)
//                            TagType.SUBNM -> sub!!.setName(text)
//                            else -> {}
//                        }
//                        tagType = TagType.NONE
//                    }
//                }
//                eventType = parser!!.next()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return resultList
//    }
//}