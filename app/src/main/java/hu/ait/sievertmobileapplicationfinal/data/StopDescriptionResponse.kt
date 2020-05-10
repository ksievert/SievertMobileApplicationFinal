package hu.ait.sievertmobileapplicationfinal.data

// result generated from /json

data class xml2(val version: String?, val encoding: String?)

data class Attraction(val cdatasection: String?)

data class Base2(val xml: xml2?, val root: Root2?)

data class Cross_street(val cdatasection: String?)

data class Food(val cdatasection: String?)

data class Intro(val cdatasection: String?)

data class Link(val cdatasection: String?)

data class North_platforms(val platform: List<String>?)

data class North_routes(val route: List<String>?)

data class Root2(val id: String?, val uri: Uri2?, val stations: Stations?, val message: String?)

data class Shopping(val cdatasection: String?)

data class South_platforms(val platform: List<String>?)

data class South_routes(val route: List<String>?)

data class Station(val name: String?, val abbr: String?, val gtfs_latitude: String?, val gtfs_longitude: String?, val address: String?, val city: String?, val county: String?, val state: String?, val zipcode: String?, val north_routes: North_routes?, val south_routes: South_routes?, val north_platforms: North_platforms?, val south_platforms: South_platforms?, val platform_info: String?, val intro: Intro?, val cross_street: Cross_street?, val food: Food?, val shopping: Shopping?, val attraction: Attraction?, val link: Link?)

data class Stations(val station: Station?)

data class Uri2(val cdatasection: String?)