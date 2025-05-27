package com.guilherme.marvelcharacters.remote

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual fun currentTimestamp(): String {
    return (NSDate().timeIntervalSince1970 * 1000).toLong().toString()
}