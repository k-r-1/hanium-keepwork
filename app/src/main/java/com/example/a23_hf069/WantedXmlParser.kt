//package com.example.a23_hf069
//
//import org.xmlpull.v1.XmlPullParser
//import org.xmlpull.v1.XmlPullParserException
//import org.xmlpull.v1.XmlPullParserFactory
//import java.io.StringReader
//
//class WantedXmlParser {
//    private enum class TagType {
//        NONE, TOTAL, COMPANY, TITLE, SALTPNM, SAL, REGION, MINEDUBG, CAREER, CLOSEDT, INFOURL, ADDR
//    }
//
//    //채용공고 상세보기 정보 =  급여, 모집인원, 고용형태, 근무지, 직무명, 담당업무, 경력사항, 최종학력
//    //필요한 요청 parameter = 최소급여(minPay), 최대급여(maxPay)(* 검색조건 미입력시 관계없음으로 조회), 모집인원(??),
//    //채용구분(상용직/일용직)(empTpGb), 지역(region), 직무명(??), 담당업무(??), 경력(career), 최종학력(education)
//    private val FAULT_RESULT = "faultResult"
//    private val TOTAL_TAG = "total"
//    private val ITEM_TAG = "wanted"
//    private val COMPANY_TAG = "company" //회사이름
//    private val TITLE_TAG = "title" //글제목
//    private val SALTPNM_TAG = "salTpNm" //임금형태
//    private val SAL_TAG = "sal" //임금
//    private val REGION_TAG = "region" //지역
//    private val MINEDUBG_TAG = "minEdubg" //최소학력
//    private val CAREER_TAG = "career" //경력
//    private val CLOSEDT_TAG = "closeDt" //마감일
//    private val INFOURL_TAG = "wantedMobileInfoUrl" //워크넷 모바일
//    private val ADDR_TAG = "basicAddr" //근무지 기본주소
//
//    private var parser: XmlPullParser? = null
//
//    fun WantedXmlParser() {
//        var factory: XmlPullParserFactory? = null
//        try {
//            factory = XmlPullParserFactory.newInstance()
//            parser = factory.newPullParser()
//        } catch (e: XmlPullParserException) {
//            e.printStackTrace()
//        }
//    }
//
//    fun parse1(xml: String?): List<Wanted> {
//        val resultList: MutableList<Wanted> = ArrayList()
//        var wtd: Wanted? = null
//        var tagType = TagType.NONE //  태그를 구분하기 위한 enum 변수 초기화
//        try {
//            parser!!.setInput(StringReader(xml))
//            var eventType = parser!!.eventType
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                when (eventType) {
//                    XmlPullParser.START_DOCUMENT -> {}
//                    XmlPullParser.START_TAG -> {
//                        val tag = parser!!.name
//                        if (tag == ITEM_TAG) {
//                            wtd = Wanted()
//                        } else if (tag == COMPANY_TAG) {
//                            tagType = TagType.COMPANY
//                        } else if (tag == TITLE_TAG) {
//                            tagType = TagType.TITLE
//                        } else if (tag == SALTPNM_TAG) {
//                            tagType = TagType.SALTPNM
//                        } else if (tag == SAL_TAG) {
//                            tagType = TagType.SAL
//                        } else if (tag == REGION_TAG) {
//                            tagType = TagType.REGION
//                        } else if (tag == MINEDUBG_TAG) {
//                            tagType = TagType.MINEDUBG
//                        } else if (tag == CAREER_TAG) {
//                            tagType = TagType.CAREER
//                        } else if (tag == CLOSEDT_TAG) {
//                            tagType = TagType.CLOSEDT
//                        } else if (tag == INFOURL_TAG) {
//                            tagType = TagType.INFOURL
//                        } else if (tag == ADDR_TAG) {
//                            tagType = TagType.ADDR
//                        } else if (tag == FAULT_RESULT) {
//                            return resultList
//                        }
//                    }
//                    XmlPullParser.END_TAG -> if (parser!!.name == ITEM_TAG && wtd != null) {
//                        resultList.add(wtd)
//                    }
//                    XmlPullParser.TEXT -> {
//                        val text = parser!!.text
//                        when (tagType) {
//                            TagType.COMPANY -> wtd!!.setCompany(text)
//                            TagType.TITLE -> wtd!!.setTitle(text)
//                            TagType.SALTPNM -> wtd!!.setSalTpNm(text)
//                            TagType.SAL -> wtd!!.setSal(text)
//                            TagType.REGION -> wtd!!.setRegion(text)
//                            TagType.MINEDUBG -> wtd!!.setMinEdubg(text)
//                            TagType.CAREER -> wtd!!.setCareer(text)
//                            TagType.CLOSEDT -> wtd!!.setCloseDt(text)
//                            TagType.INFOURL -> wtd!!.setWantedMobileInfoUrl(text)
//                            TagType.ADDR -> wtd!!.setBasicAddr(text)
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
//}