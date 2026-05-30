package com.mgpark.trackbox.ui.common

import com.mgpark.trackbox.domain.model.CarrierId
import com.mgpark.trackbox.domain.model.Tracking
import com.mgpark.trackbox.domain.model.TrackingDetail
import com.mgpark.trackbox.domain.model.TrackingParty
import com.mgpark.trackbox.domain.model.TrackingProgress
import com.mgpark.trackbox.domain.model.TrackingState
import java.time.Instant

internal object SamplePreviewData {
    val now: Instant = Instant.parse("2026-05-30T03:34:00Z")

    val inTransit = Tracking(
        id = 1L,
        trackingNumber = "1234567890",
        carrierId = CarrierId.CJ_LOGISTICS,
        alias = "엄마 선물",
        state = TrackingState.IN_TRANSIT,
        lastDescription = "서울 강남 SUB 허브를 통과했어요",
        lastLocation = "서울 강남 SUB",
        lastEventAt = now.minusSeconds(3_600),
        createdAt = now.minusSeconds(10_000),
        updatedAt = now.minusSeconds(60),
        isArchived = false,
    )

    val delivered = Tracking(
        id = 2L,
        trackingNumber = "9876543210",
        carrierId = CarrierId.KOREA_POST,
        alias = null,
        state = TrackingState.DELIVERED,
        lastDescription = "고객님께 배송이 완료되었습니다",
        lastLocation = "수원시 영통구",
        lastEventAt = now.minusSeconds(86_400),
        createdAt = now.minusSeconds(259_200),
        updatedAt = now.minusSeconds(86_400),
        isArchived = false,
    )

    val exception = Tracking(
        id = 3L,
        trackingNumber = "5555555555",
        carrierId = CarrierId.COUPANG_LS,
        alias = "겨울 자켓",
        state = TrackingState.EXCEPTION,
        lastDescription = "주소 불명으로 배송이 지연되고 있어요",
        lastLocation = "성남시 분당구",
        lastEventAt = now.minusSeconds(5_400),
        createdAt = now.minusSeconds(20_000),
        updatedAt = now.minusSeconds(120),
        isArchived = false,
    )

    val list: List<Tracking> = listOf(inTransit, delivered, exception)

    val detail: TrackingDetail = TrackingDetail(
        summary = inTransit,
        sender = TrackingParty(name = "당근마켓 셀러", location = null),
        recipient = TrackingParty(name = "박민균", location = null),
        progresses = listOf(
            TrackingProgress(
                time = now.minusSeconds(3_600),
                state = TrackingState.IN_TRANSIT,
                location = "서울 강남 SUB",
                description = "허브 통과",
            ),
            TrackingProgress(
                time = now.minusSeconds(7_200),
                state = TrackingState.AT_PICKUP,
                location = "서울 강남 LMC",
                description = "집화 완료",
            ),
            TrackingProgress(
                time = now.minusSeconds(10_800),
                state = TrackingState.INFORMATION_RECEIVED,
                location = null,
                description = "택배가 접수되었습니다",
            ),
        ),
    )
}
