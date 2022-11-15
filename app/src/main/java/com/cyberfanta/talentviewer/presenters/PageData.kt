package com.cyberfanta.talentviewer.presenters

class PageData(private var offset: String?, private var size: String?, private var aggregators: Boolean) {
        override fun toString(): String {
            var result = ""
            if (offset != null)
                result += "?offset=$offset"

            if (size != null && result.isNotEmpty())
                result += "&size=$size"
            else if (size != null)
                result += "?size=$size"

            if (aggregators && result.isNotEmpty())
                result += "&aggregators=true"
            else if (aggregators)
                result += "?aggregators=true"

            return result
        }
    }
//}