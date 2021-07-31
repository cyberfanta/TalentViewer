package com.cyberfanta.talentviewer.models

import com.google.gson.annotations.SerializedName

data class Bios(

	@field:SerializedName("projects")
	val projects: List<ProjectsItem?>? = null,

	@field:SerializedName("education")
	val education: List<EducationItem?>? = null,

	@field:SerializedName("languages")
	val languages: List<LanguagesItem2?>? = null,

	@field:SerializedName("jobs")
	val jobs: List<JobsItem?>? = null,

	@field:SerializedName("experiences")
	val experiences: List<ExperiencesItem?>? = null,

	@field:SerializedName("opportunities")
	val opportunities: List<OpportunitiesItem?>? = null,

	@field:SerializedName("stats")
	val stats: Stats2? = null,

	@field:SerializedName("person")
	val person: Person2? = null,

	@field:SerializedName("strengths")
	val strengths: List<StrengthsItem2?>? = null,

	@field:SerializedName("awards")
	val awards: List<Any?>? = null,

	@field:SerializedName("professionalCultureGenomeResults")
	val professionalCultureGenomeResults: ProfessionalCultureGenomeResults? = null,

	@field:SerializedName("personalityTraitsResults")
	val personalityTraitsResults: PersonalityTraitsResults? = null,

	@field:SerializedName("interests")
	val interests: List<InterestsItem?>? = null,

	@field:SerializedName("publications")
	val publications: List<PublicationsItem?>? = null
)

data class ProjectsItem(

	@field:SerializedName("toYear")
	val toYear: String? = null,

	@field:SerializedName("highlighted")
	val highlighted: Boolean? = null,

	@field:SerializedName("toMonth")
	val toMonth: String? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("media")
	val media: List<Any?>? = null,

	@field:SerializedName("remote")
	val remote: Boolean? = null,

	@field:SerializedName("verifications")
	val verifications: Int? = null,

	@field:SerializedName("recommendations")
	val recommendations: Int? = null,

	@field:SerializedName("contributions")
	val contributions: String? = null,

	@field:SerializedName("responsibilities")
	val responsibilities: List<String?>? = null,

	@field:SerializedName("fromYear")
	val fromYear: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("organizations")
	val organizations: List<OrganizationsItem2?>? = null,

	@field:SerializedName("additionalInfo")
	val additionalInfo: String? = null,

	@field:SerializedName("fromMonth")
	val fromMonth: String? = null,

	@field:SerializedName("rank")
	val rank: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)

data class DataItem(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("locale")
	val locale: String? = null
)

data class ExperiencesItem(

	@field:SerializedName("toYear")
	val toYear: String? = null,

	@field:SerializedName("highlighted")
	val highlighted: Boolean? = null,

	@field:SerializedName("toMonth")
	val toMonth: String? = null,

	@field:SerializedName("weight")
	val weight: Double? = null,

	@field:SerializedName("media")
	val media: List<Any?>? = null,

	@field:SerializedName("remote")
	val remote: Boolean? = null,

	@field:SerializedName("verifications")
	val verifications: Int? = null,

	@field:SerializedName("recommendations")
	val recommendations: Int? = null,

	@field:SerializedName("contributions")
	val contributions: String? = null,

	@field:SerializedName("responsibilities")
	val responsibilities: List<Any?>? = null,

	@field:SerializedName("fromYear")
	val fromYear: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("organizations")
	val organizations: List<OrganizationsItem2?>? = null,

	@field:SerializedName("additionalInfo")
	val additionalInfo: String? = null,

	@field:SerializedName("fromMonth")
	val fromMonth: String? = null,

	@field:SerializedName("rank")
	val rank: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)

data class AnalysesItem(

	@field:SerializedName("groupId")
	val groupId: String? = null,

	@field:SerializedName("analysis")
	val analysis: Float? = null
)

data class MediaItemsItem(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class GroupsItem(

	@field:SerializedName("median")
	val median: Float? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("stddev")
	val stddev: Float? = null,

	@field:SerializedName("order")
	val order: Int? = null
)

data class PublicationsItem(

	@field:SerializedName("toYear")
	val toYear: String? = null,

	@field:SerializedName("highlighted")
	val highlighted: Boolean? = null,

	@field:SerializedName("toMonth")
	val toMonth: String? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("media")
	val media: List<MediaItem?>? = null,

	@field:SerializedName("remote")
	val remote: Boolean? = null,

	@field:SerializedName("verifications")
	val verifications: Int? = null,

	@field:SerializedName("recommendations")
	val recommendations: Int? = null,

	@field:SerializedName("contributions")
	val contributions: String? = null,

	@field:SerializedName("responsibilities")
	val responsibilities: List<Any?>? = null,

	@field:SerializedName("fromYear")
	val fromYear: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("organizations")
	val organizations: List<OrganizationsItem2?>? = null,

	@field:SerializedName("additionalInfo")
	val additionalInfo: String? = null,

	@field:SerializedName("fromMonth")
	val fromMonth: String? = null,

	@field:SerializedName("rank")
	val rank: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)

data class LinksItem(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class Person2(

	@field:SerializedName("professionalHeadline")
	val professionalHeadline: String? = null,

	@field:SerializedName("completion")
	val completion: Double? = null,

	@field:SerializedName("showPhone")
	val showPhone: Boolean? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("verified")
	val verified: Boolean? = null,

	@field:SerializedName("flags")
	val flags: Flags2? = null,

	@field:SerializedName("weight")
	val weight: Float? = null,

	@field:SerializedName("locale")
	val locale: String? = null,

	@field:SerializedName("subjectId")
	val subjectId: Int? = null,

	@field:SerializedName("picture")
	val picture: String? = null,

	@field:SerializedName("hasEmail")
	val hasEmail: Boolean? = null,

	@field:SerializedName("isTest")
	val isTest: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("links")
	val links: List<LinksItem?>? = null,

	@field:SerializedName("location")
	val location: Location? = null,

	@field:SerializedName("theme")
	val theme: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("pictureThumbnail")
	val pictureThumbnail: String? = null,

	@field:SerializedName("claimant")
	val claimant: Boolean? = null,

	@field:SerializedName("summaryOfBio")
	val summaryOfBio: String? = null,

	@field:SerializedName("weightGraph")
	val weightGraph: String? = null,

	@field:SerializedName("publicId")
	val publicId: String? = null
)

data class OpportunitiesItem(

	@field:SerializedName("field")
	val field: String? = null,

//	@field:SerializedName("data")
//	val data: List<DataItem?>? = null,

	@field:SerializedName("data")
	val data1: Any? = null,

	@field:SerializedName("interest")
	val interest: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class InterestsItem(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("media")
	val media: List<Any?>? = null,

	@field:SerializedName("additionalInfo")
	val additionalInfo: String? = null
)

data class Stats2(

	@field:SerializedName("education")
	val education: Int? = null,

	@field:SerializedName("projects")
	val projects: Int? = null,

	@field:SerializedName("strengths")
	val strengths: Int? = null,

	@field:SerializedName("jobs")
	val jobs: Int? = null,

	@field:SerializedName("interests")
	val interests: Int? = null,

	@field:SerializedName("publications")
	val publications: Int? = null
)

data class EducationItem(

	@field:SerializedName("highlighted")
	val highlighted: Boolean? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("media")
	val media: List<Any?>? = null,

	@field:SerializedName("remote")
	val remote: Boolean? = null,

	@field:SerializedName("verifications")
	val verifications: Int? = null,

	@field:SerializedName("recommendations")
	val recommendations: Int? = null,

	@field:SerializedName("responsibilities")
	val responsibilities: List<Any?>? = null,

	@field:SerializedName("fromYear")
	val fromYear: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("organizations")
	val organizations: List<OrganizationsItem2?>? = null,

	@field:SerializedName("additionalInfo")
	val additionalInfo: String? = null,

	@field:SerializedName("fromMonth")
	val fromMonth: String? = null,

	@field:SerializedName("rank")
	val rank: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)

data class OrganizationsItem2(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("publicId")
	val publicId: String? = null,

	@field:SerializedName("picture")
	val picture: String? = null
)

data class Location(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("timezoneOffSet")
	val timezoneOffSet: Int? = null,

	@field:SerializedName("timezone")
	val timezone: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
)

data class JobsItem(

	@field:SerializedName("toYear")
	val toYear: String? = null,

	@field:SerializedName("highlighted")
	val highlighted: Boolean? = null,

	@field:SerializedName("toMonth")
	val toMonth: String? = null,

	@field:SerializedName("weight")
	val weight: Double? = null,

	@field:SerializedName("media")
	val media: List<Any?>? = null,

	@field:SerializedName("verifications")
	val verifications: Int? = null,

	@field:SerializedName("recommendations")
	val recommendations: Int? = null,

	@field:SerializedName("responsibilities")
	val responsibilities: List<String?>? = null,

	@field:SerializedName("fromYear")
	val fromYear: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("organizations")
	val organizations: List<OrganizationsItem2?>? = null,

	@field:SerializedName("fromMonth")
	val fromMonth: String? = null,

	@field:SerializedName("rank")
	val rank: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("remote")
	val remote: Boolean? = null,

	@field:SerializedName("additionalInfo")
	val additionalInfo: String? = null
)

data class ProfessionalCultureGenomeResults(

	@field:SerializedName("analyses")
	val analyses: List<AnalysesItem?>? = null,

	@field:SerializedName("groups")
	val groups: List<GroupsItem?>? = null
)

data class Flags2(

	@field:SerializedName("benefits")
	val benefits: Boolean? = null,

	@field:SerializedName("canary")
	val canary: Boolean? = null,

	@field:SerializedName("firstSignalSent")
	val firstSignalSent: Boolean? = null,

	@field:SerializedName("signalsOnboardingCompleted")
	val signalsOnboardingCompleted: Boolean? = null,

	@field:SerializedName("genomeCompletionAcknowledged")
	val genomeCompletionAcknowledged: Boolean? = null,

	@field:SerializedName("importingLinkedin")
	val importingLinkedin: Boolean? = null,

	@field:SerializedName("featureDiscovery")
	val featureDiscovery: Boolean? = null,

	@field:SerializedName("cvImported")
	val cvImported: Boolean? = null,

	@field:SerializedName("importingLinkedinRecommendations")
	val importingLinkedinRecommendations: Boolean? = null,

	@field:SerializedName("signalsFeatureDiscovery")
	val signalsFeatureDiscovery: Boolean? = null,

	@field:SerializedName("onBoarded")
	val onBoarded: Boolean? = null,

	@field:SerializedName("remoter")
	val remoter: Boolean? = null,

	@field:SerializedName("enlauSource")
	val enlauSource: Boolean? = null,

	@field:SerializedName("fake")
	val fake: Boolean? = null,

	@field:SerializedName("appContactsImported")
	val appContactsImported: Boolean? = null,

	@field:SerializedName("contactsImported")
	val contactsImported: Boolean? = null
)

data class MediaItem(

	@field:SerializedName("mediaItems")
	val mediaItems: List<MediaItemsItem?>? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("mediaType")
	val mediaType: String? = null,

	@field:SerializedName("group")
	val group: String? = null
)

data class StrengthsItem2(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("supra")
	val supra: Boolean? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("weight")
	val weight: Float? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("media")
	val media: List<Any?>? = null,

	@field:SerializedName("recommendations")
	val recommendations: Int? = null,

	@field:SerializedName("additionalInfo")
	val additionalInfo: String? = null
)

data class PersonalityTraitsResults(

	@field:SerializedName("analyses")
	val analyses: List<AnalysesItem?>? = null,

	@field:SerializedName("groups")
	val groups: List<GroupsItem?>? = null
)

data class LanguagesItem2(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("fluency")
	val fluency: String? = null,

	@field:SerializedName("language")
	val language: String? = null
)
