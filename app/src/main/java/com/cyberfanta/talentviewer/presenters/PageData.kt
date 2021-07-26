package com.cyberfanta.talentviewer.presenters

class PageData(var offset: String?, var size: String?, var aggregators: String?) {
        override fun toString(): String {
            var result = ""
            if (offset != null)
                result += "?offset=$offset"

            if (size != null && result.isNotEmpty())
                result += "&size=$size"
            else if (size != null)
                result += "?size=$size"

            if (aggregators != null && result.isNotEmpty())
                result += "&aggregators=$aggregators"
            else if (aggregators != null)
                result += "?aggregators=$aggregators"

            return result
        }
    }
//}