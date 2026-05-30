package com.mgpark.trackbox.domain.model

enum class CarrierId(val raw: String, val displayName: String) {
    CJ_LOGISTICS("kr.cjlogistics", "CJ대한통운"),
    KOREA_POST("kr.epost", "우체국택배"),
    DAESIN("kr.daesin", "대신택배"),
    COUPANG_LS("kr.coupangls", "쿠팡 로지스틱스"),
    CU_POST("kr.cupost", "CU 편의점택배"),
    CHUNIL("kr.chunilps", "천일택배"),
    CVS_NET("kr.cvsnet", "GS Postbox"),
    CWAY("kr.cway", "CWAY (Woori Express)"),
    DELIBOX("kr.delibox", "딜리박스"),
    LX_PANTOS("kr.epantos", "LX 판토스");

    companion object {
        fun fromRaw(raw: String): CarrierId? = entries.firstOrNull { it.raw == raw }
    }
}
