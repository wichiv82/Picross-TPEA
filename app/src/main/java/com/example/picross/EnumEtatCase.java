package com.example.picross;

public enum EnumEtatCase {
    BLANCHE(0),
    NOIRE(1),
    MARQUE(2);

    int etat;

    EnumEtatCase(int etat){
        this.etat = etat;
    }

    public int getEtat(){
        return etat;
    }

    public EnumEtatCase getNextEtat(){
        if(etat == 0)
            return EnumEtatCase.NOIRE;
        else if (etat == 1)
            return EnumEtatCase.MARQUE;
        else
            return EnumEtatCase.BLANCHE;
    }

    public static EnumEtatCase valueOf(int i){
        switch (i){
            case 0:
                return BLANCHE;
            case 1:
                return NOIRE;
            case 2 :
                return MARQUE;

            default:
                return null;

        }
    }
}
