package com.trading

import com.gigaspaces.annotation.pojo.SpaceClass
import com.gigaspaces.annotation.pojo.SpaceId
import com.gigaspaces.annotation.pojo.SpaceRouting

@SpaceClass
data class AllocationReport(
        var allocationId: String,
        var transactionType: String
) {
    constructor() : this("#empty", "#empty")

    var id = ""
        private set
        @SpaceRouting
        @SpaceId(autoGenerate = true)
        get
}