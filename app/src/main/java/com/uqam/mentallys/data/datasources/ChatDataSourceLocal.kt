package com.uqam.mentallys.data.datasources

import com.uqam.mentallys.R
import com.uqam.mentallys.model.ChatResourceLoc
import java.util.*
import javax.inject.Inject

class ChatDataSourceLocal @Inject constructor() {

    var mylist = listOf<ChatResourceLoc>(
        ChatResourceLoc(
            UUID.randomUUID(),
            "DFFBDEFC-4B47-46C7-9510-08DB00104450",
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
            "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEwNiIsIng1dCI6Im9QMWFxQnlfR3hZU3pSaXhuQ25zdE5PU2p2cyIsInR5cCI6IkpXVCJ9.eyJza3lwZWlkIjoiYWNzOmNlMWU0YjgxLWRjM2EtNDJjMC05YTcxLTdmZjE0OGQ1N2ViM18wMDAwMDAxNi1kYzQ2LTEzOTktOWMzMi04ZTNhMGQwMDlkOTYiLCJzY3AiOjE3OTIsImNzaSI6IjE2NzYwNTA0NjAiLCJleHAiOjE2NzYxMzY4NjAsInJnbiI6ImNhIiwiYWNzU2NvcGUiOiJjaGF0LHZvaXAiLCJyZXNvdXJjZUlkIjoiY2UxZTRiODEtZGMzYS00MmMwLTlhNzEtN2ZmMTQ4ZDU3ZWIzIiwicmVzb3VyY2VMb2NhdGlvbiI6ImNhbmFkYSIsImlhdCI6MTY3NjA1MDQ2MH0.LDPMeeRL-WS09ECr_UBF-RLhvGl4iT8bIuBn9guYrRKWUl-lM0l1me6vs7Qvlr5-JkAiKDXg7QZtq3UEar1snEixhUe9l3px2-Kk_mHN4dQJIpLvouIUMI8EnvYHJoszQ5ZBW1FSZcAHTCJASTSdCxC5L9SG2sTmS496bbkNjbI6yoCkK-_aAvtmJ3Ln0sW3aPrutXiZ7RhH4NFRwBWZV00FGBuIgVji62xgecUf5MRCmvTQrSzlRLn4ALPuT-oci2OaKJtPSdpJ0i1y6BbQP_COZPjoc-ZrwyG5Z6Ewfs16uyaTMx-jAxg3TvN0opmJxidVJU_WG-hGNGTwa-BLIg",
            "2023-01-30 20:10:40.2600107 +00:00"


        ),
        ChatResourceLoc(
            UUID.randomUUID(),
            "6E4269B9-EA6E-49E6-2720-08DB005EE634",
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
            "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEwNiIsIng1dCI6Im9QMWFxQnlfR3hZU3pSaXhuQ25zdE5PU2p2cyIsInR5cCI6IkpXVCJ9.eyJza3lwZWlkIjoiYWNzOmNlMWU0YjgxLWRjM2EtNDJjMC05YTcxLTdmZjE0OGQ1N2ViM18wMDAwMDAxNi1kYzQ2LTEzOTktOWMzMi04ZTNhMGQwMDlkOTYiLCJzY3AiOjE3OTIsImNzaSI6IjE2NzYwNTA0NjAiLCJleHAiOjE2NzYxMzY4NjAsInJnbiI6ImNhIiwiYWNzU2NvcGUiOiJjaGF0LHZvaXAiLCJyZXNvdXJjZUlkIjoiY2UxZTRiODEtZGMzYS00MmMwLTlhNzEtN2ZmMTQ4ZDU3ZWIzIiwicmVzb3VyY2VMb2NhdGlvbiI6ImNhbmFkYSIsImlhdCI6MTY3NjA1MDQ2MH0.LDPMeeRL-WS09ECr_UBF-RLhvGl4iT8bIuBn9guYrRKWUl-lM0l1me6vs7Qvlr5-JkAiKDXg7QZtq3UEar1snEixhUe9l3px2-Kk_mHN4dQJIpLvouIUMI8EnvYHJoszQ5ZBW1FSZcAHTCJASTSdCxC5L9SG2sTmS496bbkNjbI6yoCkK-_aAvtmJ3Ln0sW3aPrutXiZ7RhH4NFRwBWZV00FGBuIgVji62xgecUf5MRCmvTQrSzlRLn4ALPuT-oci2OaKJtPSdpJ0i1y6BbQP_COZPjoc-ZrwyG5Z6Ewfs16uyaTMx-jAxg3TvN0opmJxidVJU_WG-hGNGTwa-BLIg",
            "2023-01-30 20:11:00.5907565 +00:00"

        ),
        ChatResourceLoc(
            UUID.randomUUID(),
            "9EFB843A-C322-4010-69C6-08DB0233FA71",
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
            "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEwNiIsIng1dCI6Im9QMWFxQnlfR3hZU3pSaXhuQ25zdE5PU2p2cyIsInR5cCI6IkpXVCJ9.eyJza3lwZWlkIjoiYWNzOmNlMWU0YjgxLWRjM2EtNDJjMC05YTcxLTdmZjE0OGQ1N2ViM18wMDAwMDAxNi05ZjA5LTZjM2UtN2YwNy0xMTNhMGQwMGRhMjIiLCJzY3AiOjE3OTIsImNzaSI6IjE2Nzc3OTM3OTMiLCJleHAiOjE2Nzc4ODAxOTMsInJnbiI6ImNhIiwiYWNzU2NvcGUiOiJjaGF0LHZvaXAiLCJyZXNvdXJjZUlkIjoiY2UxZTRiODEtZGMzYS00MmMwLTlhNzEtN2ZmMTQ4ZDU3ZWIzIiwicmVzb3VyY2VMb2NhdGlvbiI6ImNhbmFkYSIsImlhdCI6MTY3Nzc5Mzc5M30.eX39w4FYvGeuzEBEFdBg6Egrk36E_RJwNkUP8hpPduITWUJR4YNxCJdsuVSi3aRNKvNu1ICx76cXN32UtAXpKbdJ-JtwFz-J4g05dmEMAQeKyhpcnICThxJAAl7aC2Bb9qkvgdw94cFsN6OkTpP64AH1c4XGD7jmFfFUwvfNlqvm5dvel03COb5ySzjOWSANA9VJdBL7wIShGUxbUuZXvZ17K3kQ_PvDpEthGN0q5XRpDpG8VJAE3CwGcFpkvKdZBk5pAWuTOOTWOxj2s8ez39Lb98jpUEXVfREQfznE2ZPBVyQGA-rr6CJ_7bcVbNLSdqXORvKZgJO_WTdKZB5Exg",
            "2023-01-30 20:11:15.3958315 +00:00"

        ),
        ChatResourceLoc(
            UUID.randomUUID(),
            "9D073E79-46EE-45A0-69C7-08DB0233FA71",
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
            "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEwNiIsIng1dCI6Im9QMWFxQnlfR3hZU3pSaXhuQ25zdE5PU2p2cyIsInR5cCI6IkpXVCJ9.eyJza3lwZWlkIjoiYWNzOmNlMWU0YjgxLWRjM2EtNDJjMC05YTcxLTdmZjE0OGQ1N2ViM18wMDAwMDAxNi1kYzQ2LTEzOTktOWMzMi04ZTNhMGQwMDlkOTYiLCJzY3AiOjE3OTIsImNzaSI6IjE2NzYwNTA0NjAiLCJleHAiOjE2NzYxMzY4NjAsInJnbiI6ImNhIiwiYWNzU2NvcGUiOiJjaGF0LHZvaXAiLCJyZXNvdXJjZUlkIjoiY2UxZTRiODEtZGMzYS00MmMwLTlhNzEtN2ZmMTQ4ZDU3ZWIzIiwicmVzb3VyY2VMb2NhdGlvbiI6ImNhbmFkYSIsImlhdCI6MTY3NjA1MDQ2MH0.LDPMeeRL-WS09ECr_UBF-RLhvGl4iT8bIuBn9guYrRKWUl-lM0l1me6vs7Qvlr5-JkAiKDXg7QZtq3UEar1snEixhUe9l3px2-Kk_mHN4dQJIpLvouIUMI8EnvYHJoszQ5ZBW1FSZcAHTCJASTSdCxC5L9SG2sTmS496bbkNjbI6yoCkK-_aAvtmJ3Ln0sW3aPrutXiZ7RhH4NFRwBWZV00FGBuIgVji62xgecUf5MRCmvTQrSzlRLn4ALPuT-oci2OaKJtPSdpJ0i1y6BbQP_COZPjoc-ZrwyG5Z6Ewfs16uyaTMx-jAxg3TvN0opmJxidVJU_WG-hGNGTwa-BLIg",
            "2023-01-30 20:11:31.1130049 +00:00"

        )
    )
    fun load(): List<ChatResourceLoc> {
        return mylist
    }
    fun getResourceByEmail(email:String): ChatResourceLoc? {

        val numbersIterator = mylist.iterator()
        while (numbersIterator.hasNext()) {
            val element = numbersIterator.next()
            if(email == element.mail)
                return element
        }
        return null
    }
    fun getResourceByCommunicationUserId(CommunicationUserId:String): ChatResourceLoc? {

        val numbersIterator = mylist.iterator()
        while (numbersIterator.hasNext()) {
            val element = numbersIterator.next()
            if(CommunicationUserId == element.CommunicationUserId)
                return element
        }
        return null
    }
}