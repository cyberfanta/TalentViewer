package com.cyberfanta.talentviewer.presenters

class PageData(var offset: String?, var size: String?, var aggregators: Boolean) {
        override fun toString(): String {
            var result = ""
            if (offset != null)
                result += "?offset=$offset"

            if (size != null && result.isNotEmpty())
                result += "&size=$size"
            else if (size != null)
                result += "?size=$size"

            if (aggregators && result.isNotEmpty())
                result += "&aggregators=$aggregators"
            else if (aggregators)
                result += "?aggregators=$aggregators"

            return result
        }
    }
//}