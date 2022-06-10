package fr.ensimag.deca.codegen;

public class RegisterManager {
    private int nbRegisterMax = 16;

    public int getNbRegisterMax() {
        return nbRegisterMax - 1;
    }

    public void setNbRegisterMax(int n) {
        nbRegisterMax = n;
    }
}