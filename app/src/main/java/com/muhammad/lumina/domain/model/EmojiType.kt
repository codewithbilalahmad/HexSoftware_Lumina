package com.muhammad.lumina.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.muhammad.lumina.R

@Immutable
enum class EmojiType(@get:StringRes val label : Int){
    SMILEYS_AND_PEOPLE(R.string.smileys_and_people),
    ANIMALS_AND_NATURE(R.string.animals_and_nature),
    FOOD_AND_DRINKS(R.string.food_and_drink),
    ACTIVITY_AND_SPORTS(R.string.activity_and_sports),
    TRAVEL_AND_PLACES(R.string.travel_and_places),
    OBJECTS(R.string.objects),
    SYMBOLS(R.string.symbols)
}