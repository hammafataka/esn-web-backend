package dev.mfataka.esnzlin.utils;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.experimental.UtilityClass;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 21.08.2024 18:51
 */
@UtilityClass
public class TimeUtils {


    public Date convertLocalDateTimeToDate(final LocalDateTime localDateTime) {
        return java.sql.Timestamp.valueOf(localDateTime);
    }
}
