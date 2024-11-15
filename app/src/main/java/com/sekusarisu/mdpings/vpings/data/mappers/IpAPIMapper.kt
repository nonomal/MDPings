package com.sekusarisu.mdpings.vpings.data.mappers

import com.sekusarisu.mdpings.vpings.data.networking.dto.IpAPIDto
import com.sekusarisu.mdpings.vpings.domain.IpAPI

fun IpAPIDto.toIpAPI(): IpAPI {
    return IpAPI(
        query = query,
        status = status,
        country = country,
        countryCode = countryCode,
        region = region,
        regionName = regionName,
        city = city,
        zip = zip,
        lat = lat,
        lon = lon,
        timezone = timezone,
        isp = isp,
        org = org,
        `as` = `as`
    )
}