package com.cyberfanta.talentviewer.models

import com.google.gson.annotations.SerializedName

data class Jobs(

	@field:SerializedName("attachments")
	val attachments: List<AttachmentsItem?>? = null,

	@field:SerializedName("boardVersion")
	val boardVersion: Int? = null,

	@field:SerializedName("prefilledStatus")
	val prefilledStatus: PrefilledStatus? = null,

	@field:SerializedName("locale")
	val locale: String? = null,

	@field:SerializedName("objective")
	val objective: String? = null,

	@field:SerializedName("stats")
	val stats: Stats? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("draft")
	val draft: Any? = null,

	@field:SerializedName("members")
	val members: List<MembersItem?>? = null,

	@field:SerializedName("details")
	val details: List<DetailsItem?>? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("place")
	val place: Place? = null,

	@field:SerializedName("deadline")
	val deadline: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("owner")
	val owner: Owner? = null,

	@field:SerializedName("completion")
	val completion: Double? = null,

	@field:SerializedName("agreement")
	val agreement: Agreement? = null,

	@field:SerializedName("languages")
	val languages: List<LanguagesItem?>? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("crawled")
	val crawled: Boolean? = null,

	@field:SerializedName("opportunity")
	val opportunity: String? = null,

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("commitment")
	val commitment: Commitment? = null,

	@field:SerializedName("stableOn")
	val stableOn: String? = null,

	@field:SerializedName("timezones")
	val timezones: List<String?>? = null,

	@field:SerializedName("strengths")
	val strengths: List<StrengthsItem?>? = null,

	@field:SerializedName("organizations")
	val organizations: List<OrganizationsItem?>? = null,

	@field:SerializedName("compensation")
	val compensation: Compensation? = null,

	@field:SerializedName("openGraph")
	val openGraph: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Stats(

	@field:SerializedName("finishedApplications")
	val finishedApplications: Int? = null
)

data class LanguagesItem(

	@field:SerializedName("fluency")
	val fluency: String? = null,

	@field:SerializedName("language")
	val language: Language? = null
)

data class Flags(

	@field:SerializedName("membersBenefitsViewed")
	val membersBenefitsViewed: Boolean? = null,

	@field:SerializedName("firstSignalSent")
	val firstSignalSent: Boolean? = null,

	@field:SerializedName("matchAndRankDisclaimerViewed")
	val matchAndRankDisclaimerViewed: Boolean? = null,

	@field:SerializedName("earlyAccess")
	val earlyAccess: Boolean? = null,

	@field:SerializedName("experiencesVerificationsSurvey")
	val experiencesVerificationsSurvey: Boolean? = null,

	@field:SerializedName("notificationsPhrasesSarcastic")
	val notificationsPhrasesSarcastic: Boolean? = null,

	@field:SerializedName("opportunityCrawler")
	val opportunityCrawler: Boolean? = null,

	@field:SerializedName("torreMatchAcknowledged")
	val torreMatchAcknowledged: Boolean? = null,

	@field:SerializedName("veiled")
	val veiled: Boolean? = null,

	@field:SerializedName("automatedMessagesFeatureDiscovery")
	val automatedMessagesFeatureDiscovery: Boolean? = null,

	@field:SerializedName("opportunitiesBenefitsViewed")
	val opportunitiesBenefitsViewed: Boolean? = null,

	@field:SerializedName("firstGetSignalBenefitsViewed")
	val firstGetSignalBenefitsViewed: Boolean? = null,

	@field:SerializedName("opportunityOperator")
	val opportunityOperator: Boolean? = null,

	@field:SerializedName("signedInToOpportunities")
	val signedInToOpportunities: Boolean? = null,

	@field:SerializedName("firstSignalOnBoarded")
	val firstSignalOnBoarded: Boolean? = null,

	@field:SerializedName("referredMatchUnderstood")
	val referredMatchUnderstood: Boolean? = null,

	@field:SerializedName("enlauSource")
	val enlauSource: Boolean? = null,

	@field:SerializedName("contactsImported")
	val contactsImported: Boolean? = null,

	@field:SerializedName("features")
	val features: String? = null
)

data class Commitment(

	@field:SerializedName("hours")
	val hours: Int? = null,

	@field:SerializedName("code")
	val code: String? = null
)

data class Language(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class Place(

	@field:SerializedName("timezone")
	val timezone: Boolean? = null,

	@field:SerializedName("location")
	val location: List<Any?>? = null,

	@field:SerializedName("remote")
	val remote: Boolean? = null,

	@field:SerializedName("anywhere")
	val anywhere: Boolean? = null
)

data class DetailsItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)

data class PrefilledStatus(

	@field:SerializedName("status")
	val status: String? = null
)

data class StrengthsItem(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("experience")
	val experience: String? = null
)

data class Compensation(

	@field:SerializedName("minAmount")
	val minAmount: Int? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("visible")
	val visible: Boolean? = null,

	@field:SerializedName("maxAmountConverted")
	val maxAmountConverted: Double? = null,

	@field:SerializedName("estimate")
	val estimate: Boolean? = null,

	@field:SerializedName("periodicity")
	val periodicity: String? = null,

	@field:SerializedName("minAmountConverted")
	val minAmountConverted: Double? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("conversionCurrency")
	val conversionCurrency: String? = null,

	@field:SerializedName("maxAmount")
	val maxAmount: Int? = null
)

data class Owner(

	@field:SerializedName("professionalHeadline")
	val professionalHeadline: String? = null,

	@field:SerializedName("hasEmail")
	val hasEmail: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("verified")
	val verified: Boolean? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("hasBio")
	val hasBio: Boolean? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("pictureThumbnail")
	val pictureThumbnail: String? = null,

	@field:SerializedName("picture")
	val picture: String? = null,

	@field:SerializedName("subjectId")
	val subjectId: Int? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class MembersItem(

	@field:SerializedName("visible")
	val visible: Boolean? = null,

	@field:SerializedName("manager")
	val manager: Boolean? = null,

	@field:SerializedName("person")
	val person: Person? = null,

	@field:SerializedName("member")
	val member: Boolean? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("poster")
	val poster: Boolean? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class AttachmentsItem(

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("resource")
	val resource: String? = null,

	@field:SerializedName("caption")
	val caption: String? = null
)

data class OrganizationsItem(

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("picture")
	val picture: String? = null
)

data class Person(

	@field:SerializedName("professionalHeadline")
	val professionalHeadline: String? = null,

	@field:SerializedName("hasEmail")
	val hasEmail: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("verified")
	val verified: Boolean? = null,

	@field:SerializedName("flags")
	val flags: Flags? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("hasBio")
	val hasBio: Boolean? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("subjectId")
	val subjectId: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("pictureThumbnail")
	val pictureThumbnail: String? = null,

	@field:SerializedName("picture")
	val picture: String? = null
)

data class Agreement(

	@field:SerializedName("currencyTaxes")
	val currencyTaxes: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)
