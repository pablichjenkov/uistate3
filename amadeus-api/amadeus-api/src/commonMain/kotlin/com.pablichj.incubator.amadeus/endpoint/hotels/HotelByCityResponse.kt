package com.pablichj.incubator.amadeus.endpoint.hotels

import AmadeusError
import com.pablichj.incubator.amadeus.endpoint.hotels.model.HotelListingBody

sealed class HotelByCityResponse {
    class Error(val error: AmadeusError) : HotelByCityResponse()
    class Success(val hotelListingBody: HotelListingBody) : HotelByCityResponse()
}
