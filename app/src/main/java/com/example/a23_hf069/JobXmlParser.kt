//package com.example.a23_hf069
//
//import org.xmlpull.v1.XmlPullParser
//import org.xmlpull.v1.XmlPullParserException
//import org.xmlpull.v1.XmlPullParserFactory
//import java.io.StringReader
//
//class JobXmlParser {
//    private enum class TagType {
//        NONE, TOTAL, ClCDNM, NAME, CODE
//    }
//
//    private val FAULT_RESULT = "faultResult"
//    private val TOTAL_TAG = "total"
//    private val ClCDNM_TAG = "jobClcdNM"
//    private val NAME_TAG = "jobNm"
//    private val NAMES_TAG = "jobsNm"
//    private val CODE_TAG = "jobCd"
//    private val CODES_TAG = "jobsCd"
//    private val ONEDEPTH_TAG = "oneDepth"
//    private val TWODEPTH_TAG = "twoDepth"
//
//    private var parser: XmlPullParser? = null
//
//    fun JobXmlParser() {
//        var factory: XmlPullParserFactory? = null
//        try {
//            factory = XmlPullParserFactory.newInstance()
//            parser = factory.newPullParser()
//        } catch (e: XmlPullParserException) {
//            e.printStackTrace()
//        }
//    }
//
//    fun parse1(xml: String?): List<Job?>? {
//        val resultList: MutableList<Job?> = ArrayList()
//        var tagType = TagType.NONE //  태그를 구분하기 위한 enum 변수 초기화
//        var j: Job? = null
//        try {
//            parser!!.setInput(StringReader(xml))
//            var eventType = parser!!.eventType
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                when (eventType) {
//                    XmlPullParser.START_DOCUMENT -> {}
//                    XmlPullParser.START_TAG -> {
//                        val tag = parser!!.name
//                        if (tag == ClCDNM_TAG) {
//                            j = Job()
//                            tagType = TagType.ClCDNM
//                        } else if (tag == NAME_TAG) {
//                            tagType = TagType.NAME
//                        } else if (tag == CODE_TAG) {
//                            tagType = TagType.CODE
//                        } else if (tag == FAULT_RESULT) {
//                            return null
//                        }
//                    }
//                    XmlPullParser.END_TAG -> if (parser!!.name == NAME_TAG) {
//                        resultList.add(j)
//                        j = null
//                    }
//                    XmlPullParser.TEXT -> {
//                        val text = parser!!.text
//                        when (tagType) {
//                            TagType.ClCDNM -> j!!.setJobClcdNM(text)
//                            TagType.NAME -> j!!.setJobNm(text)
//                            TagType.CODE -> j!!.setJobCd(text)
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
//
//    fun parse2(xml: String?): String? {
//        var total: String? = null
//        var tagType = TagType.NONE //  태그를 구분하기 위한 enum 변수 초기화
//        try {
//            parser!!.setInput(StringReader(xml))
//            var eventType = parser!!.eventType
//            while (eventType != XmlPullParser.END_DOCUMENT && total == null) {
//                when (eventType) {
//                    XmlPullParser.START_DOCUMENT -> {}
//                    XmlPullParser.START_TAG -> {
//                        val tag = parser!!.name
//                        if (tag == TOTAL_TAG) {
//                            tagType = TagType.TOTAL
//                        }
//                    }
//                    XmlPullParser.END_TAG -> {}
//                    XmlPullParser.TEXT -> {
//                        when (tagType) {
//                            TagType.TOTAL -> total = parser!!.text
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
//        return total
//    }
//
//    fun parse3(xml: String?): List<Job?>? {
//        val resultList: MutableList<Job?> = ArrayList()
//        var j: Job? = null
//        var oneDepth = true
//        var tagType = TagType.NONE //  태그를 구분하기 위한 enum 변수 초기화
//        try {
//            parser!!.setInput(StringReader(xml))
//            var eventType = parser!!.eventType
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                when (eventType) {
//                    XmlPullParser.START_DOCUMENT -> {}
//                    XmlPullParser.START_TAG -> {
//                        val tag = parser!!.name
//                        if (tag == ONEDEPTH_TAG) {
//                            j = Job()
//                        } else if (tag ==TWODEPTH_TAG) {
//                            oneDepth = false
//                        } else if (tag == CODES_TAG && j != null && oneDepth == true) {
//                            tagType = TagType.CODE
//                        } else if (tag == NAMES_TAG && j != null && oneDepth == true) {
//                            tagType = TagType.NAME
//                        } else if (tag == FAULT_RESULT) {
//                            return null
//                        }
//                    }
//                    XmlPullParser.END_TAG -> if (parser!!.name == ONEDEPTH_TAG) {
//                        resultList.add(j)
//                        j = null
//                        oneDepth = true
//                    }
//                    XmlPullParser.TEXT -> {
//                        val text = parser!!.text
//                        when (tagType) {
//                            TagType.CODE -> j!!.setJobCd(text)
//                            TagType.NAME -> j!!.setJobNm(text)
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