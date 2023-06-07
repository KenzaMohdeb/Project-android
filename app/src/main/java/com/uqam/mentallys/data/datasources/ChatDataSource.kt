package com.uqam.mentallys.data.datasources

import androidx.annotation.Keep
import androidx.room.Entity
import com.uqam.mentallys.R
import com.uqam.mentallys.model.ChatResourceLoc
import java.util.*
import javax.inject.Inject
@Keep
@Entity(tableName = "ChatDataSource")
class ChatDataSource @Inject constructor() {
    fun load(): List<ChatResourceLoc> {
        return listOf<ChatResourceLoc>(
            ChatResourceLoc(
                UUID.randomUUID(),
                "0",
                "Luc Vigneault",
                "Homme",
                R.drawable.luc_vigneault,
                "ph",
                R.drawable.luc_vigneault.toString()
                ,
                "740 Rue Galt O",
                "J1H 1Z2",
                "Sherbrooke",
                "Québec",
                "Canada",
                "45.386790;-71.918550",
                "+1 (819) 563-1363",
                "luc.vigneault@gmail.com",
                "APPAMM-Estrie offre soutien et information aux proches de personnes atteintes de maladie mentale.",
                "lun-ven, 9h-12h / 13h30 - 16h30",
                "Disponible maintenant",
                "1.8 km",
                listOf("Anxiété", "Dépression"),
                listOf("Travailleur social"),
                "Français",
                "Frais réduit",
                listOf("Adultes"),
                listOf("Présentiel", "À distance"),
                "8:acs:ce1e4b81-dc3a-42c0-9a71-7ff148d57eb3_00000016-9f08-e2fd-7f07-113a0d00da1d",
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEwNiIsIng1dCI6Im9QMWFxQnlfR3hZU3pSaXhuQ25zdE5PU2p2cyIsInR5cCI6IkpXVCJ9.eyJza3lwZWlkIjoiYWNzOmNlMWU0YjgxLWRjM2EtNDJjMC05YTcxLTdmZjE0OGQ1N2ViM18wMDAwMDAxNi05ZjA4LWUyZmQtN2YwNy0xMTNhMGQwMGRhMWQiLCJzY3AiOjE3OTIsImNzaSI6IjE2NzUwMjMwNDAiLCJleHAiOjE2NzUxMDk0NDAsInJnbiI6ImNhIiwiYWNzU2NvcGUiOiJjaGF0LHZvaXAiLCJyZXNvdXJjZUlkIjoiY2UxZTRiODEtZGMzYS00MmMwLTlhNzEtN2ZmMTQ4ZDU3ZWIzIiwicmVzb3VyY2VMb2NhdGlvbiI6ImNhbmFkYSIsImlhdCI6MTY3NTAyMzA0MH0.iRGzyqqI15oKd-lULiAd6HJSt4EbFbplf3AR6F81da6hKuKMMApnEnRZVpHr_DUc0v62zkXp75qFthI-E-VgoAdULQjEo6oMuQSN22TVvI3HKl1RkY2rpIow4fzN0GLz62SZHA8LhCwRytfqM0noNVgchvk3g0seMP35qhGcnTpMWiaBZiV-QSzmwraqQ3re9o01bdaAJg7cvYOFHC6anQ-zC-K_BawxZjseDQTIaCbgeF3VOQOsZRWk4tvONd3AVAhhyxMryM7m0-GVhlNnZ-_DigcVu7Wm-wDEd947-TAhICt7qlmQP5eCSdSM7QOTGJqgvXJW5jPB_ZLJyND-mw",
                "2023-01-30 20:10:40.2600107 +00:00"


            ),
            ChatResourceLoc(
                UUID.randomUUID(),
                "1",
                "Julie Tansey",
                "Femme",
                R.drawable.julie_tansey,
                "ph",
                "",
                "740 Rue Galt O",
                "J1H 1Z2",
                "Sherbrooke",
                "Québec",
                "Canada",
                "45.386790;-71.918550",
                "+1 (819) 563-1363",
                "julie.tansey@gmail.com",
                "APPAMM-Estrie offre soutien et information aux proches de personnes atteintes de maladie mentale.",
                "lun-ven, 9h-12h / 13h30 - 16h30",
                "Disponible maintenant",
                "1.8 km",
                listOf("Anxiété", "Dépression"),
                listOf("Travailleur social"),
                "Français",
                "Frais réduit",
                listOf("Adultes"),
                listOf("Présentiel", "À distance"),
                "8:acs:ce1e4b81-dc3a-42c0-9a71-7ff148d57eb3_00000016-9f09-3268-7f07-113a0d00da1f",
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEwNiIsIng1dCI6Im9QMWFxQnlfR3hZU3pSaXhuQ25zdE5PU2p2cyIsInR5cCI6IkpXVCJ9.eyJza3lwZWlkIjoiYWNzOmNlMWU0YjgxLWRjM2EtNDJjMC05YTcxLTdmZjE0OGQ1N2ViM18wMDAwMDAxNi05ZjA5LTMyNjgtN2YwNy0xMTNhMGQwMGRhMWYiLCJzY3AiOjE3OTIsImNzaSI6IjE2NzUwMjMwNjAiLCJleHAiOjE2NzUxMDk0NjAsInJnbiI6ImNhIiwiYWNzU2NvcGUiOiJjaGF0LHZvaXAiLCJyZXNvdXJjZUlkIjoiY2UxZTRiODEtZGMzYS00MmMwLTlhNzEtN2ZmMTQ4ZDU3ZWIzIiwicmVzb3VyY2VMb2NhdGlvbiI6ImNhbmFkYSIsImlhdCI6MTY3NTAyMzA2MH0.v6uuVl07O3xaAnAHbvP7DWF2qvMSWtM61XGRm-PkSFD9SDyMR0ry6CPRM0CDUF4qMfBrntMfWy18zs8hgxnuzwMf5u4RwNGwrM46UKT9wabdybmMHM13yJA9bkF8KgajSPskWErPOFMR3ZXcBQcrDgKAXj-Xl-VVySVGnYzaACXtWPmWYzTDLcFX92lLX8mvzyQcvdnIBUToLire2ZxG9pw9zjjEtVXaku2AWIoXZ5MvVJiqKxM1l8syeKzek0HF0vulQ4zm7tcsq_wYi-fdJMrZ-B-Z271okgJxWg9VrXYlrll_yDyHQOvNGEHSk7Wa0npSREzZfAJqStleXbncng",
                "2023-01-30 20:11:00.5907565 +00:00"

            ),
            ChatResourceLoc(
                UUID.randomUUID(),
                "2",
                "Remi Lalonde",
                "Homme",
                R.drawable.remi_lalonde,
                "ph",
                "",
                "740 Rue Galt O",
                "J1H 1Z2",
                "Sherbrooke",
                "Québec",
                "Canada",
                "45.386790;-71.918550",
                "+1 (819) 563-1363",
                "remi.lalonde@gmail.com",
                "APPAMM-Estrie offre soutien et information aux proches de personnes atteintes de maladie mentale.",
                "lun-ven, 9h-12h / 13h30 - 16h30",
                "Disponible maintenant",
                "1.8 km",
                listOf("Anxiété", "Dépression"),
                listOf("Travailleur social"),
                "Français",
                "Frais réduit",
                listOf("Adultes"),
                listOf("Présentiel", "À distance"),
                "8:acs:ce1e4b81-dc3a-42c0-9a71-7ff148d57eb3_00000016-9f09-6c3e-7f07-113a0d00da22",
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEwNiIsIng1dCI6Im9QMWFxQnlfR3hZU3pSaXhuQ25zdE5PU2p2cyIsInR5cCI6IkpXVCJ9.eyJza3lwZWlkIjoiYWNzOmNlMWU0YjgxLWRjM2EtNDJjMC05YTcxLTdmZjE0OGQ1N2ViM18wMDAwMDAxNi05ZjA5LTZjM2UtN2YwNy0xMTNhMGQwMGRhMjIiLCJzY3AiOjE3OTIsImNzaSI6IjE2NzUwMjMwNzUiLCJleHAiOjE2NzUxMDk0NzUsInJnbiI6ImNhIiwiYWNzU2NvcGUiOiJjaGF0LHZvaXAiLCJyZXNvdXJjZUlkIjoiY2UxZTRiODEtZGMzYS00MmMwLTlhNzEtN2ZmMTQ4ZDU3ZWIzIiwicmVzb3VyY2VMb2NhdGlvbiI6ImNhbmFkYSIsImlhdCI6MTY3NTAyMzA3NX0.pnuFgogvaFuuXhDfz4zQbziXggaeP2IcallNZpws7Iw8nmTdcFj4s2MCXvjIXK86OSoKfMC49F4iSyEVPJg6M_vdJBuGtaFfweM2CrHwjcaQKCi2X3tm0JYmSaQSRfQf4NW9e-InMWfTml6sUtxjuYIrce-lSUfUCuMC4x2XGZRKXKnTai-PqAJKqE3bmbe172rCWwlMegxx1EHd74vCChvmzcxrYhSKSwLdq4o4LwVO6q4lu9kwqABNfNT_Qd4WxfL8nRNW7ELpMXeamX0Bo3IGOU2_Po9yaAPkrP3R80q9aNvX3oc03Cv_DUL6CgRPRnayMaAnWhLFaGcJy3xNEw",
                "2023-01-30 20:11:15.3958315 +00:00"

            ),
            ChatResourceLoc(
                UUID.randomUUID(),
                "4",
                "raphaelle dufour",
                "Femme",
                R.drawable.raphaelle_dufour,
                "ph",
                "",
                "740 Rue Galt O",
                "J1H 1Z2",
                "Sherbrooke",
                "Québec",
                "Canada",
                "45.386790;-71.918550",
                "+1 (819) 563-1363",
                "raphaelle.dufour@gmail.com",
                "APPAMM-Estrie offre soutien et information aux proches de personnes atteintes de maladie mentale.",
                "lun-ven, 9h-12h / 13h30 - 16h30",
                "Disponible maintenant",
                "1.8 km",
                listOf("Anxiété", "Dépression"),
                listOf("Travailleur social"),
                "Français",
                "Frais réduit",
                listOf("Adultes"),
                listOf("Présentiel", "À distance"),
                "8:acs:ce1e4b81-dc3a-42c0-9a71-7ff148d57eb3_00000016-9f09-a9a3-7f07-113a0d00da24",
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEwNiIsIng1dCI6Im9QMWFxQnlfR3hZU3pSaXhuQ25zdE5PU2p2cyIsInR5cCI6IkpXVCJ9.eyJza3lwZWlkIjoiYWNzOmNlMWU0YjgxLWRjM2EtNDJjMC05YTcxLTdmZjE0OGQ1N2ViM18wMDAwMDAxNi05ZjA5LWE5YTMtN2YwNy0xMTNhMGQwMGRhMjQiLCJzY3AiOjE3OTIsImNzaSI6IjE2NzUwMjMwOTEiLCJleHAiOjE2NzUxMDk0OTEsInJnbiI6ImNhIiwiYWNzU2NvcGUiOiJjaGF0LHZvaXAiLCJyZXNvdXJjZUlkIjoiY2UxZTRiODEtZGMzYS00MmMwLTlhNzEtN2ZmMTQ4ZDU3ZWIzIiwicmVzb3VyY2VMb2NhdGlvbiI6ImNhbmFkYSIsImlhdCI6MTY3NTAyMzA5MX0.nuizQPsrdJ5GOokWTnBqiLCE1AOMGG6MJQq1ItkIQYeBLWb0df2yl8e8-kvzRY9C9FB45g3V39GTm8zNoSt3NIyxgvJmdYfY4pvAMQu5zpSfj4DfVU2srXShxhXjesh8Kahs_hpJDSzwyKqmJficVCsZ_Rb55kboOAY8gbirYBlH8x4SYEvE2JkfEqtBNRaizklDb6DYHETFoXNiPuZq-GjOg3Af8b2gl8luoAzYPyTBTpNDxBUAvE7jo-EzooH-mIujyGJsLYGIjPaLTLmQ_9YbO8bByBP15mx-6EPEX2_QxZ0MXKbxExBx4K1vaQrvfFlD-_hDBGhdHCk-GaExXw",
                "2023-01-30 20:11:31.1130049 +00:00"

            )
        )
    }
}