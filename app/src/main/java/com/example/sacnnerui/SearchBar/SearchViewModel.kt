package com.example.sacnnerui.SearchBar

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _searchWidgetState: MutableState<SearchWidgetState> =
        mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetState: State<SearchWidgetState> = _searchWidgetState

    private val _searchTextState: MutableState<String> =
        mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        _searchWidgetState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

    val productDictionary = mapOf(
        "MIL073022" to "Milwaukee M28 Cordless 6-1/2\" Circular Saw Kit",
        "MIL082020" to "Milwaukee M12 Cordless Mounting Fan - Tool Only",
        "MIL073022" to "Milwaukee M28 Cordless 6-1/2\" Circular Saw Kit",
        "MIL082020" to "Milwaukee M12 Cordless Mounting Fan - Tool Only",
        "MIL085020" to "Milwaukee M12 Cordless Compact Vacuum - Tool Only",
        "MIL085220" to "Milwaukee M12 Cordless Compact Spot Blower - Tool Only",
        "MIL088020" to "Milwaukee M18 Cordless 2-Gallon Wet/Dry Vacuum  - Tool Only",
        "MIL088220" to "Milwaukee M18 Cordless Compact Vacuum - Tool Only",
        "MIL088420" to "Milwaukee M18 Cordless Compact Blower - Tool Only",
        "MIL088520" to "Milwaukee M18 FUEL Cordless 3-in-1 Backpack Vacuum - Tool Only",
        "MIL088620" to "Milwaukee M18 Cordless Jobsite Fan - Tool Only",
        "MIL092020" to "Milwaukee M18 FUEL 9 Gallon Dual-Battery Wet/Dry Vacuum - Tool Only",
        "MIL092022HD" to "Milwaukee M18 FUEL 9 Gallon Dual-Battery Wet/Dry Vacuum Kit",
        "MIL093120" to "Milwaukee 6.5 Peak HP Wet/Dry Vacuum Motor Head",
        "MIL094020" to "Milwaukee M18 FUEL Cordless Compact Vacuum  - Tool Only",
        "MIL096020" to "Milwaukee M12 FUEL Cordless 1.6 Gallon Wet/Dry Vacuum  - Tool Only",
        "MIL096021" to "Milwaukee M12 FUEL Cordless 1.6 Gallon Wet/Dry Vacuum Kit",
        "MIL097020" to "Milwaukee M18 FUEL Cordless PACKOUT 2.5 Gallon Wet/Dry Vacuum  - Tool Only",
        "CHA1002" to "Chapin 48 oz. Multi-Purpose Handheld Pump Sprayer",
        "CHA1046" to "Chapin 48 oz. Industrial Cleaner/Degreaser Handheld Pump Sprayer",
        "BOH11250VSR" to "Bosch SDS Plus® Bulldog™ 7/8\" Rotary Hammer",
        "BOH11255VSR" to "Bosch SDS Plus® Bulldog™ Xtreme 1\" D-Handle Rotary Hammer",
        "BOH11264EVS" to "Bosch SDS MAX® 1-5/8\" Combination Hammer",
        "BOH11316EVS" to "Bosch 14 Amp SDS MAX® Demolition Hammer",
        "BOH11321EVS" to "Bosch 13 Amp SDS MAX® Demolition Hammer",
        "BOH11335K" to "Bosch Jack 35 Lb. 1-1/8\" Hex Breaker Hammer",
        "BAR11766" to "Bartell 24\" Float Pan",
        "BAR11811" to "Bartell 30\" Float Pan",
        "BOH1191VSRK" to "Bosch Hammer Drill",
        "CTS12017" to "CTS Adjustable Load Binder w/ Safety Pin",
        "CTS12018" to "CTS Safety Pin",
        "CTS12510" to "CTS Horizontal E-Track - 10ft",
        "CTS12610" to "CTS Vertical E-Track - 10ft",
        "MAR134484" to "Vileda 24\" Trooper Push Broom w/ Brace",
        "MAR134486" to "Vileda 24\" Trooper Combo Synthetic Push Broom",
        "MARMB93LWB" to "Vileda Curved Magnetic Broom",
        "MAR134768" to "Vileda Broom Brace",
        "MAR134985" to "Vileda 16\" Barn Broom Head",
        "BOH1375A" to "Bosch 4-1/2\" 6 A Small Angle Grinder",
        "CTS140072" to "CTS Steel Winch Track - 6ft",
        "LEP1417396" to "LePage PL Premium Fast Grab Construction Adhesive - 295mL",
        "LEP1421925" to "LePage PL9000 Construction Adhesive - 295mL",
        "LEP1421928" to "LePage PL200 Drywall & Paneling Adhesive - 295mL",
        "MIL14461011" to "Milwaukee Steel QUIK-LOK Clamp Kit",
        "CHA1480" to "Chapin 3.5-gallon Industrial Funnel Top General Duty Tri-Poxy Steel Tank Sprayer",
        "MAR153041" to "Vileda Super Angled Broom w/ Handle",
        "LEP1536417" to "LePage Express Quick Dry Glue - 400mL",
        "BOH1617" to "Bosch 2 HP Fixed-Base Router",
        "BOH1617EVS" to "Bosch 2.25 HP Electronic Fixed-Base Router",
        "BOH1617EVSPK" to "Bosch 2.25 HP Combination Plunge/Fixed-Base Router",
        "LEP1662532" to "LePage Ultra Gel Super Glue - 4mL",
        "MIL16756" to "Milwaukee 1675-6 1/2\" Hole Hawg",
        "BOH1775E" to "Bosch Factory-Reconditioned 5\" Tuck Pointer Grinder",
        "LEP1819160" to "LePage Plastic Wood Filler - 473mL",
        "CTS18352" to "CTS E-Track Tie Off w/ O-Ring",
        "CTS18474" to "CTS Reusable Hinged Roof Anchor Bracket w/ D-Ring",
        "MIL18541" to "Milwaukee 3/4\" 120 V 350 RPM Large Drill w/Keyed Chuck",
        "CTS18770" to "CTS 4\" Plastic Corner Protector",
        "CTS1877836" to "CTS 36\" Plastic Corner Protector",
        "CTS18778" to "CTS 10\" Plastic Corner Protector",
        "CTS18781" to "CTS Heavy Duty Brick Board - 7\" x 7\" x 10\"",
        "CTS18787424" to "CTS Ballistic Nylon Anti-Abrasive Sleeve Protector - 4.5\" x 24\"",
        "BOH18DC5E" to "Bosch Dust-Extraction Guard For Bosch 4.5\"/5\" Small Angle Grinders",
        "BOH18SG5E" to "Bosch Surface Grinding Dust-Extraction Attachment",
        "BOH18SG7" to "Bosch 7\" Angle Grinder Concrete Surfacing Attachment",
        "CHA19049" to "Chapin 3.5-gallon Xtreme Tri-Poxy"
    )

    // Function to search for a product by model number
    fun searchProductByModelNumber(modelNumber: String): String? {
        return productDictionary[modelNumber]
    }

}