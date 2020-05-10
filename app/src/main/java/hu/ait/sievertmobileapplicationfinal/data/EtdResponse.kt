package hu.ait.sievertmobileapplicationfinal.data

// result generated from /json

data class xml(val version: String?, val encoding: String?)

data class Base(val xml: xml?, val root: Root?)

data class Estimate1368298459(val minutes: String?, val platform: String?, val direction: String?, val length: String?, val color: String?, val hexcolor: String?, val bikeflag: String?, val delay: String?)

data class Estimate29643027(val minutes: String?, val platform: String?, val direction: String?, val length: String?, val color: String?, val hexcolor: String?, val bikeflag: String?, val delay: String?)

data class Etd156029136(val destination: String?, val abbreviation: String?, val limited: String?, val estimate: List<Estimate1368298459>?)

data class Root(val id: String?, val uri: Uri?, val date: String?, val time: String?, val station: List<Station106556928>?, val message: String?)

data class Station106556928(val name: String?, val abbr: String?, val etd: List<Etd156029136>?)

data class Uri(val cdatasection: String?)

