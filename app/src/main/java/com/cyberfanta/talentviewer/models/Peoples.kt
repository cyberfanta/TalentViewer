package com.cyberfanta.talentviewer.models

import com.google.gson.annotations.SerializedName

data class Peoples(

	@field:SerializedName("aggregators")
	val aggregators: Aggregators1? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("pagination")
	val pagination: Pagination1? = null,

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("results")
	val results: List<PeopleItem?>? = null
)

data class Intern(

	@field:SerializedName("amount")
	val amount: Double? = null,

	@field:SerializedName("periodicity")
	val periodicity: String? = null,

	@field:SerializedName("privacy")
	val privacy: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("minHourlyUSD")
	val minHourlyUSD: Double? = null
)

data class Employee(

	@field:SerializedName("amount")
	val amount: Double? = null,

	@field:SerializedName("periodicity")
	val periodicity: String? = null,

	@field:SerializedName("privacy")
	val privacy: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("minHourlyUSD")
	val minHourlyUSD: Double? = null
)

data class AndItem1(

	@field:SerializedName("score")
	val score: Any? = null,

	@field:SerializedName("input")
	val input: Input1? = null,

	@field:SerializedName("scorer")
	val scorer: String? = null,

	@field:SerializedName("@type")
	val type: String? = null,

	@field:SerializedName("rank")
	val rank: Double? = null
)

data class Aggregators1(
	val any: Any? = null
)

data class SkillsItem1(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("weight")
	val weight: Double? = null
)

data class PeopleItem(

	@field:SerializedName("professionalHeadline")
	val professionalHeadline: String? = null,

	@field:SerializedName("compensations")
	val compensations: Compensations? = null,

	@field:SerializedName("locationName")
	val locationName: String? = null,

	@field:SerializedName("openTo")
	val openTo: List<String?>? = null,

	@field:SerializedName("verified")
	val verified: Boolean? = null,

	@field:SerializedName("_meta")
	val meta: Meta1? = null,

	@field:SerializedName("weight")
	val weight: Double? = null,

	@field:SerializedName("picture")
	val picture: String? = null,

	@field:SerializedName("subjectId")
	val subjectId: String? = null,

	@field:SerializedName("skills")
	val skills: List<SkillsItem1?>? = null,

	@field:SerializedName("context")
	val context: Context1? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("remoter")
	val remoter: Boolean? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class Context1(

	@field:SerializedName("signaled")
	val signaled: Any? = null
)

data class Pagination1(

	@field:SerializedName("next")
	val next: String? = null,

	@field:SerializedName("previous")
	val previous: String? = null
)

data class Meta1(

	@field:SerializedName("filter")
	val filter: Any? = null,

	@field:SerializedName("ranker")
	val ranker: Ranker? = null
)

data class Freelancer(

	@field:SerializedName("amount")
	val amount: Double? = null,

	@field:SerializedName("periodicity")
	val periodicity: String? = null,

	@field:SerializedName("privacy")
	val privacy: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("minHourlyUSD")
	val minHourlyUSD: Double? = null
)

data class Person1(

	@field:SerializedName("grammar")
	val grammar: Double? = null,

	@field:SerializedName("completion")
	val completion: Double? = null,

	@field:SerializedName("weight")
	val weight: Double? = null
)

data class Ranker(

	@field:SerializedName("score")
	val score: Any? = null,

	@field:SerializedName("@type")
	val type: String? = null,

	@field:SerializedName("and")
	val and: List<AndItem1?>? = null,

	@field:SerializedName("rank")
	val rank: Double? = null
)

data class Compensations(

	@field:SerializedName("employee")
	val employee: Employee? = null,

	@field:SerializedName("freelancer")
	val freelancer: Freelancer? = null,

	@field:SerializedName("intern")
	val intern: Intern? = null
)

data class Input1(

	@field:SerializedName("criteria")
	val criteria: Any? = null,

	@field:SerializedName("person")
	val person: Person1? = null
)
