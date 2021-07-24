package com.cyberfanta.talentviewer.models

import com.google.gson.annotations.SerializedName

data class Opportunities(

	@field:SerializedName("aggregators")
	val aggregators: Aggregators? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem?>? = null
)

data class OrganizationsItem3(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("picture")
	val picture: String? = null
)

data class Context(

	@field:SerializedName("signaled")
	val signaled: List<Any?>? = null
)

data class Opportunity(

	@field:SerializedName("completion")
	val completion: Double? = null
)

data class Input(

	@field:SerializedName("criteria")
	val criteria: Any? = null,

	@field:SerializedName("opportunity")
	val opportunity: Opportunity? = null
)

data class QuestionsItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("text")
	val text: String? = null
)

data class SkillsItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("experience")
	val experience: String? = null
)

data class Aggregators(
	val any: Any? = null
)

data class ResultsItem(

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("questions")
	val questions: List<QuestionsItem?>? = null,

	@field:SerializedName("_meta")
	val meta: Meta? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("remote")
	val remote: Boolean? = null,

	@field:SerializedName("objective")
	val objective: String? = null,

	@field:SerializedName("skills")
	val skills: List<SkillsItem?>? = null,

	@field:SerializedName("external")
	val external: Boolean? = null,

	@field:SerializedName("members")
	val members: List<MembersItem3?>? = null,

	@field:SerializedName("organizations")
	val organizations: List<OrganizationsItem3?>? = null,

	@field:SerializedName("context")
	val context: Context? = null,

	@field:SerializedName("locations")
	val locations: List<String?>? = null,

	@field:SerializedName("compensation")
	val compensation: Compensation3? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("deadline")
	val deadline: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Rank(

	@field:SerializedName("boostedValue")
	val boostedValue: Double? = null,

	@field:SerializedName("position")
	val position: Int? = null,

	@field:SerializedName("value")
	val value: Double? = null
)

data class Pagination(

	@field:SerializedName("next")
	val next: String? = null,

	@field:SerializedName("previous")
	val previous: Any? = null
)

data class Data(

	@field:SerializedName("minAmount")
	val minAmount: Double? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("maxHourlyUSD")
	val maxHourlyUSD: Double? = null,

	@field:SerializedName("periodicity")
	val periodicity: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("maxAmount")
	val maxAmount: Double? = null,

	@field:SerializedName("minHourlyUSD")
	val minHourlyUSD: Double? = null
)

data class Compensation3(

	@field:SerializedName("visible")
	val visible: Boolean? = null,

	@field:SerializedName("data")
	val data: Data? = null
)

data class Meta(

	@field:SerializedName("filter")
	val filter: Any? = null,

	@field:SerializedName("scorer")
	val scorer: Scorer? = null,

	@field:SerializedName("boosters")
	val boosters: List<Any?>? = null,

	@field:SerializedName("rank")
	val rank: Rank? = null
)

data class Scorer(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("@type")
	val type: String? = null,

	@field:SerializedName("and")
	val and: List<AndItem?>? = null
)

data class MembersItem3(

	@field:SerializedName("professionalHeadline")
	val professionalHeadline: String? = null,

	@field:SerializedName("manager")
	val manager: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("member")
	val member: Boolean? = null,

	@field:SerializedName("weight")
	val weight: Double? = null,

	@field:SerializedName("poster")
	val poster: Boolean? = null,

	@field:SerializedName("subjectId")
	val subjectId: String? = null,

	@field:SerializedName("picture")
	val picture: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class AndItem(

	@field:SerializedName("input")
	val input: Input? = null,

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("@type")
	val type: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
