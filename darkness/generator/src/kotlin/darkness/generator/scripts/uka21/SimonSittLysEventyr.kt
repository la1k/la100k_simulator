package darkness.generator.scripts.uka21

import java.awt.Color

class SimonSittLysEventyr : BaseScript() {
    override suspend fun run() {
        super.run()

        /*
        for(i in 0 until allBulbs.size/2)
        {
            set(allBulbs[i], 0, 255, 0)
            set(allBulbs[allBulbs.size-1-i], 255, 0, 0)
            skip(2)
        }

        for(i in 0 until allBulbs.size/2)
        {
            set(allBulbs[allBulbs.size/2-i], 0, 0, 255)
            set(allBulbs[allBulbs.size/2 + i], 0, 255, 0)
            skip(2)
        }
        */

        //Masterball:

        //Svart strek RGB: 56,51,48
        //Hvit fargen RGB: 203,203,201
        //Rød fargen på sidene RGB: 162,92,126
        //mørkeblå/lilla RGB: 54,45,92
        // M fargen RGB: 137,132,164

        /*
        Planen er å få mappet fargene, for så å ordne en vibrasjonssekvens der den midterste pæra i
        m-en veksler mellom rød-farge og vanlig standard farge, etter 3 vibrasjoner blir den svart
        som hinter i at vi har fanget en pokemon!


         */
/*
        for(i in 0 until allBulbs.size){
            set(allBulbs[i], 0, 255, 0)
            skip(10)
            set(allBulbs[i], 0, 0, 0)
        }
*/



        //Pære 4,5,6 skal være RGB: 162,92,126
        //set(allBulbs[3], purple)
        //set(allBulbs[4], purple)
        //set(allBulbs[5], purple)
        //Pære 102, 103, 104 skal være RGB: 162,92,126
        //set(bulb(102), purple)
        //set(bulb(103), purple)
        //set(bulb(104), purple)

        val purple = Color(162,92,126)
        val lilla = group(4,5,6,102, 103, 104)
        set(lilla, purple)

        /*
        7, 8, 9, 23, 24, 41, 42, 43, 44, 54, 56, 71, 72, 73, 84, 91, 92, 93 Skal være
        RGB: 54,45,92
        */
        val blå_lilla = Color(54,45,92)
        val blå_lill_Bulbs = group(7, 8, 9, 23, 24, 41, 42, 43, 44, 54, 56, 71, 72, 73, 84, 91, 92, 93)
        set(blå_lill_Bulbs, blå_lilla)

        /*
        3, 10, 22, 25, 31, 32, 33, 45, 53, 57, 63, 64, 74, 83, 85, 94, 95, 104
         Svart strek RGB: 56,51,48
         */
        val svart_strek = Color(56,51,48)
        val svart_strek_gruppe = group(3, 10, 22, 25, 31, 32, 33, 45, 53, 57, 63, 64, 74, 83, 85, 94, 95, 104)
        set(svart_strek_gruppe, svart_strek)

        /*
        1, 2, 11, 12, 13,
        21, 26, 27,
        34, 35, 36, 37, 38
        46, 47, 48
        51, 52, 58, 59
        61, 62, 65, 66, 67
        75, 76
        81, 82, 86, 87, 88
        96, 97, 98
        105, 106, 107, 108
         */
        val hvit_farge = Color(203,203,201)
        val hvit_gruppe = group(1, 2, 11, 12, 13, 21, 26, 27, 34, 35, 36, 37, 38, 46, 47, 48,51, 52, 58, 59,61, 62, 65, 66, 67,75, 76, 81, 82, 86, 87, 88,96, 97, 98,105, 106, 107, 108)
        set(hvit_gruppe,hvit_farge)

        //Pære nummer 55 (hvisj eg går ut ifra allBulbs skal det være allBulbs[54], er midten av M-en
        for (i in 0 until 3)
        {
            set(bulb(55), 137,132,164)
            skip(30) //20 frames per sekund
            set(bulb(55), 255, 0, 0)
            skip(30) //20 frames per sekund
        }
        set(bulb(55),255,215,0)
        //set(E, 137, 132, 164)
        skip(100)





    }
}
