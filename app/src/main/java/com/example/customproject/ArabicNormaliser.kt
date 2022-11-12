package com.example.customproject

class ArabicNormaliser(var input: String) {

    var output = normalise()


    private fun normalise():String
    {
        input=input.replace("\u0610", "");//ARABIC SIGN SALLALLAHOU ALAYHE WA SALLAM
        input=input.replace("\u0611", "");//ARABIC SIGN ALAYHE ASSALLAM
        input=input.replace("\u0612", "");//ARABIC SIGN RAHMATULLAH ALAYHE
        input=input.replace("\u0613", "");//ARABIC SIGN RADI ALLAHOU ANHU
        input=input.replace("\u0614", "");//ARABIC SIGN TAKHALLUS

        //Remove koranic anotation
        input=input.replace("\u0615", "");//ARABIC SMALL HIGH TAH
        input=input.replace("\u0616", "");//ARABIC SMALL HIGH LIGATURE ALEF WITH LAM WITH YEH
        input=input.replace("\u0617", "");//ARABIC SMALL HIGH ZAIN
        input=input.replace("\u0618", "");//ARABIC SMALL FATHA
        input=input.replace("\u0619", "");//ARABIC SMALL DAMMA
        input=input.replace("\u061A", "");//ARABIC SMALL KASRA
        input=input.replace("\u06D6", "");//ARABIC SMALL HIGH LIGATURE SAD WITH LAM WITH ALEF MAKSURA
        input=input.replace("\u06D7", "");//ARABIC SMALL HIGH LIGATURE QAF WITH LAM WITH ALEF MAKSURA
        input=input.replace("\u06D8", "");//ARABIC SMALL HIGH MEEM INITIAL FORM
        input=input.replace("\u06D9", "");//ARABIC SMALL HIGH LAM ALEF
        input=input.replace("\u06DA", "");//ARABIC SMALL HIGH JEEM
        input=input.replace("\u06DB", "");//ARABIC SMALL HIGH THREE DOTS
        input=input.replace("\u06DC", "");//ARABIC SMALL HIGH SEEN
        input=input.replace("\u06DD", "");//ARABIC END OF AYAH
        input=input.replace("\u06DE", "");//ARABIC START OF RUB EL HIZB
        input=input.replace("\u06DF", "");//ARABIC SMALL HIGH ROUNDED ZERO
        input=input.replace("\u06E0", "");//ARABIC SMALL HIGH UPRIGHT RECTANGULAR ZERO
        input=input.replace("\u06E1", "");//ARABIC SMALL HIGH DOTLESS HEAD OF KHAH
        input=input.replace("\u06E2", "");//ARABIC SMALL HIGH MEEM ISOLATED FORM
        input=input.replace("\u06E3", "");//ARABIC SMALL LOW SEEN
        input=input.replace("\u06E4", "");//ARABIC SMALL HIGH MADDA
        input=input.replace("\u06E5", "");//ARABIC SMALL WAW
        input=input.replace("\u06E6", "");//ARABIC SMALL YEH
        input=input.replace("\u06E7", "");//ARABIC SMALL HIGH YEH
        input=input.replace("\u06E8", "");//ARABIC SMALL HIGH NOON
        input=input.replace("\u06E9", "");//ARABIC PLACE OF SAJDAH
        input=input.replace("\u06EA", "");//ARABIC EMPTY CENTRE LOW STOP
        input=input.replace("\u06EB", "");//ARABIC EMPTY CENTRE HIGH STOP
        input=input.replace("\u06EC", "");//ARABIC ROUNDED HIGH STOP WITH FILLED CENTRE
        input=input.replace("\u06ED", "");//ARABIC SMALL LOW MEEM

        //Remove tatweel
        input=input.replace("\u0640", "");

        //Remove tashkeel
        input=input.replace("\u064B", "");//ARABIC FATHATAN
        input=input.replace("\u064C", "");//ARABIC DAMMATAN
        input=input.replace("\u064D", "");//ARABIC KASRATAN
        input=input.replace("\u064E", "");//ARABIC FATHA
        input=input.replace("\u064F", "");//ARABIC DAMMA
        input=input.replace("\u0650", "");//ARABIC KASRA
        input=input.replace("\u0651", "");//ARABIC SHADDA
        input=input.replace("\u0652", "");//ARABIC SUKUN
        input=input.replace("\u0653", "");//ARABIC MADDAH ABOVE
        input=input.replace("\u0654", "");//ARABIC HAMZA ABOVE
        input=input.replace("\u0655", "");//ARABIC HAMZA BELOW
        input=input.replace("\u0656", "");//ARABIC SUBSCRIPT ALEF
        input=input.replace("\u0657", "");//ARABIC INVERTED DAMMA
        input=input.replace("\u0658", "");//ARABIC MARK NOON GHUNNA
        input=input.replace("\u0659", "");//ARABIC ZWARAKAY
        input=input.replace("\u065A", "");//ARABIC VOWEL SIGN SMALL V ABOVE
        input=input.replace("\u065B", "");//ARABIC VOWEL SIGN INVERTED SMALL V ABOVE
        input=input.replace("\u065C", "");//ARABIC VOWEL SIGN DOT BELOW
        input=input.replace("\u065D", "");//ARABIC REVERSED DAMMA
        input=input.replace("\u065E", "");//ARABIC FATHA WITH TWO DOTS
        input=input.replace("\u065F", "");//ARABIC WAVY HAMZA BELOW
        input=input.replace("\u0670", "");//ARABIC LETTER SUPERSCRIPT ALEF


        return input
    }

}