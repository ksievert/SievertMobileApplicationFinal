package hu.ait.sievertmobileapplicationfinal.data

import com.google.android.gms.maps.model.LatLng

object hardcodedData {
    val abbrMap = mapOf("12th St. Oakland City Center" to "12th",
        "16th St. Mission" to "16th", "19th St. Oakland" to "19th", "24th St. Mission" to "24th",
        "Ashby" to "ashb", "Antioch" to "antc", "Balboa Park" to "balb", "Bay Fair" to "bayf",
        "Berryessa" to "bery", "Castro Valley" to "cast", "Civic Center" to "civc", "Coliseum" to "cols",
        "Colma" to "colm", "Concord" to "conc", "Daly City" to "daly", "Downtown Berkeley" to "dbrk",
        "Dublin/Pleasanton" to "dubl", "El Cerrito del Norte" to "deln", "El Cerrito Plaza" to "plza",
        "Embarcadero" to "embr", "Fremont" to "frmt", "Fruitvale" to "ftvl", "Glen Park" to "glen",
        "Hayward" to "hayw", "Lafayette" to "lafy", "Lake Merritt" to "lake", "MacArthur" to "mcar",
        "Millbrae" to "mlbr", "Milpitas" to "mlpt", "Montgomery St." to "mont",
        "North Berkeley" to "nbrk", "North Concord/Martinez" to "ncon",
        "Oakland Int'l Airport" to "oakl", "Orinda" to "orin", "Pittsburg/Bay Point" to "pitt",
        "Pittsburg Center" to "pctr", "Pleasant Hill" to "phil", "Powell St." to "powl",
        "Richmond" to "rich", "Rockridge" to "rock", "San Bruno" to "sbrn",
        "San Francisco Int'l Airport" to "sfia", "San Leandro" to "sanl", "South Hayward" to "shay",
        "South San Francisco" to "ssan", "Union City" to "ucty", "Warm Springs/South Fremont" to "warm",
        "Walnut Creek" to "wcrk", "West Dublin" to "wdub", "West Oakland" to "woak")

    val coordMap = mapOf("antc" to LatLng(37.995388, -121.780420),
        "dubl" to LatLng(37.701687, -121.899179), "milb" to LatLng(37.600271, -122.386702),
        "oakl" to LatLng(37.713238, -122.212191), "rich" to LatLng(37.936853, -122.353099),
        "warm" to LatLng(37.502171, -121.939313))
}