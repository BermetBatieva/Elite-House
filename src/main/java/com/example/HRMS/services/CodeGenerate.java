package com.example.HRMS.services;

public class CodeGenerate {

    public static char[] symbol = {'A','a','B','b','C','c','D','d','E','e','F','f','G','G','H','h','I','i','J','J',
            'K','K','L','l','M','m','N','n','P','p','Q','q','R','r','S','s','T','t',
            'U','u','V','v','W','w','X','x','Y','y','Z','z','1','2','3','4','5','6','7','8','9'};

    public static String generate(){
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int n = (int) (Math.random()*(symbol.length-1));
            code.append(symbol[n]);
        }
        return code.toString();
    }
}
